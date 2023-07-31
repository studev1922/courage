app.controller('securityControl', function ($scope, $routeParams, security) {

    $scope.register = async function (user, target, isLogin) {
        let form = document.querySelector(target);
        let data = new FormData(form); // FORM DATA
        $http.post(`${server}/oauth/register`, data,
            { headers: { 'Content-Type': undefined } }
        ).then(async res => {
            form.reset(); // clear form
            Object.assign(user, { time: 0 });
            bsfw.hideModel(target);
            $scope.pushMessage(`${res.data.username} register successfull`, 5e3);
            if (isLogin) {
                let token = res.headers('Authorization');
                $scope.authenticated = security.setToken(token); // login
                $scope.pushMessage(`Welcome ${res.data.username} logged in.`, 5e3);
            }
        }).catch(err => {
            $scope.pushMessage(err.message || err.data?.message, 5e3);
            console.error(err)
        });
    }

    $scope.$watch('$stateChangeSuccess', () => {
        bsfw.loadPopovers();

        switch ($routeParams['page']) {
            case 'register': $scope.srctab = 'components/security/_register.htm'; break;
            default: $scope.srctab = 'components/security/_options.htm'; break;
        }
    })
})