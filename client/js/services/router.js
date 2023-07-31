app.config($routeProvider => {
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
    
    $routeProvider
        .when('/', { templateUrl: "pages/main.htm" })
        .when('/author', { templateUrl: "pages/author.htm" })
        .when('/detail/:id', {
            templateUrl: "pages/detail.htm", controller: 'detailcontrol'
        })
        .when('/security', {
            templateUrl: "pages/security.htm", controller: 'securityControl'
        })
        .when('/security/:page', {
            templateUrl: "pages/security.htm", controller: 'securityControl'
        })
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
