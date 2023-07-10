const app = angular.module('app', ['ngRoute']);
const server = 'testapi' // 'http://localhost:8080/api';

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
   $scope.fil = {
      page: 0,
      size: 10,
   };
   $scope.data = [];
   $scope.customize = local.read('customize') || {
      colortrip: false,
      bgr: {
         on: true,
         link: 'https://static.vecteezy.com/system/resources/thumbnails/002/017/939/original/cosmic-galaxy-with-nebula-free-video.jpg'
      }
   };

   let crud = {
      get: (path, to) => $http
         .get(`${server}/${path}`)
         .then(r => $scope[to] = r.data)
         .catch(e => console.error(e)),
      post: (path, to, data) => $http
         .post(`${server}/${path}`, data)
         .then(r => {
            $scope[to].push(r.data);
            return r.data;
         })
         .catch(e => console.error(e)),
      put: (path, to, data) => $http
         .put(`${server}/${path}`, data)
         .then(r => $scope[to]
            .forEach(e => { // update element in array
               if (e === data) {
                  Object.assign(e, r.data)
                  return e; // return element assigned
               }
            })
         )
         .catch(e => console.error(e)),
      delete: (path, to, id, key = 'id') => $http
         .delete(`${server}/${path}/${id}`)
         .then(r => $scope[to].forEach((e, i) => {
            if (e[key] == id) {
               $scope[to].splice(i, 1);
               return r.data; // number of deleted on server
            }
         }))
         .catch(e => console.error(e))
   }

   $scope.detail = (e) => {
      location = `#!detail/${e.uid}`
   }

   $scope.appendContents = (p, s, o, ...f) => {
      console.log(p, s, o, ...f);
   }

   $scope.setting = () => {
      // custom...

      setting($scope.customize);
   }
   $scope.updateCustom = () => local.write('customize', $scope.customize);

   $scope.$watch('$stateChangeSuccess', async () => {
      let data = await crud.get('accounts.json', 'data');
      $scope.appendContents();
      $scope.setting();

      ((sup) => { // clone data to test
         for (let i = 0; i < sup; i++) {
            let append = [];
            data.forEach(e => {
               let x = { ...e };
               x.uid += data.length
               x.username += data.length
               append.push(x);
            })
            data.push(...append)
         }
      })(5);
   });
});
