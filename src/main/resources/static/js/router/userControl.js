// usercontrol
app.controller('usercontrol', function ($scope, $routeParams, security) {
    let path = "accounts", // path api
        dataName = 'mdata', // data
        entity = 'user', // form data
        key = 'uid'; // key of entity

    const u = {
        uid: -1,
        username: undefined,
        email: undefined,
        fullname: undefined,
        regTime: new Date(),
        images: [],
        access: 0,
        roles: [0],
        platforms: [0]
    };

    var httpConfig = {
        transformRequest: angular.identity,
        headers: { 
            'Content-Type': undefined,
            'Authorization': security.getToken,
        }
    };

    (() => {
        // handle nav-tabs event clicked
        let items = tabs.querySelectorAll('.nav-link');
        items.forEach(item => item.addEventListener('click', _ => {
            items.forEach(e => e.classList?.remove('active')) // remove all active
            item.classList.add('active'); // show new active
            removeElements('[role="tooltip"]'); // remove all tag tooltip
        }));

        $scope[entity] = angular.copy(u);
        $scope.selectTab = (at = 0) => items[at].click();

        switch ($routeParams['page']) {
            default:
            case 'one':
                $scope.srctab = 'components/manage/_one.htm'
                items[0].click();
                break;
            case 'list':
                $scope.srctab = 'components/manage/_list.htm'
                items[1].click();
                break;
        }
    })();

    function getFormData() {
        let obj = $scope[entity];
        let input = formControl.querySelector('input[type="file"]');
        delete obj.regTime;
        return util.getFormData(obj, input?.files);
    }

    /**
     * @returns {{ id: Number, index: Number, exist: { username: String, email: String }}}
     */
    function checkUnique() {
        let index = -1, exist = {}; // $scope[dataName].findIndex
        let user = $scope[entity], id = user[key];
        $scope[dataName].forEach((e, i) => {
            if (e[key] === id) index = i; // find index by id(key)
            if (e.username === user.username && user !== e) exist.username = user.username;
            if (e.email === user.email && user !== e) exist.email = user.email;
        });
        return { id, index, exist };
    }

    const _promise = {
        success: (res, execute = 'execute') => {
            $scope.pushMessage({
                htype: 'fw-bolder bg-success text-white',
                heading: `${execute} success.`,
                body: `${execute} ${res?.username || 'data'} successfully!`
            }, 3.5e3)
            _promise.resetForm();
        },
        exception: (e, execute = 'execute') => {
            $scope.pushMessage({
                htype: 'bg-danger text-white',
                heading: `${execute} data failed`,
                body: e.message
            })
            console.error(e);
        },
        resetForm: () => {
            let input = formControl.querySelector('input[type="file"]');
            for (let type of ['text', 'file']) input.setAttribute('type', type);
            $scope[entity] = angular.copy(u); // reset binding
        }
    }

    $scope.control = {
        insert: () => {
            if ($scope[entity].password) {
                let { index, exist } = checkUnique();
                let notExecute = exist.username || exist.email;
                let mesWarning = {
                    htype: 'bg-warning text-danger',
                    btype: 'text-danger',
                    ftype: 'bg-warning',
                    heading: `save new user ${$scope[entity][key]}`,
                    body: `${exist.username || ''} ${exist.email || ''} already exist!`
                };

                if (notExecute) $scope.pushMessage(mesWarning, 5e3); // 5 seconds
                else if (index > -1) { // rare case
                    mesWarning.body = `uid: ${$scope[entity][key]} already exist!`
                    $scope.pushMessage(mesWarning, 5e3); // rare case
                } else $scope.crud.post(path, dataName, getFormData(), httpConfig)
                    .then(_promise.success).catch(_promise.exception)
            } else $scope.pushMessage('please input your password', 5000);

        },
        update: () => {
            delete $scope[entity].password; //doesn's update password
            let { index, exist } = checkUnique();
            let notExecute = exist.username || exist.email;
            let mesWarning = {
                htype: 'bg-warning text-danger',
                btype: 'text-danger',
                ftype: 'bg-warning',
                heading: `update user ${$scope[entity][key] || 'unknown.'}`,
                body: `${exist.username || ''} ${exist.email || ''} already exist!`
            };

            if (notExecute) $scope.pushMessage(mesWarning, 5e3);
            else if (index < 0) { // rare case
                mesWarning.body = `uid: ${$scope[entity][key]} doesn't exist!`
                $scope.pushMessage(mesWarning, 5e3); // rare case
            } else $scope.crud.put(path, dataName, getFormData(), index, httpConfig)
                .then(_promise.success).catch(_promise.exception)
        },
        delete: () => $scope.crud
            .delete(path, dataName, $scope[entity][key], key, httpConfig)
            .then(_promise.success).catch(_promise.exception),
        read: (e) => {
            $scope[entity] = e;
            $scope.selectTab(0);
            $scope.access = true;
        },
        clear: () => {
            if (confirm(`clear form data ${$scope[entity].username || 'user'}`)) {
                $scope.access = false;
                _promise.success(undefined, 'clear');
            }
        }
    }

    $scope.$watch('srctab', function (src) { // load all component
        $scope.$watch('$stateCngeSuccess', setTimeout(() => {
            bsfw.loadPopovers();
            if (src.endsWith('_one.htm')) bsfw.showImageInput(formControl, showInputImages);
        }, 500))
    }) // await 500 miliseconds to load popovers

    $scope.$watch('$stateChangeSuccess', async () => {
        if (security.isLoggedIn()) {
            let head = httpConfig.headers;
            if(!head.Authorization) head.Authorization = security.getToken;
            if (!$scope.ur) await $scope.loadRelationships(); // await for load all data
            await $scope.crud.get(path, dataName, undefined, httpConfig).catch(console.error)//.then(console.log)
        } else $scope.pushMessage({
            heading: 'need to login',
            body: 'This function needs to be logged in and admin',
            htype: 'bg-danger text-warning',
            btype: 'bg-dark-gradient text-danger text-center fs-3',
            ftype: 'bg-danger text-warning',
        }, 5e3);
    });
});