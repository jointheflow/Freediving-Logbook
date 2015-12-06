//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

//controls the dive detail view
appNeaClient.controller ('diveDetailController',  
	function ($scope, modelService, freediverService, $log, $filter, $location) {
    
    //set the current dive
    $scope.dive = modelService.freediverMdl.currentDiveSession.currentDive;
    
    //go to back
    $scope.back = function() {
            $location.path('/divesessiondetail');
    };
    
    
        

});