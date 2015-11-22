//controls the dialog view
appNeaClient.controller('diveSessionDialogController',  
	function ($scope, modelService, freediverService, $mdDialog, $log, $filter) {
        
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
            freediverService.addDiveSession(modelService.freediverMdl.id,
                                            modelService.freediverMdl.externalPlatformId,
                                            modelService.freediverMdl.externalToken,
                                            modelService.freediverMdl.depthUnit,
                                            modelService.freediverMdl.tempUnit,
                                            modelService.freediverMdl.weightUnit,
                                            $filter('date')($scope.divesession.date,'dd-MM-yyyy'),
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
            
            $log.info('dive session saved!');
            //TODO: stop dialog spinner
            
            //TODO: populate the model with the new divesession
            $log.info('model population with:'+data);
            //close the dialog
            $mdDialog.hide();
        };
    
        //dive session save error callback
        $scope.onSaveError = function (dada) {
            //TODO: stop dialog spinner
            
            $log.inf('dive session saved error!');
              
        };
    
});
	