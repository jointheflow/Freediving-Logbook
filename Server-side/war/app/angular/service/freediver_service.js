/*Contains wrapper of all freediver http services*/
//get reference to the service module
var servicesModule = angular.module('servicesModule');

/*add freediver services*/
servicesModule.service('freediverService', function () {
	/*add login/signin service*/
	this.login = function (externalPlatform, externalToken, platformId) {
		return 'fake login data';
	};
	/*add remove service*/
	this.remove = function (externalPlatform, externalToken, freediverId) {
		
	};
});