//controls the dialog view
appNeaClient.controller('diveSessionDialogController',  
	function ($scope, modelService, freediverService, $mdDialog, $log) {
        
        //hide the dialog
        $scope.hide = function() {
            $mdDialog.hide();
        };
  
        //cancel the dialog
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
        
        //save the dive session
        $scope.save = function() {
            $log.info('saving dive session.....');
            
            //TODO: activate dialog spinner
            
            //TODO:invoke Asynch add dive session rest service passing callback function
            freediverService.addDiveSession(modelService.freediver.externalId,
                                            modelService.freediver.externalPlatformId$scope.externalPlatformId,
                                            modelService.freediver.externalToken,
                                            modelService.freediver.depthUnit,
                                            modelService.freediver.tempUnit,
                                            modelService.freediver.weightUnit,
                                            $scope.divesession.date,
                                            $scope.divesession.location,
                                            $scope.divesession.meteo,
                                            $scope.divesession.equipment,
                                            $scope.divesession.weight,
                                            $scope.divesession.temp,
                                            $scope.divesession.depth,
                                            $scope.onSaveSuccess,
                                            $scope.onSaveError);                
            
        };
    
    
        //dive session save success callback
        $scope.onSaveSuccess = function (data) {
            
            $log.inf('dive session saved!');
            //TODO: stop dialog spinner
            
            //TODO: populate the model with the new divesession
            
            //close the dialog
            $mdDialog.hide();
        };
    
        //dive session save error callback
        $scope.onSaveError = function (dada) {
            //TODO: stop dialog spinner
            
            $log.inf('dive session saved error!');
              
        };
    
});
	