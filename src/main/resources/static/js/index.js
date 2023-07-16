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
      .when('/author', { templateUrl: "pages/author.htm" })
      .when('/detail/:id', { templateUrl: "pages/detail.htm", controller: 'detailcontrol' })
      .when('/manage', { templateUrl: "pages/manage.htm", controller: 'usercontrol' })
      .when('/manage/:page', { templateUrl: "pages/manage.htm", controller: 'usercontrol' })
      .otherwise({ redirectTo: '/' });
});

// usercontrol
app.controller('usercontrol', function ($scope, $routeParams) {
   (() => {
      // handle nav-tabs event clicked
      let items = tabs.querySelectorAll('.nav-link');
      items.forEach(item => item.addEventListener('click', _ => {
         items.forEach(e => e.classList?.remove('active')) // remove all active
         item.classList.add('active'); // add new active
      }));

      $scope.user = {
         access: 0,
         roles: [0, 2],
         platforms: [0],
         regTime: new Date()
      }

      $scope.detailControl = (e) => {
         $scope.user = e;
         items[0].click();
      }

      function switchPage(value) {
         switch (value) {
            case 'one':
               $scope.srctab = 'components/manage/_one.htm'
               break;
            case 'list': default:
               $scope.srctab = 'components/manage/_list.htm'
               break;
         }
      }

      switchPage($routeParams['page']);
   })()

   $scope.$watch('$stateChangeSuccess', async () => {
      if (!$scope.ur) await $scope.loadRelationships(); // await for load all data
      await $scope.crud.get('accounts', 'mdata'); // load all data
      bsfw.loadPopover(); // load all popover check box references
   });
});


// Define a custom directive to handle multiple file upload
app.directive('ngFiles', function ($parse) {
   return {
      link: function (scope, element, attrs) {
         // Get the expression from the ng-files attribute value

         var expression = $parse(attrs.ngFiles);

         // Add a change event listener to the element
         element.on('change', function (event) {
            // Get the files object from the event target
            var files = event.target.files;

            // Invoke the expression with the files object as an argument
            expression(scope, { $files: files });

            // Apply the scope changes
            scope.$apply();
         });
      }
   };
});

app.filter('has', () => (arr, values, key) => arr?.filter(x => values?.includes(x[key])));

// Show detail controller
app.controller('detailcontrol', function ($scope, $routeParams) {

   /**
    * get map from to set data
    * 
    * @param {String} from data is references id (key)
    * @param {Array} array to get element
    * @param {String} to set data
    * @param {Object} e element to set data
    * @param {String} by key of element
    * @param  {...String} deletes any fields
    */
   function set(from, array, to, e, by, ...deletes) {
      // delete fields
      let move_fileds = (x) => {
         if (x) for (let del of deletes) delete x[del];
      }

      // set single value
      if (!Array.isArray(e[from])) {
         if (deletes?.length) {
            e[to] = array.find(x => x[by] == e[from]);
            move_fileds(e[to]);
         } else e[to] = array.find(x => x[by] == e[from]);
         return;
      }

      e[to] = []; // set multiple values
      let values = e[from], { length } = values;

      if (deletes?.length) {
         for (let i = 0; i < length; i++) {
            e[to].push({ ...array.find(x => x[by] == values[i]) });
            move_fileds(e[to][i]);
         }
      } else for (let i = 0; i < length; i++)
         e[to].push({ ...array.find(x => x[by] == values[i]) });
   }

   $scope.$watch('$stateChangeSuccess', async () => {
      if (!$scope.ur) {
         await $scope.loadRelationships(); // await for load all data
         await $scope.crud.get('accounts/page', 'data', '?.content');
      }

      let id = $routeParams['id'];
      let e = $scope.e = angular.copy($scope.data.find(x => x.uid == id)) || {};

      // relationships
      set('access', $scope.ur.accesses, '_access', e, 'uaid', 'accounts');
      set('roles', $scope.ur.roles, '_roles', e, 'urid', 'accounts');
      set('platforms', $scope.ur.platforms, '_platforms', e, 'upid', 'accounts');
   });
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

   $scope.toggleChecks = function (id, arr) {
      let i = arr.findIndex(x => x == id);
      if (i < 0) arr.push(id);
      else arr.splice(i, 1);
   };

   // fetch api
   $scope.crud = {
      /**
       * 
       * @param {String} path to get api
       * @param {String} to is set to variable in scope
       * @param {String} content get content of response.data
       * @returns {Promise<Array<Object>>} data gotten
       */
      get: (path, to, content) => $http
         .get(`${server}/${path}`)
         .then(r => {
            let { data } = r;
            if (content) data = eval(`data${content}`);
            return to ? $scope[to] = data : data;
         })
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

   $scope.getImage = (img, ...paths) => img
      ? img.startsWith('http') || img.startsWith('blob')
         ? img : `${server}/${paths.join('/')}/${img}`
      : $scope.defaultImg;

   // show detail content
   $scope.detail = (e) => location = `#!detail/${e.uid}`;

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
   $scope.loadRelationships = async () => {
      let [roles, accesses, platforms] = [
         await $scope.crud.get('roles'),
         await $scope.crud.get('accesses'),
         await $scope.crud.get('platforms'),
      ];
      $scope.ur = { roles, accesses, platforms };
   }

   $scope.$watch('$stateChangeSuccess', async () => {
      $scope.setting(); // setting display
      await $scope.loadRelationships(); // await for load all data
      await $scope.crud.get('accounts/page', 'data', '?.content');
   });
});
