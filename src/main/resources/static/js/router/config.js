app.config(($routeProvider) => {
    $routeProvider
        .when('/', { templateUrl: "pages/main.htm" })
        .when('/author', { templateUrl: "pages/author.htm" })
        .when('/detail/:id', { templateUrl: "pages/detail.htm", controller: 'detailcontrol' })
        .when('/manage', { templateUrl: "pages/manage.htm", controller: 'usercontrol' })
        .when('/manage/:page', { templateUrl: "pages/manage.htm", controller: 'usercontrol' })
        .otherwise({ redirectTo: '/' });
});

// arr:Array, values:Array, key:String
app.filter('has', () => util.has);
// arr:Array, column:String, desc:Boolean
app.filter('sort', () => util.sort);
// arr:Array
app.filter('total', () => util.total)
