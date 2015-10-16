/*Contains wrapper of all freediver http services*/
//get reference to the service module
var appNeaClientService = angular.module('appNeaClient.service');

/*add freediver services*/
appNeaClientService.service('freediverService', function ($http) {
	/*add login/signin service*/
	this.login = function (externalPlatform, externalToken, platformId) {
		var loginResult;
		return $http.get(freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiFreediverLoginPath);
		
	};
	
	
	
	/*add remove service*/
	this.remove = function (externalPlatform, externalToken, freediverId) {
		return 'fake remove action';
	};
});