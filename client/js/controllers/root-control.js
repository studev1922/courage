const app = angular.module('app', ['ngRoute', 'ngCookies']);
const server = 'http://localhost:8080/api';

// MAIN APP CONTROLLER
app.controller('control', ($scope, $http, $location, security) => {
   var pageFilter = {
      p: 0, s: 10, o: 'ASC', f: 'regTime,uid'
   }

   $scope.defaultImg = 'https://www.photoshopbuzz.com/wp-content/uploads/change-color-part-of-image-psd4.jpg';
   $scope.fil = { page: 0, size: 10 }; // filter contents
   $scope.customize = local.get('customize') || {
      isAlert: true,
      colortrip: false,
      bgr: {
         on: true, // default background image
         link: 'https://static.vecteezy.com/system/resources/thumbnails/002/017/939/original/cosmic-galaxy-with-nebula-free-video.jpg'
      }
   };
   $scope.messages = [];

   $scope.toggleChecks = function (id, arr) {
      let i = arr.findIndex(x => x == id);
      if (i < 0) arr.push(id);
      else arr.splice(i, 1);
   };

   $scope.security = {
      login: async function (user, target) {
         if ($scope.authenticated) return;
         let auth = security.getAuth();
         if (!auth) auth = $scope.authenticated = await security.loginByParams(user)
            .catch(error => {
               console.error(error);
               $scope.pushMessage({
                  htype: 'bg-danger', btype: 'bg-danger', ftype: 'bg-danger',
                  heading: 'Login failed',
                  body: error.message || error.data?.message || 'failed!'
               }, 5e3);
            });
            
         $scope.pushMessage(auth ? `${auth['username']} logged in.` : 'failed to loggin!', 3.5e3);
         Object.assign(user, { username: undefined, password: undefined }); // clear form
         if (target) bsfw.hideModel(target);
         $scope.$apply();
      },
      logout: async function (isAllow, target) {
         if (!$scope.authenticated) return;
         let { username } = $scope.authenticated;
         if (security.logout(isAllow)) {
            $scope.pushMessage(`${username} logout successfully!`, 3.5e3);
            $scope.authenticated = undefined;
            $location.path('/');
         }
         if (target) bsfw.hideModel(target);
      },
      register: async function (user, target, isLogin) {
         let form = document.querySelector(target);
         let data = new FormData(form); // FORM DATA
         $http.post(`${server}/oauth/register`, data,
            { headers: { 'Content-Type': undefined } }
         ).then(async res => {
            form.reset(); // clear form
            Object.assign(user, { time: 0 });
            bsfw.hideModel(target);
            $scope.pushMessage(`${res.data.username} register successfull`, 5e3);
            if (isLogin) {
               let token = res.headers('Authorization');
               $scope.authenticated = security.setToken(token); // login
               $scope.pushMessage(`Welcome ${res.data.username} logged in.`, 5e3);
            }
         }).catch(err => {
            $scope.pushMessage(err.message || err.data?.message, 5e3);
            console.error(err)
         });
      },
      verification: (user) => {
         if (!user) $scope.pushMessage('please input form data!', 3.5e3);
         else if (user.email) $http.get(`${server}/oauth/get-code?email=${user.email}`)
            .then(res => {
               let { data } = res;
               $scope.pushMessage(data.message, 3.5e3);
               user.time = data.time || data.age || 300;

               let interval = setInterval(() => {
                  user.time = --user.time;
                  if (user.time < 1) {
                     user.time = undefined;
                     clearInterval(interval);
                  }
                  $scope.$apply();
               }, 1e3);
            }).catch(err => {
               console.error(err);
               $scope.pushMessage(err.message || err.data?.message, 5e3);
            })
         else $scope.pushMessage("Please input your email to get authentication code!", 5e3);
      },
      clearForm: (target, user) => {
         let form = document.querySelector(target);
         if (confirm('Do you want to reset this form?')) {
            form.reset();
            Object.assign(user, { time: 0 });
            $scope.pushMessage('form clear succesfully', 3.5e3);
         }
      },
      hasRole: security.hasRole,
      isLoggedIn: security.isLoggedIn,
   }

   // fetch api
   $scope.crud = {
      /**
       * 
       * @param {String} path to get api
       * @param {String} to is set to variable in scope
       * @param {String} content get content of response.data
       * @param {Object} config config header
       * @returns {Promise<Array<Object>>} data gotten
       */
      get: (path, to, content, config) => $http
         .get(`${server}/${path}`, config)
         .then(r => {
            let { data } = r;
            if (content) data = eval(`data${content}`);
            return to ? $scope[to] = data : data;
         })
         .catch(e => { throw e }),
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
            $scope[to].unshift(r.data)
            return r.data;
         })
         .catch(e => { throw e }),
      /**
       * 
       * @param {string} path to update api
       * @param {String} to variable update data
       * @param {Object} data for update
       * @param {Number} index of element
       * @param {Object} config config put data
       * @returns {Promise<Object>} data updated
       */
      put: (path, to, data, index, config) => $http
         .put(`${server}/${path}`, data, config)
         .then(r => index != undefined
            ? $scope[to][index] = r.data
            : $scope[to].filter(e => { // copy response data to client data
               if (e === data) return Object.assign(e, r.data);
            })
         )
         .catch(e => { throw e }),
      /**
       * 
       * @param {String} path to delete api
       * @param {String} to variable in scope
       * @param {any} id is value of the key
       * @param {String} key default id
       * @param {Object} config config data
       * @returns {Promise<Object>} number of rows deleted
       */
      delete: (path, to, id, key = 'id', config) => $http
         .delete(`${server}/${path}/${id}`, config)
         .then(r => {
            let data = r.data; // number deleted on server
            $scope[to].forEach((e, i) => {
               if (e[key] === id) $scope[to].splice(i, 1);
            })
            return data;
         })
         .catch(e => { throw e })
   }

   $scope.getImage = (img, ...paths) => img
      ? img.startsWith('http') || img.startsWith('blob')
         ? img : `${server}/${paths.join('/')}/${img}`
      : $scope.defaultImg;

   // show detail content
   $scope.detail = (e) => location = `#!detail/${e.uid}`;

   // get page api and append to data
   $scope.appendContents = async () => {
      let { p, s, o, f } = pageFilter;
      let path = `accounts/page?p=${++p}&s=${s}&o=${o}&f=${f}`

      $scope.crud.get(path)
         .then(r => {
            let { content } = r;
            if (content?.length) {
               $scope['data'].push(...content);
               $scope.pushMessage(`get new ${s} data from ${p}.`, 3e3)
            } else $scope.pushMessage('data has run out', 3e3, --p);
         })
         .catch(console.error)
   }

   // seting display
   $scope.setting = () => {
      // custom...

      setting($scope.customize);
   }

   // saving custom to localStored
   $scope.updateCustom = () => {
      let message = {
         heading: 'update customize layout',
         body: undefined,
         time: new Date()
      };
      try {
         local.put('customize', $scope.customize);
         message.body = `updated successfully.`
      } catch (err) {
         message.body = err.message;
         console.error(err);
      }

      if ($scope.customize.isAlert) {
         $scope.messages.push(message);
      } else alert(message.body);
   };

   // fetch api
   $scope.loadRelationships = async () => {
      let [roles, accesses, platforms] = [
         await $scope.crud.get('roles'),
         await $scope.crud.get('accesses'),
         await $scope.crud.get('platforms'),
      ];
      $scope.ur = { roles, accesses, platforms };
   }

   $scope.removeMes = (_mes, i) => {
      $scope.messages.splice(i, 1);
   }

   $scope.pushMessage = (message, time) => {
      if (!message) return;
      else if (typeof (message) == 'string') {
         message = {
            heading: `${$scope.title || 'Expansive System'} alert`,
            body: message,
            time: new Date()
         }
      } else if (!message.time) message.time = new Date();

      $scope.messages.push(message);
      if (time) setTimeout(() => {
         $scope.messages.splice(0, 1);
         $scope.$apply(); // update the view
      }, time);
   }

   $scope.$watch('$stateChangethen', async () => {
      $scope.setting(); // setting display
      $scope.authenticated = await security.loadToken();
      await $scope.loadRelationships(); // await for load all data
      await $scope.crud.get('accounts/page', 'data', '?.content');
   });
});
