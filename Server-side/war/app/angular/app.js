/*Creates all application module*/
//appNeaClient is the main module that represents the app. It needs all other modules dependencies
var appNeaClient = angular.module('appNeaClient', ['appNeaClient.service']);
var appNeaClientService = angular.module('appNeaClient.service', []);


/*appNeaClient.run(function(freediverService) {
    console.log('run after module loading');
    console.log('invoking freediverService...');

    var successFun = function(data) {
    		alert(data);
    };
    
    var errorFun = function(data) {
		alert(data.errorMessage);
    };
    freediverService.login(0,'CAAB4GhgAGN0BALfswdiZAMWTANTAxbbFDSTxZAZBLGKfPKunhcaodAenbiSJ0vKcQasM1DKWNgc11UlSuF0uQ6rCJQm1vIU5SlPdOBOhv6yZA3jZCqA7g7RzZAfWP5ZB6vxokZBESOLgfN8FdLGf9MumwgyzBZB0a9fB4KKQBTeTGZBj9Kinh5DR8tk5NyrEofo7dL24gAmHPNwQZDZD',100, successFun, errorFun);
    
});*/
appNeaClient.config(['$httpProvider', function ($httpProvider) {
	  //Reset headers to avoid OPTIONS request (aka preflight)
	  $httpProvider.defaults.headers.common = {};
	  $httpProvider.defaults.headers.post = {};
	  $httpProvider.defaults.headers.put = {};
	  $httpProvider.defaults.headers.patch = {};
	}]);