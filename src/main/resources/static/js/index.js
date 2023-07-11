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

   /**
    * 
    * @param {Array} values are keys to find
    * @param {Map} map to get element
    * @param  {...String} deletes 
    */
   function set(values, map, ...deletes) {
      let { length } = values;
      if (deletes?.length) {
         for (let i = 0; i < length; i++) {
            values[i] = map.get(values[i]);
            for (let del of deletes)
               delete values[i][del];
         }
      } else for (let i = 0; i < length; i++)
         values[i] = map.get(values[i]);
   }

   (async () => {
      if (!$scope.ur) console.log(location.href = '');

      let id = $routeParams['id'];
      let e = $scope.e = Object.assign({}, $scope.data.find(x => x.uid == id)) || {};
      // relationships
      e.access = $scope.ur.accesses?.get(e.access);
      set(e.roles, $scope.ur.roles, 'accounts');
      set(e.platforms, $scope.ur.platforms, 'accounts');
   })();
});

app.controller('control', ($scope, $http) => {
   $scope.defaultImg = 'https://www.photoshopbuzz.com/wp-content/uploads/change-color-part-of-image-psd4.jpg';
   $scope.fil = {
      page: 0,
      size: 10,
   };
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
         .then(r => to ? $scope[to] = r.data : r.data)
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


   $scope.onloadData = async () => {
      let [roles, accesses, platforms] = [
         await crud.get('roles.json'),
         await crud.get('accesses.json'),
         await crud.get('platforms.json'),
         await crud.get('accounts.json', 'data')
      ];

      $scope.ur = {
         roles: new Map(roles.map(e => [e['urid'], e])),
         accesses: new Map(accesses.map(e => [e['urid'], e])),
         platforms: new Map(platforms.map(e => [e['upid'], e]))
      }
   }

   $scope.$watch('$stateChangeSuccess', async () => {
      await $scope.onloadData();
      $scope.setting();

      let data = $scope.data;

      ((sup) => { // clone data for test
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
