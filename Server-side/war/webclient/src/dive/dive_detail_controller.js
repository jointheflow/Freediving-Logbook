//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

//controls the dive detail view
appNeaClient.controller ('diveDetailController',  
	function ($rootScope, $scope, modelService, freediverService, $log, $filter, $location, $route, $mdDialog, $mdToast) {
    
    $scope.viewStatus = modelService.freediverMdl.viewstatus;
    
    //regarding the status of the view set the defaut attribute to show
    switch(modelService.freediverMdl.viewstatus) {
        case freedivingLogbookConstant.VIEW_NEW:
            //null
            
        break;
        case freedivingLogbookConstant.VIEW_UPDATE:
            //set the default date for time picker. The default date is the date caming from dive session plus
            //the dive time of the selected dive.The default
            //date must be inn the following format AAAA-MM-DD HH:mm
            $scope.diveDate = modelService.freediverMdl.currentDiveSession.diveDate.getFullYear()+'-'+
                             (modelService.freediverMdl.currentDiveSession.diveDate.getMonth()+1)+'-'+
                              modelService.freediverMdl.currentDiveSession.diveDate.getDate()+ ' '+
                              modelService.freediverMdl.currentDiveSession.currentDive.getTimeHHMM();
            
            //bind the scope dive to the current dive in the model
            $scope.dive = modelService.freediverMdl.currentDiveSession.currentDive;
            //show the dive time of the selected dive            
            $scope.diveTime = modelService.freediverMdl.currentDiveSession.currentDive.getTimeHHMM();
            $scope.dive.timeHour = (Math.floor(modelService.freediverMdl.currentDiveSession.currentDive.diveTime / 60)).toString().paddingLeft('00');
            
            $scope.dive.timeMinute = (modelService.freediverMdl.currentDiveSession.currentDive.diveTime % 60).toString().paddingLeft('00');
            //bind the scope to the minute and second from duration expressed in seconds
            $scope.dive.minute = Math.floor(modelService.freediverMdl.currentDiveSession.currentDive.duration / 60);
            $scope.dive.second = (modelService.freediverMdl.currentDiveSession.currentDive.duration % 60);
            
            
                
            
        break;
        
    }
        
    
    //go to back
    $scope.back = function() {
            modelService.freediverMdl.diveSessionActiveTabIndex = freedivingLogbookConstant.TAB_DIVES;
            window.history.back();
            //$location.path('/divesessiondetail');
    };
    
    $scope.diveTypes = [ 'Constant Weight (CWT)', 'Constant Weight Without Fins (CNF)', 'Free Immersion (FIM)', 'Dynamic With Fins (DYN)', 'Dynamic Without Fins (DNF)', 'No Limit (NLT)', 'Variable Weight (VWT)', 'other'];
    
    //hour array
    $scope.h24 = ['00','01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    //minute (or second) array
    $scope.m60 = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59'];
    
        
    /*watch the current tab index and change icon and selected index according*/
        $scope.$watch('ctrl.timepicker', function(current, old) {
            $log.info('ctrl.timepicker:'+current);   
            
        });
    
    
    
    
    //save the dive
    $scope.saveDive = function() {
        $log.info('saving dive.....');

        //activate dialog spinner
        $rootScope.showWaitingSpinner();
        
        //convert dive time in abosulte minute in a day HH*60 + mm

        var selectedMoment = moment($scope.diveTime, 'HH:mm');
        //check if $scope.dive if defined. If not create, this prevent exception if $scope.dive is not defined at all. This overwrite all field of $scope.dive
        if ($scope.dive==null)  {
            $scope.dive =  new Object();
        }
            var selectedMoment = moment($scope.dive.timeHour+':'+$scope.dive.timeMinute, 'HH:mm');
            //check if $scope.dive if defined. If not create. This overwrite all field of $scope.dive
            if (!angular.isDefined($scope.dive))   new Object();

            $scope.dive.diveTime = (selectedMoment.hours() * 60) + (selectedMoment.minutes());

        //convert minute and second of duration in duration expressed in seconds only 
            var convMinute = ($scope.dive.minute == null)?0:$scope.dive.minute;
            var convSecond = ($scope.dive.second == null)?0:$scope.dive.second;
            $scope.dive.duration = ((convMinute * 60) + convSecond);

        
        //basing on current view status we know if it is a new dive (add) or update an existed dive (update)
        //regarding the status of the view set the defaut attribute to show
        switch(modelService.freediverMdl.viewstatus) {
            case freedivingLogbookConstant.VIEW_NEW:
                //TODO --remove the test stub
                var custoFieldDiveMap = new Map();
                custoFieldDiveMap.set('custom_1', 'zero');
                custoFieldDiveMap.set('custom_2', 'due');
                //test stub
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
                                        custoFieldDiveMap,
                                        $scope.onSaveSuccess,
                                        $scope.onSaveError);   
                

            break;
            case freedivingLogbookConstant.VIEW_UPDATE:
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
            break;
        };

    };

    /*remove dive*/
    $scope.removeDive = function() {
        //activare wait spinner
        $rootScope.showWaitingSpinner();
        
        freediverService.removeDive($scope.dive.id, 
                                    modelService.freediverMdl.externalPlatformId,
                                    modelService.freediverMdl.externalToken,
                                    $scope.onDiveRemoveSuccess,
                                    $scope.onSaveError);

    };

    //dive session save success callback
    $scope.onSaveSuccess = function (data) {

        $log.info('dive saved!');
        //stop dialog spinner
        $rootScope.closeWaitingSpinner();

        //populate the model with the new divesession
        $log.info('model population with:'+data);
        modelService.freediverMdl.currentDiveSession.currentDive = modelService.addOrUpdateDiveFromData(data.detail,
                                                                                                        modelService.freediverMdl.depthUnit,
                                                                                                        modelService.freediverMdl.tempUnit,
                                                                                                        modelService.freediverMdl.weightUnit);
                                        
        //update the status of the view
        modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;  
        
        //update the scope!
        $scope.dive = modelService.freediverMdl.currentDiveSession.currentDive; 
        //update the model
        modelService.freediverMdl.currentDiveSession.addOrUpdateDive($scope.dive);
        
        //show an action executed message
        $mdToast.show($mdToast.simple()
                          .content('Dive saved!')
                          .position('bottom right')
                          .hideDelay(2000)
                         );
           
        //refresh location
        $route.reload();

    };

    //dive session save error callback
    $scope.onSaveError = function (data) {
        //stop dialog spinner
        $rootScope.closeWaitingSpinner();
        
        $log.info('dive session saved error!');
        //throw the eexception
        var error = new Error(data.errorMessage);
        throw error;
        

    };

    //manage removing success
    $scope.onDiveRemoveSuccess = function (data, diveId) {
        $log.info('dive removed');
        //stop dialog spinner
        $rootScope.closeWaitingSpinner();
        //update the current model
        modelService.freediverMdl.currentDiveSession.removeDive(diveId);
        
        //in this case the view state, before to go back, should bacame UPDATE
        modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;
        
        //show an action executed message
        $mdToast.show($mdToast.simple()
                          .content('Dive removed!')
                          .position('bottom right')
                          .hideDelay(2000)
                         );
        
        
        
        //route to dive session list page
        $scope.back();
    };

    //managed remove confirmation with confirmation dialog
    $scope.showRemoveConfirmation = function(ev) {
    // Appending dialog to document.body to cover sidenav in docs app
    var confirm = $mdDialog.confirm()
          .title('Would you like to remove this dive?')
          .textContent('Current dive will be removed permanently.')
          .ariaLabel('remove dive')
          .targetEvent(ev)
          .ok('Please do it!')
          .cancel('No thanks');
    $mdDialog.show(confirm).then(function() {
      $scope.removeDive();
        
    }, function() {
      $log.info('cancel remove!');
    });
  };   
    
        

});