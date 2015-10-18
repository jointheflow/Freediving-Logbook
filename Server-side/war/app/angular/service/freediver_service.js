/*Contains wrapper of all freediver http services*/
//get reference to the service module
var appNeaClientService = angular.module('appNeaClient.service');

/*add freediver services*/
appNeaClientService.service('freediverService', function ($http, $log) {
	/*add login/signin service*/
	this.login = function (externalPlatform, externalToken, pageSize) {
		var loginUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiFreediverLoginPath +
						'?'+freedivingLogbookConstant.pNameExternalPlatform+'='+externalPlatform+
						'&'+freedivingLogbookConstant.pNamePageSize+'='+pageSize+
						'&'+freedivingLogbookConstant.pNameExternalToken+'='+externalToken;
		$log.info('freediverService.login executing:'+loginUrl);
		return $http.get(loginUrl);
		
	};
	
	
	
	/*add remove service*/
	this.remove = function (externalPlatform, externalToken, freediverId) {
		return 'fake remove action';
	};
});