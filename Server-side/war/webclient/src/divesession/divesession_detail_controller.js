//controls the dialog view
appNeaClient.controller('diveSessionDetailController',  
	function ($scope, modelService, freediverService, $log, $filter, $location) {
        
        $scope.selectedIndex=0;
        //set the current divesession with that selected in the model
        $scope.divesession = modelService.freediverMdl.currentDiveSession;       
        
        $scope.back = function() {
            $location.path('/divesessionlist');
        };
    
        
        $scope.$watch('selectedIndex', function(current, old) {
            $log.info('selected index:'+current);   
            $scope.selectedIndex = current;
        });
    
        /*Dependig on active tab invoke saving detail dive session or add new dive action*/
        $scope.tabAction = function () {
            if ($scope.selectedIndex ==0) alert('save');
            if ($scope.selectedIndex ==1) alert('add dive')
        
        
        };
    
        /*call add new dive menu*/
        $scope.callAddNewDive = function() {
        };
        
        
        //save the dive session
        $scope.saveDivesession = function() {
            $log.info('saving dive session.....');
            
            //activate dialog spinner
            //$scope.spinner = true;
            //basing on current dive session id we know if it is a new dive session (add) or update an existent dive session (update)
            if ($scope.divesession.id == null) {
            
                //invoke Asynch add dive session rest service passing callback function
                freediverService.addDiveSession(modelService.freediverMdl.id,
                                                modelService.freediverMdl.externalPlatformId,
                                                modelService.freediverMdl.externalToken,
                                                modelService.freediverMdl.depthUnit,
                                                modelService.freediverMdl.tempUnit,
                                                modelService.freediverMdl.weightUnit,
                                                $filter('date')($scope.divesession.diveDate,'dd-MM-yyyy'),
                                                $scope.divesession.location,
                                                $scope.divesession.meteo,
                                                $scope.divesession.equipment,
                                                $scope.divesession.weight,
                                                $scope.divesession.temp,
                                                $scope.divesession.depth,
                                                $scope.divesession.note,
                                                $scope.onSaveSuccess,
                                                $scope.onSaveError);                
            }else {
            
                 //invoke Asynch update dive session rest service passing callback function
                freediverService.updateDiveSession(modelService.freediverMdl.externalPlatformId,
                                                modelService.freediverMdl.externalToken,
                                                modelService.freediverMdl.depthUnit,
                                                modelService.freediverMdl.tempUnit,
                                                modelService.freediverMdl.weightUnit,
                                                $scope.divesession.id,
                                                $filter('date')($scope.divesession.diveDate,'dd-MM-yyyy'),
                                                $scope.divesession.location,
                                                $scope.divesession.meteo,
                                                $scope.divesession.equipment,
                                                $scope.divesession.weight,
                                                $scope.divesession.temp,
                                                $scope.divesession.depth,
                                                $scope.divesession.note,
                                                $scope.onSaveSuccess,
                                                $scope.onSaveError);    
            }

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
	