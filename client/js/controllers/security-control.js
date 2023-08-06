app.controller('securityControl', function ($scope, $http, $routeParams, security) {

    $scope.changePassword = async function (user, target, withLogin) {
        let form = document.querySelector(target);
        let data = new FormData(form); // FORM DATA
        let unique = data.get('unique');
        data.set('withLogin', withLogin || false);

        $http.put(`${server}/oauth/forgot-pass`, data,
            { headers: { 'Content-Type': undefined } }
        ).then(async res => {
            console.log(res.headers());
            // reset form data input
            form.reset(); // clear form
            Object.assign(user, { time: 0 });
            bsfw.hideModel(target);
            $scope.pushMessage(res.data?.message || 'Change password successfully.', 5e3);

            if (withLogin) {
                let token = res.headers('Authorization') || res.body.token;
                let auth = $scope.authenticated = security.setToken(token); // login
                if (auth) $scope.pushMessage(`Welcome ${unique} logged in.`, 5e3);
            }
        }).catch(err => {
            $scope.pushMessage(err.message || err.data?.message, 5e3);
            console.error(err)
        });
    }

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
                let token = res.headers('Authorization') || res.body.token;
                let auth = $scope.authenticated = security.setToken(token); // login
                if (auth) $scope.pushMessage(`Welcome ${res.data.username} logged in.`, 5e3);
            }
        }).catch(err => {
            $scope.pushMessage(err.message || err.data?.message, 5e3);
            console.error(err)
        });
    }

    $scope.$watch('$stateChangeSuccess', () => {
        switch ($routeParams['page']) {
            case 'forget-password': $scope.srctab = 'components/security/_forgot-pass.htm'; break;
            case 'register':
                $scope.srctab = 'components/security/_register.htm';
                break;
            default: $scope.srctab = 'components/security/_options.htm'; break;
        }

        $scope.$watch('srctab', function (src = '') { // load all component
            $scope.$watch('$stateChangeSuccess', setTimeout(() => {
                bsfw.loadPopovers();
                if (src.endsWith('_register.htm')) {
                    bsfw.showImageInput(formRegister, showInputImages);
                }
            }, 250))
        }) // await 250 miliseconds to load popovers
    })
})