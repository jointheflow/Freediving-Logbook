//testMaterialClient is the main module that represents the app. It needs all other modules dependencies
var testMaterialClient = angular.module('testMaterialClient', ['ngMaterial']);


/*Angular Material theme configuration*/
testMaterialClient.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue')
    .accentPalette('light-blue');
});


testMaterialClient.controller('materialController', function($scope, $mdSidenav) {
  $scope.openSidenav = function() {
    $mdSidenav('left').toggle();
  };
});