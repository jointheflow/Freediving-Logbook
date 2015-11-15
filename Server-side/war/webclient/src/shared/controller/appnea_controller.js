//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');


appNeaClient.controller('appNeaController', function($scope, $mdSidenav) {
  
  //toggle left side menu  
  $scope.openLeftMenu = function() {
    $mdSidenav('left').toggle();
  };
});