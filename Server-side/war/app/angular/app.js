/*Creates all application module*/
//appNeaClient is the main module that represents the app. It needs all other modules dependencies
var appNeaClient = angular.module('appNeaClient', ['appNeaClient.service']);
var appNeaClientService = angular.module('appNeaClient.service', []);

appNeaClient.run(function(freediverService) {
    console.log('run after module loading');
    console.log('invoking freediverService...');
    var loginPromiseResponse = freediverService.login();
    
    loginPromiseResponse.success(function(data, status, headers, config) {
    	console.log(data);
    });
    loginPromiseResponse.error(function(data, status, headers, config) {
    	console.log(data);
    });
});