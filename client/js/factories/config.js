var resolve = function (...roles) {
    return {
        checkAdmin: function ($q, $location, security) {
            var deferred = $q.defer();
            if (security.hasRole(...roles)) {
                deferred.resolve();
            } else {
                deferred.reject();
                $location.path('/');
            }
            return deferred.promise;
        }
    };
}


app.config($routeProvider => {
    $routeProvider
        .when('/', { templateUrl: "pages/main.htm" })
        .when('/author', { templateUrl: "pages/author.htm" })
        .when('/detail/:id', { templateUrl: "pages/detail.htm", controller: 'detailcontrol' })
        .when('/manage', {
            templateUrl: "pages/manage.htm", controller: 'usercontrol',
            resolve: resolve('ADMIN')
        })
        .when('/manage/:page', {
            templateUrl: "pages/manage.htm", controller: 'usercontrol',
            resolve: resolve('ADMIN')
        })
        .otherwise({ redirectTo: '/' });
});

// arr:Array, values:Array, key:String
app.filter('has', () => util.has);
// arr:Array, column:String, desc:Boolean
app.filter('sort', () => util.sort);
// arr:Array
app.filter('total', () => util.total)

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