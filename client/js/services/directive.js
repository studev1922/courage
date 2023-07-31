app.directive('accessColor', function () {
    return {
        restrict: 'A',
        scope: { access: '=' },
        link: function (scope, element, attrs) {
            let color = '#fff';
            switch (scope.access) {
                case 0: color = '#b40000aa'; break;
                case 1: color = '#b49000aa'; break;
                case 2: color = '#b4b400aa'; break;
                case 3: color = '#12b400aa'; break;
                default: color = '#009fb4aa'; break;
            }
            element.css('background-color', color);
        }
    }
})

app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter, { 'event': event });
                });
                event.preventDefault();
            }
        });
    };
});