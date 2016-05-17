//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('settingController',  function ($scope,$rootScope, modelService, $log) {
    
    $scope.freediver = modelService.freediverMdl;
    //$scope.customFieldListOfDiveSession = ["custom_one", "custom_two", "custom_three"];
    $scope.customFieldListOfDiveSession = [
      { name: 'custom_one'},
      { name: 'custom_two'},
      { name: 'custom_three'}
    ];
    
    
    $scope.removeCustom = function() {
        if ($scope.custom != null) {
            $log.info("Removing item index "+$scope.custom.index);
            $scope.customFieldListOfDiveSession.splice($scope.custom.index, 1);
        }
     // $scope.customFieldListOfDiveSession.pop();
    };
    
    //go to back
    $scope.back = function() {
            window.history.back();
            //$location.path('/divesessiondetail');
    };
    
       
});