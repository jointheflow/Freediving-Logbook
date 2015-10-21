//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('freediverController',  
	function ($scope,$rootScope, freediverService, $log, $timeout, fbAuth) {
	
	/*This function executes facebook login and populate the scope model,
	 * invoking login service and finding dive sessions data about the user */
	$scope.login = function() {
		
        //executes fb login
		//$scope.fbIntentLogin();
        fbAuth.login();
        /*freediverService.login(0,
    	    					$rootScope.externalToken,
    	    					100, 
    	    					$scope.onLoginSuccess, 
    	    					$scope.onLoginError);
		*/
	};
	
	
	/*manages login success*/
	$scope.onLoginSuccess = function(data) {
		alert('Login success '+ data.message);
        $scope.freediver=data.detail;
	};
	
	/*manages login error*/
	$scope.onLoginError = function(data) {
		alert('Login error '+ data.errorMessage);
		
	};
	
    $scope.logout = function() {
        fbAuth.logout();
    };
    	
    $rootScope.$watch('externalToken', function() {
        $log.info('externalToken:'+$rootScope.externalToken);
        $scope.externalToken=$rootScope.externalToken;
        if ($rootScope.externalToken!=null) {
            freediverService.login(0,
    	    					$rootScope.externalToken,
    	    					100, 
    	    					$scope.onLoginSuccess, 
    	    					$scope.onLoginError);
        }
    });    
         
});   
