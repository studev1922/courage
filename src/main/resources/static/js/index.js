const app = angular.module('app', ['ngRoute']);

// http://localhost:8080/api/accounts/1001
let element = {
   "uid": 1001,
   "username": "admin",
   "email": "ngoduyhoaname2002@gmail.com",
   "fullname": "Admin System Test",
   "regTime": new Date(),
   "access": 4,
   "images": [
      "admin1.png",
      "admin3.png"
   ],
   "roles": [
      0,
      1,
      2
   ],
   "platforms": [
      0,
      1
   ]
};


let local = {
   /**
    * @param {String} key 
    * @returns {Object} parse as json
    */
   read: (key) => JSON.parse(localStorage.getItem(key)),

   /**
    * @param {String} key 
    * @param {Object} value 
    * @returns {void} undefined
    */
   write: (key, value) => localStorage.setItem(key, JSON.stringify(value)),


   /**
    * @param {String} key 
    * @returns {void} undefined
    */
   delete: (key) => localStorage.removeItem(key)
}

function setting(customize = {}) {
   let { colortrip, bgr } = customize;
   let { body } = document;

   if (colortrip) {
      let e = document.createElement('div');
      e.setAttribute('class', 'bg-animation');
      body.appendChild(e);
   } else {
      body.querySelector('.bg-animation')?.remove();
   }

   if (bgr.on) {
      body.style.backgroundImage = colortrip ? `var(--bgr-linear1), url('${bgr.link}')` : `url('${bgr.link}')`;
   } else {
      body.style.backgroundImage = 'var(--bgr-linear)';
   }
}

app.config(($routeProvider) => {
   $routeProvider
      .when('/', { templateUrl: "pages/main.htm" })
      .when('/detail/:id', { templateUrl: "pages/detail.htm", controller: 'detailcontrol' })
      .otherwise({ redirectTo: '/' });
})

app.controller('detailcontrol', function ($scope, $routeParams) {
   let id = $routeParams['id'];
   let { data } = $scope;
   let index = data.findIndex(e => e.uid == id);

   $scope.e = JSON.stringify(index > -1 ? data[index] : {}, null, 4);
})

app.controller('control', ($scope, $http) => {
   $scope.data = [];
   $scope.customize = local.read('customize') || {
      colortrip: false,
      bgr: {
         on: true,
         link: 'https://static.vecteezy.com/system/resources/thumbnails/002/017/939/original/cosmic-galaxy-with-nebula-free-video.jpg'
      }
   };

   $scope.detail = (e) => {
      location = `#!detail/${e.uid}`
   }

   $scope.appendContents = (size = 10) => {
      for (let i = 0; i < size; i++) {
         $scope.data.push({ ...element })
      }
   }

   $scope.setting = () => {
      // custom...

      setting($scope.customize);
   }
   $scope.updateCustom = () => local.write('customize', $scope.customize);

   $scope.$watch('$stateChangeSuccess', () => {
      $scope.appendContents();
      $scope.setting();
   });
});
