//controls the dialog view
appNeaClient.controller('diveSessionDetailController',  
	function ($scope, modelService, freediverService, $log, $filter, $location) {
        
               
        
        $scope.back = function () {
            $location.path('/divesessionlist');
        };
    
        //save the dive session
        $scope.save = function() {
            $log.info('saving dive session.....');
            
            //activate dialog spinner
            //$scope.spinner = true;
            
            //invoke Asynch add dive session rest service passing callback function
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
                                            $scope.divesession.note,
                                            $scope.onSaveSuccess,
                                            $scope.onSaveError);                
            
        };
    
    
        //dive session save success callback
        $scope.onSaveSuccess = function (data) {
            
            $log.info('dive session saved!');
            //stop dialog spinner
            //$scope.spinner = false;
            
            //TODO: populate the model with the new divesession
            $log.info('model population with:'+data);
            modelService.addOrUpdateDiveSessionFromData(data.detail);
            /*diveSession = new DiveSessionMdl();
            diveSession.id = data.detail.id;
            diveSession.diveDate = data.detail.diveDate;
            diveSession.location = data.detail.locationDesc;
            diveSession.depth = $scope.divesession.depth;
            
            diveSession.meteo = $scope.divesession.meteo;
            diveSession.equipment =  $scope.divesession.equipment;
            diveSession.weight = $scope.divesession.weight;
            //TODO
            diveSession.note = null;
            
            modelService.freediverMdl.diveSessions.push(diveSession);
            */
            
        };
    
        //dive session save error callback
        $scope.onSaveError = function (data) {
            //stop dialog spinner
            $scope.spinner = false;
            //TODO: manage error
            alert(data);
            $log.info('dive session saved error!');
              
        };
    
});
	