//controls the dialog view
appNeaClient.controller('diveSessionDetailController',  
	function ($scope, modelService, freediverService, $log, $filter, $location) {
        //index of the current tab
        $scope.selectedIndex=freedivingLogbookConstant.TAB_DETAIL;
        
        //icon for the current fab button
        $scope.actionIcon= freedivingLogbookConstant.ICON_SAVE_DIVESESSION;
    
        //set the current divesession with that selected in the model
        $scope.divesession = modelService.freediverMdl.currentDiveSession;       
        
        $scope.back = function() {
            $location.path('/divesessionlist');
        };
    
        /*watch the current tab index and change icon and selected index according*/
        $scope.$watch('selectedIndex', function(current, old) {
            $log.info('selected index:'+current);   
            $scope.selectedIndex = current;
            
            if (current==freedivingLogbookConstant.TAB_DETAIL)
                $scope.actionIcon=freedivingLogbookConstant.ICON_SAVE_DIVESESSION;
            if (current==freedivingLogbookConstant.TAB_DIVES) 
                $scope.actionIcon=freedivingLogbookConstant.ICOND_ADD_DIVE;
        });
    
        /*Dependig on active tab invoke saving detail dive session or add new dive action*/
        $scope.tabAction = function () {
            if ($scope.selectedIndex == freedivingLogbookConstant.TAB_DETAIL) {
                $scope.saveDivesession();
                                          
            }
            if ($scope.selectedIndex == freedivingLogbookConstant.TAB_DIVES) {
                alert('add dive');
            
            }
        
        
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
        
    
    
        /*remove dive session*/
        $scope.removeDivesession = function() {
            freediverService.removeDiveSession(modelService.freediverMdl.externalPlatformId,
                                               modelService.freediverMdl.externalToken,
                                               $scope.divesession.id, 
                                               $scope.onDivesessionRemoveSuccess,
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
            //update the scope!
            $scope.divesession = modelService.freediverMdl.currentDiveSession; 
           
            
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
        $scope.onDivesessionRemoveSuccess = function (data, divesessionId) {
            $log.info('dive session removed');
            //update the current model
            modelService.freediverMdl.removeDiveSession(divesessionId);
            
            //route to dive list page
            $scope.back();
        };
        
        
    
});
	