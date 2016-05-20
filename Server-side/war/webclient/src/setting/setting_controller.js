//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('settingController',  function ($scope,$rootScope, modelService, $log, $mdDialog) {
    
    $scope.freediver = modelService.freediverMdl;
    //$scope.customFieldListOfDiveSession = ["custom_one", "custom_two", "custom_three"];
    $scope.customFieldListOfDiveSession = [
      { name: 'custom_one'},
      { name: 'custom_two'},
      { name: 'custom_three'}
    ];
    
     /*check if the custom already exists in the list*/
    $scope.customFieldExists = function() {
        if ($scope.newCustom !=null && $scope.newCustom.length>0) {
            for (var i = 0; i < $scope.customFieldListOfDiveSession.length; i++){
                if ($scope.customFieldListOfDiveSession[i].name.toUpperCase() == $scope.newCustom.toUpperCase()){
                    return true;
                }
            }
            return false;
        }else return true;
        
    }
    
    $scope.removeCustom = function() {
        if ($scope.custom != null) {
            $log.info("Removing item index "+$scope.custom.index);
            $scope.customFieldListOfDiveSession.splice($scope.custom.index, 1);
        }
     // $scope.customFieldListOfDiveSession.pop();
    };
    
    //go to back
    $scope.back = function() {
            window.history.back();
            //$location.path('/divesessiondetail');
    };
    
   
    $scope.dialogAdd = function() {
        $log.info('add pressed for field name:'+$scope.newCustom);
        //if ($scope.newCustom!=null && $scope.newCustom.length>0) {
            $scope.customFieldListOfDiveSession.push({name: $scope.newCustom});
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