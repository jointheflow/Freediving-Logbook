//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('settingController',  function ($scope,$rootScope, modelService, $log) {
    
    $scope.freediver = modelService.freediverMdl;
    
       
});