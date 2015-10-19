//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('freediverController', 
	function ($scope, freediverService, $log) {
	
	/*This function executes facebook login and populate the scope model,
	 * invoking login service and finding dive sessions data about the user */
	$scope.login = function() {
		//executes fb login
		//TODO
		//...then executes login on freediving-logbook platform
		freediverService.login(0,
					'xxxx',
					100, 
					$scope.onLoginSuccess, 
					$scope.onLoginError);
	};
	
	
	/*manages login success*/
	$scope.onLoginSuccess = function(data) {
		alert('Login success '+ data.message);
	};
	
	/*manages login error*/
	$scope.onLoginError = function(data) {
		alert('Login error '+ data.errorMessage);
		
	};
	     
});   