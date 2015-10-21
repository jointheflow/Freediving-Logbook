/*Contains wrapper of all freediver http services*/

//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.service');

/*add freediver services*/
appNeaClientService.service('freediverService', function ($http, $log) {
	/*add login/signin service*/
	this.login = function (externalPlatform, externalToken, pageSize, okCallBack, errorCallBack) {
		var loginUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiFreediverLoginPath +
						'?'+freedivingLogbookConstant.pNameExternalPlatform+'='+externalPlatform+
						'&'+freedivingLogbookConstant.pNamePageSize+'='+pageSize+
						'&'+freedivingLogbookConstant.pNameExternalToken+'='+externalToken;
		$log.info('freediverService.login executing:'+loginUrl);
		
		
		
		var loginPromiseResponse = $http({method: 'GET', url: loginUrl}); 
		
		
	    //managing success response
		loginPromiseResponse.success(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	okCallBack(data);
	    });
	    
	    //managing error response
		loginPromiseResponse.error(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	errorCallBack(data);
	    });
	};
	
	
	
	/*TODO: add remove service*/
	this.remove = function (externalPlatform, externalToken, freediverId) {
		return 'fake remove action';
	};
});