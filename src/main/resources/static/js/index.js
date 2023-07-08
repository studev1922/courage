const app = angular.module('app', []);
let element = // 20230708173430
// http://localhost:8080/api/accounts/1001

{
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
}

app.controller('control', ($scope, $http) => {
   $scope.data = [];

   $scope.appendContents = (size = 4) => {
      for (let i = 0; i < size; i++) {
         $scope.data.push({ ...element })
      }
   }

   $scope.$watch('$stateChangeSuccess', () => {
      $scope.appendContents();
   });
});

