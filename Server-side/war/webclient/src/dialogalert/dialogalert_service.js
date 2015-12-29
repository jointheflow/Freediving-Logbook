/*This service is used to create and show alert in a centralized point
 */
var appNeaClientService = angular.module('appNeaClient.service');

appNeaClientService.service('serviceDialogAlert', function ($mdDialog) {

        this.serviceName = 'serviceDialogAlert';
    
        this.show = function(mTitle, mDetail) {
            
            $mdDialog.show({
              templateUrl: 'src/dialogalert/dialogalert_view.html',
              parent: angular.element(document.body),
              clickOutsideToClose:true,
              fullscreen: false,
              locals: { dTitle: mTitle, dDetail: mDetail },
              /*controller: ['$scope', 'dTitle', 'dDetail', function($scope, dTitle, dDetail, $mdDialog) { 
                    $scope.title = dTitle;
                    $scope.detail = dDetail;
                    $scope.close = function() {
                        $mdDialog.cancel();
                    };
              }]*/
              controller : serviceDialogController
            });
            
        };
    
        serviceDialogController = function($scope, dTitle, dDetail, $mdDialog) {
           $scope.title = dTitle;
           $scope.detail = dDetail;  
           $scope.close = function() {
                        $mdDialog.cancel();
           };
        };

});
    
    
