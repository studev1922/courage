// usercontrol
app.controller('usercontrol', function ($scope, $routeParams) {
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

    $scope.control = {
        insert: () => {
            let data = new FormData(formControl);

        },
        update: () => {
            let data = new FormData(formControl);

        },
        delete: () => {

        },
        clear: () => {
            if (confirm(`clear form data ${$scope.user.username || 'user'}`)) {
                formControl.reset() // reset html form
                let input = formControl.querySelector('input[type="file"]');
                for (let type of ['text', 'file']) input.setAttribute('type', type);
                $scope.user = angular.copy(u); // reset binding
            }
        }
    }

    $scope.$watch('srctab', function () { // load all component
        $scope.$watch('$stateCngeSuccess', setTimeout(bsfw.loadPopovers, 500))
    }) // await 500 miliseconds to load popovers

    $scope.$watch('$stateChangeSuccess', async () => {
        if (!$scope.ur) await $scope.loadRelationships(); // await for load all data
        await $scope.crud.get('accounts', 'mdata'); // load all data

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
    });
});
