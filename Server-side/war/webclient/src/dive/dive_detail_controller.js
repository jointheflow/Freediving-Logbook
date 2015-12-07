//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

//controls the dive detail view
appNeaClient.controller ('diveDetailController',  
	function ($scope, modelService, freediverService, $log, $filter, $location, $route) {
    
    //set the current dive
    $scope.dive = modelService.freediverMdl.currentDiveSession.currentDive;
    
    //go to back
    $scope.back = function() {
            $location.path('/divesessiondetail');
    };
    
    
    
    //save the dive
    $scope.saveDive = function() {
        $log.info('saving dive.....');

        //activate dialog spinner
        //$scope.spinner = true;
        //basing on current dive  id we know if it is a new dive (add) or update an existed dive (update)
        if ($scope.dive.id == null) {
            
            //invoke Asynch add dive rest service passing callback function
            freediverService.addDive(modelService.freediverMdl.currentDiveSession.id,
                                    modelService.freediverMdl.externalPlatformId,
                                    modelService.freediverMdl.externalToken,
                                    modelService.freediverMdl.depthUnit,
                                    modelService.freediverMdl.tempUnit,
                                    modelService.freediverMdl.weightUnit,
                                    $scope.dive.diveTime,
                                    $scope.dive.duration,
                                    $scope.dive.equipment,
                                    $scope.dive.weight,
                                    $scope.dive.depthTemp,
                                    $scope.dive.maxDepth,
                                    $scope.dive.neutralBuoyance,
                                    $scope.dive.note,
                                    $scope.dive.diveType,
                                    $scope.onSaveSuccess,
                                    $scope.onSaveError);               
        }else {

             //invoke Asynch update dive rest service passing callback function
            freediverService.updateDive(modelService.freediverMdl.currentDiveSession.currentDive.id,
                                        modelService.freediverMdl.externalPlatformId,
                                        modelService.freediverMdl.externalToken,
                                        modelService.freediverMdl.depthUnit,
                                        modelService.freediverMdl.tempUnit,
                                        modelService.freediverMdl.weightUnit,
                                        $scope.dive.diveTime,
                                        $scope.dive.duration,
                                        $scope.dive.equipment,
                                        $scope.dive.weight,
                                        $scope.dive.depthTemp,
                                        $scope.dive.maxDepth,
                                        $scope.dive.neutralBuoyance,
                                        $scope.dive.note,
                                        $scope.dive.diveType,
                                        $scope.onSaveSuccess,
                                        $scope.onSaveError);               
        }

    };



    /*remove dive*/
    $scope.removeDivesession = function() {
        freediverService.removeDive(modelService.freediverMdl.externalPlatformId,
                                    modelService.freediverMdl.externalToken,
                                    $scope.dive.id, 
                                    $scope.onDiveRemoveSuccess,
                                    $scope.onSaveError);

    };

    //dive session save success callback
    $scope.onSaveSuccess = function (data) {

        $log.info('dive saved!');
        //stop dialog spinner
        //$scope.spinner = false;

        //TODO: populate the model with the new divesession
        $log.info('model population with:'+data);
        modelService.freediverMdl.currentDiveSession.currentDive = modelService.addOrUpdateDiveFromData(data.detail,
                                                                                                        modelService.freediverMdl.depthUnit,
                                                                                                        modelService.freediverMdl.tempUnit,
                                                                                                        modelService.freediverMdl.weightUnit);
                                        
        
        //update the scope!
        $scope.dive = modelService.freediverMdl.currentDiveSession.currentDive; 
        //update the model
        modelService.freediverMdl.currentDiveSession.addOrUpdateDive($scope.dive);
        //refresh location
        $route.reload();

    };

    //dive session save error callback
    $scope.onSaveError = function (data) {
        //stop dialog spinner
        $scope.spinner = false;
        //TODO: manage error
        alert(data);
        $log.info('dive session saved error!');

    };

    //manage removing success
    $scope.onDiveRemoveSuccess = function (data, divesessionId) {
        $log.info('dive removed');
        //update the current model
        modelService.freediverMdl.removeDiveSession(diveiId);

        //route to dive session list page
        $scope.back();
    };

        
    
        

});