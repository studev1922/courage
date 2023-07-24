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
