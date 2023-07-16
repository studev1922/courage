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