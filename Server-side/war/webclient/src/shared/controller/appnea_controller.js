//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');


appNeaClient.controller('appNeaController', function($scope, $mdSidenav) {
  
  $scope.applicationName = freedivingLogbookConstant.applicationName;
    
  //toggle left side menu  
  $scope.openLeftMenu = function() {
    $mdSidenav('left').toggle();
  };
});