//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('freediverController',  
	function ($scope,$rootScope, freediverService, $log, $timeout, fbAuth, usSpinnerService) {
	
	
	/*manages rest server login success*/
	$scope.onLoginSuccess = function(data) {
		//stop the spinner
        usSpinnerService.stop('spinner');
        
        //alert('Login success '+ data.message);
        //initialize the the scope with value fetch from login
        $scope.externalToken=$rootScope.externalToken;
        $scope.freediver=data.detail;
        $scope.freediver.weightUnit=data.weightUnit;
        $scope.freediver.deepUnit=data.deepUnit;
        $scope.freediver.tempUnit=data.tempUnit;
        
        
        
	};
	
	/*manages rest server login error*/
	$scope.onLoginError = function(data) {
		//stop the spinner
        usSpinnerService.stop('spinner');
        alert('Login error '+ data.errorMessage);
		
	};
	
   
    /*Watching externalToken updating. Every time externalToken changes, means the uses has executed login/logout
    function on facebook side. Then we need to refresh the model regarding freediver data*/	
    $rootScope.$watch('externalToken', function() {
        $log.info('externalToken change to:'+$rootScope.externalToken);
        
        /*If external token has been changed to new value, we need to do Login on server side*/
        if ($rootScope.externalToken!=null) {
            //start the spinner
            usSpinnerService.spin('spinner');
            
            freediverService.login(0,
    	    					$rootScope.externalToken,
    	    					100, 
    	    					$scope.onLoginSuccess, 
    	    					$scope.onLoginError);
        /*if external token is null clean the context because user has been logged out*/
        } else {
        	$scope.externalToken=$rootScope.externalToken;
            $scope.freediver=null;
           
        	
        }
    });
    
    
    
    /*This function executes facebook login and populate the scope model,
	 * invoking login service and finding dive sessions data about the user */
	/*$scope.login = function() {
		
        //executes fb login
		//$scope.fbIntentLogin();
        fbAuth.login();
        freediverService.login(0,
    	    					$rootScope.externalToken,
    	    					100, 
    	    					$scope.onLoginSuccess, 
    	    					$scope.onLoginError);
		
	};
    */
	
    //manege logout on fb but we let fb manage logout directly +/
    $scope.logout = function() {
        fbAuth.logout();
        window.location.reload();
    };
    
         
});   
