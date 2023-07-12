const app = angular.module('app', ['ngRoute']);
const server = 'http://localhost:8080/api';

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

// seting user-inteface
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
      .when('/manage', { templateUrl: "pages/manage.htm", controller: 'usercontrol' })
      .when('/manage/**', { redirectTo: '/manage' })
      .otherwise({ redirectTo: '/' });
});

// usercontrol
app.controller('usercontrol', function ($scope) {
   $scope.srctab = 'components/manage/_myself.htm'
   let items = tabs.querySelectorAll('.nav-link');
   
   // handle nav-tabs event clicked
   items.forEach(item => item.addEventListener('click', _ => {
      items.forEach(e => e.classList?.remove('active')) // remove all active
      item.classList.add('active'); // add new active
   }))
});


// Show detail controller
app.controller('detailcontrol', function ($scope, $routeParams) {

   /**
    * get map from to set data
    * 
    * @param {String} from data
    * @param {Map} map to get element
    * @param {String} to set data
    * @param {Object} e element to set data
    * @param  {...String} deletes any fields
    */
   function set(from, map, to, e, ...deletes) {
      e[to] = [];
      let values = e[from], { length } = values;

      if (deletes?.length) {
         for (let i = 0; i < length; i++) {
            e[to].push({ ...map.get(values[i]) });
            if (e[to][i]) for (let del of deletes)
               delete e[to][i][del]; // delete fields
         }
      } else for (let i = 0; i < length; i++)
         e[to].push({ ...map.get(values[i]) });
   }

   (async () => {
      if (!$scope.ur) location.href = '';

      let id = $routeParams['id'];
      let e = $scope.e = Object.assign({}, $scope.data.find(x => x.uid == id)) || {};
      // relationships
      e.access = $scope.ur.accesses?.get(e.access);
      set('roles', $scope.ur.roles, '_roles', e, 'accounts');
      set('platforms', $scope.ur.platforms, '_platforms', e, 'accounts');
   })();
});

// MAIN APP CONTROLLER
app.controller('control', ($scope, $http) => {
   $scope.dynamic_src = 'js/test.js'
   $scope.defaultImg = 'https://www.photoshopbuzz.com/wp-content/uploads/change-color-part-of-image-psd4.jpg';
   $scope.fil = { page: 0, size: 10 }; // filter contents
   $scope.customize = local.read('customize') || {
      colortrip: false,
      bgr: {
         on: true, // default background image
         link: 'https://static.vecteezy.com/system/resources/thumbnails/002/017/939/original/cosmic-galaxy-with-nebula-free-video.jpg'
      }
   };

   // fetch api
   $scope.crud = {
      /**
       * 
       * @param {String} path to get api
       * @param {String} to is set to variable in scope
       * @returns {Promise<Array<Object>>} data gotten
       */
      get: (path, to) => $http
         .get(`${server}/${path}`)
         .then(r => to ? $scope[to] = r.data : r.data)
         .catch(e => console.error(e)),
      /**
       * 
       * @param {String} path to save api
       * @param {String} to variable to add new data
       * @param {String} data data for save
       * @param {Object} config config post data
       * @returns {Promise<Object>} data inserted
       */
      post: (path, to, data, config) => $http
         .post(`${server}/${path}`, data, config)
         .then(r => {
            $scope[to].push(r.data);
            return r.data;
         })
         .catch(e => console.error(e)),
      /**
       * 
       * @param {string} path to update api
       * @param {String} to variable update data
       * @param {Object} data for update
       * @param {Object} config config put data
       * @returns {Promise<Object>} data updated
       */
      put: (path, to, data, config) => $http
         .put(`${server}/${path}`, data, config)
         .then(r => $scope[to]
            .forEach(e => { // update element in array
               if (e === data) {
                  Object.assign(e, r.data)
                  return e; // return element assigned
               }
            })
         )
         .catch(e => console.error(e)),
      /**
       * 
       * @param {String} path to delete api
       * @param {String} to variable in scope
       * @param {any} id is value of the key
       * @param {String} key default id
       * @returns {Promise<Object>} number of rows deleted
       */
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

   $scope.getImage = (img, ...paths) => img ? `${server}/${paths.join('/')}/${img}` : $scope.defaultImg;

   // show detail content
   $scope.detail = (e) => {
      location = `#!detail/${e.uid}`
   }

   // get page api and append to data
   $scope.appendContents = (p, s, o, ...f) => {
      console.log(p, s, o, ...f);
      // TODO call api, append to data
   }

   // seting display
   $scope.setting = () => {
      // custom...

      setting($scope.customize);
   }

   // saving custom to localStored
   $scope.updateCustom = () => local.write('customize', $scope.customize);

   // fetch api
   $scope.onloadData = async () => {
      let [roles, accesses, platforms] = [
         await $scope.crud.get('roles'),
         await $scope.crud.get('accesses'),
         await $scope.crud.get('platforms'),
         await $scope.crud.get('accounts', 'data')
      ];

      $scope.ur = { // set map data references
         roles: new Map(roles.map(e => [e['urid'], e])),
         accesses: new Map(accesses.map(e => [e['urid'], e])),
         platforms: new Map(platforms.map(e => [e['upid'], e]))
      }
   }

   $scope.$watch('$stateChangeSuccess', async () => {
      // await $scope.onloadData(); // await for load all data
      $scope.setting(); // setting display
   });
});
