// usercontrol
app.controller('usercontrol', function ($scope, $routeParams) {
    let path = "accounts", dataName = 'mdata', key = 'uid';

    const u = {
        uid: -1, username: undefined, email: undefined,
        fullname: undefined, regTime: new Date(), images: [],
        access: 0, roles: [0], platforms: [0]
    };

    (() => {
        // handle nav-tabs event clicked
        let items = tabs.querySelectorAll('.nav-link');
        items.forEach(item => item.addEventListener('click', _ => {
            items.forEach(e => e.classList?.remove('active')) // remove all active
            item.classList.add('active'); // show new active
            removeElements('[role="tooltip"]');
        }));

        $scope.user = angular.copy(u);

        $scope.detailControl = (e) => {
            $scope.user = e;
            items[0].click();
        }

        switch ($routeParams['page']) {
            default:
            case 'one':
                $scope.srctab = 'components/manage/_one.htm'
                break;
            case 'list':
                $scope.srctab = 'components/manage/_list.htm'
                break;
        }
    })();

    function getData() {
        let obj = $scope.user;
        let input = formControl.querySelector('input[type="file"]');
        delete obj.regTime;
        return util.getFormData(obj, input?.files);
    }

    $scope.control = {
        insert: () => $scope.crud
            .post(path, dataName, getData())
            .then(
                r => $scope.pushMessage({
                    heading: 'insert success',
                    body: `insert ${r.username} successfully`
                }, 3000)
            ).catch(
                e => $scope.pushMessage({
                    htype: 'text-danger',
                    heading: 'insert data failed',
                    body: e.message
                }, 3000)
            ),
        update: () => $scope.crud
            .put(path, dataName, getData())
            .then(r => {
                $scope.pushMessage({
                    heading: 'update success',
                    body: `update ${r.username} successfully`
                }, 3000);
            }).catch(
                e => $scope.pushMessage({
                    htype: 'text-danger',
                    heading: 'update failed',
                    body: e.message
                }, 3000)
            ),
        delete: () => $scope.crud
            .delete(path, dataName, $scope.user[key], key)
            .then(
                _ => $scope.pushMessage({
                    heading: 'delete success',
                    body: `delete successfully`
                }, 3000)
            ).catch(
                e => $scope.pushMessage({
                    htype: 'text-danger',
                    heading: 'delete failed',
                    body: e.message
                }, 3000)
            ),
        clear: () => {
            if (confirm(`clear form data ${$scope.user.username || 'user'}`)) {
                formControl.reset() // reset html form
                let input = formControl.querySelector('input[type="file"]');
                for (let type of ['text', 'file']) input.setAttribute('type', type);
                $scope.user = angular.copy(u); // reset binding
                $scope.pushMessage({
                    heading: 'Clear form data',
                    body: `clear form data finshed!`
                }, 3000)
            }
        }
    }

    $scope.$watch('srctab', function () { // load all component
        $scope.$watch('$stateCngeSuccess', setTimeout(() => {
            bsfw.loadPopovers();
            if (document.querySelector('#formControl')) {
                let input = formControl.querySelector('input[type="file"]');
                let flex = showInputImages.querySelector('.d-flex');

                input.addEventListener('change', _ => {
                    let childs = [], src;

                    // remove all old images
                    while (flex.lastChild) flex.removeChild(flex.firstChild);
                    // add new images
                    if (input.files?.length)
                        for (let f of input.files) {
                            src = URL.createObjectURL(f);
                            childs.push(`<div class="custom-img"><span>size: ${f.size}</span><img src="${src}"alt="${f.name}"><h5>${f.name}</h5></div>`);
                        }
                    else childs.push(`<h4 style="background: var(--bgr-linear1);">No photos selected!</h4>`);
                    flex.innerHTML = childs.join('');
                });
            }
        }, 500))
    }) // await 500 miliseconds to load popovers

    $scope.$watch('$stateChangeSuccess', async () => {
        if (!$scope.ur) await $scope.loadRelationships(); // await for load all data
        await $scope.crud.get(path, dataName); // load all data
    });
});
