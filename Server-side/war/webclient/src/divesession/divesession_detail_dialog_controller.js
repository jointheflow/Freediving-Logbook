//controls the dialog view
appNeaClient.controller('diveSessionDialogController',  
	function ($scope, modelService, freediverService, $mdDialog, $log, $filter) {
        //manage scope spinner showing/hinding
        $scope.spinner = false;
    
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
            
            //activate dialog spinner
            $scope.spinner = true;
            
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
            //stop dialog spinner
            $scope.spinner = false;
            
            //TODO: populate the model with the new divesession
            $log.info('model population with:'+data);
            diveSession = new DiveSessionMdl();
            //diveSession.id = null;
            //TODO add service must return an id!!!!
            diveSession.diveDate =  $filter('date')($scope.divesession.date,'dd-MM-yyyy');
            diveSession.location = $scope.divesession.location;
            diveSession.depth = $scope.divesession.depth;
            diveSession.meteo = $scope.divesession.meteo;
            diveSession.equipment =  $scope.divesession.equipment;
            diveSession.weight = $scope.divesession.weight;
            //TODO
            diveSession.note = null;
            
            modelService.freediverMdl.diveSessions.push(diveSession);
            //close the dialog
            $mdDialog.hide();
        };
    
        //dive session save error callback
        $scope.onSaveError = function (data) {
            //stop dialog spinner
            $scope.spinner = false;
            //TODO: manage error
            alert(data);
            $log.inf('dive session saved error!');
              
        };
    
});
	