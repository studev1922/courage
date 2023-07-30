
app.factory('security', function ($http, $cookies, $window) {
    var TOKEN_KEY = 'ES_TOKEN';

    // Define a private variable to store the authenticated user data
    var authenticated = null;

    // Define a private function to parse the JWT and get the user data
    var parseJwt = function (token) {
        if (!token) return;
        else if (token.startsWith('Bearer')) token = token.substring(7);
        var base64Url = token.split('.')[1];
        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        return JSON.parse($window.atob(base64));
    };

    var loadToken = function () {
        let payload = parseJwt($cookies.get(TOKEN_KEY));
        return payload?.exp > Date.now() / 1e3
            ? authenticated = payload : undefined;
    }

    var setToken = function (token) {
        if (!token) return;
        authenticated = parseJwt(token);
        $cookies.put(TOKEN_KEY, token, { 'expires': new Date(authenticated.exp * 1e3) });
        return authenticated;
    }

    var getAuth = function () {
        return authenticated || loadToken();
    }

    // has any role
    var hasRole = function (...roles) {
        if (roles == undefined || !roles.length || !authenticated) return false;
        return authenticated.roles.findIndex(role => roles.includes(role)) > -1;
    }

    // Define a public function to check if the user is logged in
    var isLoggedIn = function () {
        var token = $cookies.get(TOKEN_KEY);
        if (token) {
            var payload = parseJwt(token);
            return payload.exp > Date.now() / 1e3;
        }
        return false;
    }

    // Define a public function to log in by params
    var loginByParams = async function (user) {
        let token;
        if (!user) user = {
            username: prompt("input your username"),
            password: prompt("input your password")
        };

        return $http
            .post(`${server}/oauth/login`, user)
            .then(res => {
                token = res.headers('Authorization') || res.data.token;
                setToken(token);
                return authenticated;
            });
    };

    // Define a public function to log out
    var logout = function (isAllow) {
        if (authenticated) {
            let { username } = authenticated;
            if (isAllow || confirm(`Do you want to logout account "${username}"?`)) {
                $cookies.remove(TOKEN_KEY);
                authenticated = undefined;
                return true;
            }
        } else console.warn(`Expansive System isn't authenticated!`);
        return false
    };

    // Define a public function to refresh the token
    var refreshToken = function () {
        console.log('TODO: ...');
    };

    // Return an object with the public functions as properties
    return {
        loadToken, setToken, getAuth, hasRole, isLoggedIn,
        getToken: $cookies.get(TOKEN_KEY), // load in client 
        loginByParams, logout, refreshToken // call api
    };
});