// usercontrol
app.controller('usercontrol', function ($scope, $routeParams, $location, security) {
    let items = tabs.querySelectorAll('.nav-link'); // panel-tabs

    let path = "accounts", // path api
        dataName = 'mdata', // data
        entity = 'user', // form data
        key = 'uid'; // key of entity
    var configuration = {
        transformRequest: angular.indentity,
        headers: {
            'Content-Type': undefined,
            'Authorization': security.getToken,
        }
    };

    const u = {
        uid: -1, username: undefined, email: undefined,
        fullname: undefined, regTime: new Date(), images: [],
        access: 0, roles: [0], platforms: [0],
    };

    $scope.switchTab = function (value) {
        let at = items.length - 1;
        switch (value) {
            case 'detail': $scope.srctab = 'components/manage/_detail.htm'; at = 0; break;
            case 'list': $scope.srctab = 'components/manage/_list.htm'; at = 1; break;
            case 'statistic': default: at = 2;
                $scope.srctab = 'components/manage/_statistic.htm'; break;
        }
        //nav-links => show active css
        items.forEach(item => item.classList.remove('active'));
        items[at].classList.add('active');
        removeElements('[role="tooltip"]'); // remove all tag tooltip
    }

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

    $scope.chart = {
        contruct: { destroy: console.dir },
        relationship: function (mdata) {
            var ctx = chartStatistic?.getContext("2d");
            if (!ctx || !mdata) return;

            var objData = { images: [], roles: [], platforms: [] };
            var options = {
                responsive: true,
                plugins: {
                    title: { display: true, fullSize: true, text: 'Count Relationship Data' }
                },
                scales: {
                    x: { stacked: true },
                    y: { stacked: true }
                }
            };

            // data of datasets
            mdata.forEach(e => {
                objData.roles.push(e.roles?.length || 0)
                objData.images.push(e.images?.length || 0)
                objData.platforms.push(e.platforms?.length || 0)
            });

            var data = {
                labels: mdata.map(e => e['username']),
                datasets: [
                    { label: "images", data: objData.images },
                    { label: "roles", data: objData.roles },
                    { label: "platforms", data: objData.platforms, borderRadius: 10 }
                ]
            }
            $scope.chart.contruct.destroy(); // destroy old chart
            $scope.chart.contruct = new Chart(ctx, { type: "bar", data, options });
        },
        timeRegister: function (mdata, group = 'month') {
            var ctx = chartStatistic?.getContext("2d");
            if (!ctx || !mdata) return;

            // group by date
            mdata = handleDate.groupBy(mdata, handleDate.at[group]);

            // declare opotions and data to create chart
            var options = {
                borderRadius: 10,
                plugins: {
                    title: { display: true, fullSize: true, text: `Account register in ${group}` }
                }
            }, backgroundColor = [], borderColor = [];
            var values = Object.values(mdata).map(e => e.length)
            var data = {
                labels: Object.keys(mdata),
                datasets: [
                    { label: group, data: values, backgroundColor, borderColor, borderWidth: 3 }
                ]
            }

            // random background and border
            util.randomColor({ size: values.length, co: .39, bo: .79 }, backgroundColor, borderColor);

            $scope.chart.contruct.destroy(); // destroy old chart
            $scope.chart.contruct = new Chart(ctx, { type: "bar", data, options });
        },
        sizeOf(data = [], keyLabel, keyArr = 'accounts') {
            var ctx = chartStatistic?.getContext("2d");

            // declare opotions and data to create chart
            var options = { borderRadius: 10 }, backgroundColor = [], borderColor = [];
            var values = data.map(e => e[keyArr]?.length);
            var data = {
                labels: data.map(e => e[keyLabel]),
                datasets: [
                    {
                        label: 'size', data: values,
                        backgroundColor, borderColor, borderWidth: 3
                    }
                ]
            }

            // random background and border
            util.randomColor({ size: values.length, co: .39, bo: .79 }, backgroundColor, borderColor);

            $scope.chart.contruct.destroy(); // destroy old chart
            $scope.chart.contruct = new Chart(ctx, { type: "bar", data, options });
        },
    }


    const _rest = {
        success: (res, execute = 'execute') => {
            $scope.pushMessage({
                htype: 'fw-bolder bg-success text-white',
                heading: `${res?.message || execute} success.`,
                body: `${execute} ${res?.username || 'data'} successfully!`
            }, 3.5e3)
            _rest.resetForm();
        },
        exception: (e, execute = 'execute') => {
            $scope.pushMessage({
                htype: 'bg-danger text-white',
                heading: `${e.config?.method || execute} data failed`,
                body: e.message
            })
            console.error(e);
        },
        resetForm: () => {
            let input = document.querySelector('#formControl input[type="file"]');
            for (let type of ['text', 'file']) input?.setAttribute('type', type);
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
                } else $scope.crud.post(path, dataName, getFormData(), configuration)
                    .then(_rest.success).catch(_rest.exception)
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
            } else $scope.crud.put(path, dataName, getFormData(), index, configuration)
                .then(_rest.success, $scope.access = false).catch(_rest.exception)
        },
        delete: () => $scope.crud
            .delete(path, dataName, $scope[entity][key], key, configuration)
            .then(_rest.success).catch(_rest.exception),
        read: (e) => {
            $scope[entity] = e;
            $scope.access = true;
            $scope.switchTab('detail');
        },
        clear: () => {
            if (confirm(`Do you want to clear form data ${$scope[entity].username || 'user'}?`)) {
                $scope.access = false;
                _rest.success(undefined, 'clear');
            }
        }
    }

    $scope.$watch('srctab', function (src = '') { // load all component
        $scope.$watch('$stateCngeSuccess', setTimeout(() => {
            bsfw.loadPopovers();
            switch (src.substring(src.lastIndexOf('_'))) {
                case '_detail.htm': bsfw.showImageInput(formControl, showInputImages); break;
                case '_statistic.htm': $scope.chart.relationship($scope[dataName]); break;
                default: break;
            }
        }, 250))
    }) // await 500 miliseconds to load popovers

    $scope.$watch('$stateChangeSuccess', async () => {
        $scope[entity] = angular.copy(u);
        $scope.switchTab($routeParams['page']);

        if (security.isLoggedIn()) {
            await $scope.crud.get(path, dataName, undefined, configuration)
                .then(_rest.success).catch(_rest.exception)
        } else $scope.pushMessage({
            heading: 'need to login',
            body: 'This function needs to be logged in and admin',
            htype: 'bg-danger text-warning',
            btype: 'bg-dark-gradient text-danger text-center fs-3',
            ftype: 'bg-danger text-warning',
        }, 5e3);
    });
});