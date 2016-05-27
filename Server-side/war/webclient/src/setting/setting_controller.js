//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('settingController',  function ($scope,$rootScope, modelService, $log, $mdDialog, freediverService, $mdToast) {
    
    $scope.freediver = modelService.freediverMdl;
    //$scope.customFieldListOfDive = ["custom_one", "custom_two", "custom_three"];
    /*$scope.customFieldListOfDive = [
      { name: 'custom_one'},
      { name: 'custom_two'},
      { name: 'custom_three'}
    ];*/
    $scope.customFieldListOfDive = modelService.freediverMdl.customFieldListOfDive;
    
    
     /*check if the custom already exists in the list*/
    $scope.customFieldExists = function() {
        if ($scope.newCustom !=null && $scope.newCustom.length>0 && $scope.customFieldListOfDive != null) {
            for (var i = 0; i < $scope.customFieldListOfDive.length; i++){
                if ($scope.customFieldListOfDive[i].toUpperCase() == $scope.newCustom.toUpperCase()){
                    return true;
                }
            }
            return false;
        }else 
            return false;
        
    }
    
    $scope.removeCustom = function() {
        if ($scope.custom != null) {
            $log.info("Removing item index "+$scope.custom.index);
            $scope.customFieldListOfDive.splice($scope.custom.index, 1);
        }
     // $scope.customFieldListOfDive.pop();
    };
    
    //go to back
    $scope.back = function() {
            window.history.back();
            //$location.path('/divesessiondetail');
    };
    
    $scope.saveSetting = function() {
        $log.info("Call save setting");
        //activate dialog spinner
        $rootScope.showWaitingSpinner();
        freediverService.updateFreediverSetting(modelService.freediverMdl.id,
                                                modelService.freediverMdl.externalPlatformId,
                                                modelService.freediverMdl.externalToken,
                                                $scope.customFieldListOfDive,
                                                $scope.onSaveSettingSuccess,
                                                $scope.onSaveSettingError);
    };
    
    $scope.onSaveSettingSuccess = function() {
        $log.info("Save setting success");
        $rootScope.closeWaitingSpinner();
         //show an action executed message
            $mdToast.show($mdToast.simple()
                          .content('Setting saved!')
                          .position('bottom right')
                          .hideDelay(2000)
                         );
    };
    
    $scope.onSaveSettingError = function() {
        $log.info("Save setting error");
         $rootScope.closeWaitingSpinner();
            //TODO: manage error
            //alert(data);
            $log.info('Save setting error!');
            var error = new Error(data.errorMessage);
            throw error;
    };
   
    $scope.dialogAdd = function() {
        $log.info('add pressed for field name:'+$scope.newCustom);
        //if ($scope.newCustom!=null && $scope.newCustom.length>0) {
        //create list if not exists
        if ($scope.customFieldListOfDive == null) {
            $scope.customFieldListOfDive = [];
        }
        $scope.customFieldListOfDive.push($scope.newCustom);
        //}
        $mdDialog.cancel();
        
    };
    
    $scope.dialogCancel = function() {
        $log.info('cancel pressed');
        $mdDialog.cancel();

    };
    
   
    $scope.addCustom = function() { 
        
        $mdDialog.show({
                  templateUrl: 'src/setting/dialog_addcustomfield_view.html',
                  parent: angular.element(document.body),
                  clickOutsideToClose:true,
                  fullscreen: false,
                  preserveScope: true,
                  scope: $scope    
                  //locals: { dTitle: mTitle, dDetail: mDetail },
                  /*controller: ['$scope', 'dTitle', 'dDetail', function($scope, dTitle, dDetail, $mdDialog) { 
                        $scope.title = dTitle;
                        $scope.detail = dDetail;
                        $scope.close = function() {
                            $mdDialog.cancel();
                        };
                  }]*/
                  //controller : serviceDialogController
                });

            };

           // serviceDialogController = function($scope, dTitle, dDetail, $mdDialog) {
            serviceDialogController = function($scope, $mdDialog) {
               /*$scope.title = dTitle;
               $scope.detail = dDetail;  
               */
               $scope.close = function() {
                            $mdDialog.cancel();
               };
                
               
            };
    

});