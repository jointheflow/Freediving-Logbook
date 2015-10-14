/**
 * This service provides access to the following rest services of App-nea platform
 * <host>/app/freediver/login
 */
var urlLogin='http://localhost:8888/app/freediver/login';
var diveSessionModule = angular.module('diveSessionModule');


diveSessionModule.service('diveSessionServices', function () {
	this.login = function (externalPlatform, externalToken, platformId) {
		return 'fake login data';
	};
	this.get = function (diveSessionId) {
		
	};
});
  


