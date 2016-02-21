//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

//controls the divesession detail view
appNeaClient.controller('diveSessionDetailController',  
	function ($rootScope, $scope, modelService, freediverService, $log, $filter, $location, $mdDialog, $mdToast, $route) {
        
        $scope.viewStatus = modelService.freediverMdl.viewstatus;
    
        //index of the current tab
        $scope.selectedIndex=modelService.freediverMdl.diveSessionActiveTabIndex;
        
        //icon for the current fab button
        $scope.actionIcon= freedivingLogbookConstant.ICON_SAVE_DIVESESSION;
    
        //set the current divesession with that selected in the model
        $scope.divesession = modelService.freediverMdl.currentDiveSession;
        //order basing diveTime
        if (modelService.freediverMdl.viewstatus==freedivingLogbookConstant.VIEW_UPDATE) {
            $scope.divesession.dives = $filter('orderBy')(modelService.freediverMdl.currentDiveSession.dives, 'diveTime', false);
        };
        
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
            
            //check if $scope.divesession if defined. If not create, this prevent exception saving new divesession with no parameters. This overwrite all field of $scope.divesession.
            if ($scope.divesession==null) {
                $scope.divesession =  new Object();
            }
            //activate dialog spinner
            $rootScope.showWaitingSpinner();
            
            //basing on current dive session id we know if it is a new dive session (add) or update an existent dive session (update)
            //regarding the status of the view set the defaut attribute to show
            switch(modelService.freediverMdl.viewstatus) {
                case freedivingLogbookConstant.VIEW_NEW:    
                    //if ($scope.divesession.id == null) {
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
                    break;
                    //}else {
                case freedivingLogbookConstant.VIEW_UPDATE:
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
                    break;
                    //}

                };
        };
    
    
        /*remove dive session*/
        $scope.removeDivesession = function() {
            //activate dialog spinner
            $rootScope.showWaitingSpinner();
            
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
            $rootScope.closeWaitingSpinner();
            
            //TODO: populate the model with the new divesession
            $log.info('model population with:'+data);
            modelService.addOrUpdateDiveSessionFromData(data.detail);
            //update the scope!
            $scope.divesession = modelService.freediverMdl.currentDiveSession; 
            //update the status of the view
            modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;
            //refresh location
            $route.reload();
            
            //show an action executed message
            $mdToast.show($mdToast.simple()
                          .content('Dive session saved!')
                          .position('bottom right')
                          .hideDelay(2000)
                         );
           
            
        };
    
        //dive session save error callback
        $scope.onSaveError = function (data) {
            //stop dialog spinner
            $rootScope.closeWaitingSpinner();
            //TODO: manage error
            //alert(data);
            $log.info('dive session saved error!');
            var error = new Error(data.errorMessage);
            throw error;
              
        };
    
        //manage removing success
        $scope.onDivesessionRemoveSuccess = function (data, divesessionId) {
            $log.info('dive session removed');
            //stop dialog spinner
            $rootScope.closeWaitingSpinner();
            
            //update the current model
            modelService.freediverMdl.removeDiveSession(divesessionId);
            
            //show an action executed message
            $mdToast.show($mdToast.simple()
                          .content('Dive session removed!')
                          .position('bottom right')
                          .hideDelay(2000)
                         );
           
            
            //route to dive list page
            $scope.back();
        };
    
    
        
    //Open dive detail view in edit or insert mode depending on aDive parameter
    $scope.showDiveDetail = function(aDive) {
        
        if (aDive == null)
            modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_NEW;
        else { 
            modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;
            modelService.freediverMdl.currentDiveSession.currentDive=aDive;
        }
        $location.path('/divedetail');
    };
    
    
    
    //managed remove confirmation with confirmation dialog
    $scope.showRemoveConfirmation = function(ev) {
    // Appending dialog to document.body to cover sidenav in docs app
    var confirm = $mdDialog.confirm()
          .title('Would you like to remove this dive session?')
          .textContent('Current dive session will be removed permanently.')
          .ariaLabel('remove dive session')
          .targetEvent(ev)
          .ok('Please do it!')
          .cancel('No thanks');
    $mdDialog.show(confirm).then(function() {
      $scope.removeDivesession();
        
    }, function() {
      $log.info('cancel remove!');
    });
  };
    
   //managed share confirmation with confirmation dialog
    $scope.showShareConfirmation = function(ev) {
    // Appending dialog to document.body to cover sidenav in docs app
    var confirm = $mdDialog.confirm()
          .title('Would you like to share this dive session with Facebook?')
          .textContent('Current dive session will be posted in your Facebook profile by appnea.')
          .ariaLabel('share dive session')
          .targetEvent(ev)
          .ok('Post to Facebook!')
          .cancel('No thanks');
    $mdDialog.show(confirm).then(function() {
      $scope.shareSession();
        
    }, function() {
      $log.info('cancel share!');
    });
  }; 
    
    //execute sharing on facebook
    $scope.shareSession = function() {
         //activate dialog spinner
         $rootScope.showWaitingSpinner();
         freediverService.publishDiveSession(modelService.freediverMdl.externalPlatformId,
                                               modelService.freediverMdl.externalToken,
                                               $scope.divesession.id, 
                                               $scope.onShareSuccess,
                                               $scope.onShareError);
        
    };
    
    
    //managing share success callback
    $scope.onShareSuccess = function (data) {
        $log.info('dive session shared!');
        //stop dialog spinner
        $rootScope.closeWaitingSpinner();
        //show an action executed message
        $mdToast.show($mdToast.simple()
                      .content('Dive session shared on Facebook!')
                      .position('bottom right')
                      .hideDelay(2000)
                     );
           
    };
    
    //managing share error callback
    $scope.onShareError = function (data) {
        $log.info('error during sharing session!');
        //stop dialog spinner
        $rootScope.closeWaitingSpinner();
        var error = new Error(data.errorMessage);
        throw error;
            
    };
    
    /*CHART MANAGEMENT START*/
    $scope.populateXChart = function () {
        xChart = []; 
        switch(modelService.freediverMdl.viewstatus) {
                case freedivingLogbookConstant.VIEW_NEW:    
                    break;
                    
                case freedivingLogbookConstant.VIEW_UPDATE:
                    for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
                        xChart.push(modelService.freediverMdl.currentDiveSession.dives[i].getTimeHHMM());
                    }
                break;
        }
        return xChart;
    };
    
    $scope.populateYChartDepth = function () {
        yChart = [];
        switch(modelService.freediverMdl.viewstatus) {
                case freedivingLogbookConstant.VIEW_NEW:    
                    break;
                    
                case freedivingLogbookConstant.VIEW_UPDATE:
                    for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
                       yChart.push(modelService.freediverMdl.currentDiveSession.dives[i].maxDepth);
                    } 
                    break;
        }
        return yChart;
    };
    
     $scope.populateYChartDuration = function () {
         yChart = [];
         
        switch(modelService.freediverMdl.viewstatus) {
                case freedivingLogbookConstant.VIEW_NEW:    
                    break;
                    
                case freedivingLogbookConstant.VIEW_UPDATE:
                    for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
                       yChart.push(modelService.freediverMdl.currentDiveSession.dives[i].duration);
                    } 
                    break;
        }
       
        return yChart;
    };
    
    $scope.labels = $scope.populateXChart();
    
    $scope.seriesDepth =['Depth'];
    $scope.seriesDuration =['Duration'];
   
   
    $scope.dataDepth = [$scope.populateYChartDepth()];
    $scope.dataDepthOption = {datasetFill : false};
    
    $scope.dataDuration =[$scope.populateYChartDuration()];
    $scope.dataDurationOption = {datasetFill : false}; 
    
    $scope.myJson = {
        "type":"mixed",
            "title":{
                "text":"Depth vs Duration"
            },
            "scale-x":{
                "labels": $scope.populateXChart()
            },
            "scale-y":{
                "values":"0:"+Math.floor($scope.divesession.getMaxDiveDepth(0)+30)+":5",
                "label": {
                        "text":"meters"
                        }
            },
            "scale-y-2":{
                "values":"0:"+($scope.divesession.getMaxDiveDuration()+30)+":5",
                "label": {
                        "text":"duration"
                        }
                //"labels":["1","2","3"],
                //"show-labels":["A","B", "C", "D"]
                
                
            },
            "series":[
                {
                    "values": $scope.populateYChartDepth(),
                    "type":"line",
                    "scales":"scale-x,scale-y",
                    //"aspect": "spline",
                    "text": "Depth chart"
                    
                },
                {
                    "values":$scope.populateYChartDuration(),
                    "type":"line",
                    "scales":"scale-x,scale-y-2",
                    //"aspect": "spline",
                    label: {"text": "Duration chart"}
                }
            ]
    };
    
    /*CHART MANAGEMENT END*/
});
	