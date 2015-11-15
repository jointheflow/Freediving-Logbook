/* rootController is used to manage features shared between differents view in the app*/

//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

appNeaClient.controller('rootController', 
    
    //function ($rootScope, $scope, $modal, $log, $timeout, $location) {
    function ($rootScope, $scope, $log, $timeout, $location) {
        
       
		
		
		/*defining and managing alert, show message receive a _msg text a type and a focus _fcs that
		represent the elementID in the dom where the focus must be when the modal view is closed*/
      	/*$rootScope.showMsg = function (_msg, _tpe, _fcs) {
            var l_alerts =[{type: _tpe, msg: _msg}];
			
			var modalInstance = $modal.open({
              templateUrl: 'showMessageModal.html',
              controller: 'ShowMsgController',
              resolve: {
                alerts: function () {
                  return l_alerts;
                },
				focusTo: function () {
			  		return _fcs;
			  	}
              }
            });

            modalInstance.result.then(function () {
              //$scope.selected = selectedItem;
            }, function () {
              $log.info('Modal dismissed at: ' + new Date());
            });
        };
		
        */
        
        //set focus to destinationId or destinationId.destinationTag basing key event and key code
        //for example key code 13 correponds to RETURN
        $scope.setFocus = function(keyEvent, keyCode, destinationId, destinationTag) {
            if (keyEvent.which === keyCode) { 
              /*fix using $timeout https://docs.angularjs.org/error/$rootScope/inprog?p0=
			  because It is possible to workaround this problem by moving the call to set 
			  the focus outside of the digest, by using $timeout(fn, 0, false), where the
			  false value tells Angular not to wrap this fn in a $apply block*/
				$timeout(function () {
							//alert('Focus to '+destinationId);
							var element = document.getElementById(destinationId);

							if(element && destinationTag) {

								var childs = element.getElementsByTagName(destinationTag); 
								if (childs) childs[0].focus();
							}
							else if (element) 
								element.focus();
						}, 0, false);
            }
        };
		
		
		
});
