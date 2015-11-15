//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

appNeaClient.controller('MyController', function($scope, $mdSidenav) {
  $scope.openLeftMenu = function() {
    $mdSidenav('right').toggle();
  };
});