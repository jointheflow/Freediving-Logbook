/* Creates all application module */
//appPublishedClient is the main module that represents the published fb page. It needs all other modules dependencies

var appPublishedClient = angular.module('appPublishedClient', ['appNeaClient.service',  'ngMaterial', 'zingchart-angularjs']);

//defines appNeaClient.service module
var appNeaClientService = angular.module('appNeaClient.service', []);

/*CORS management*/
appPublishedClient.config(['$httpProvider', function ($httpProvider) {
	  //Reset headers to avoid OPTIONS request (aka preflight)
	  $httpProvider.defaults.headers.common = {};
	  $httpProvider.defaults.headers.post = {};
	  $httpProvider.defaults.headers.put = {};
	  $httpProvider.defaults.headers.patch = {};
}]);


/*Angular Material theme configuration*/
appPublishedClient.config(function($mdThemingProvider) {
  //$mdThemingProvider.theme('appnea') 
  $mdThemingProvider.theme('default')
    .primaryPalette('blue')
    .accentPalette('pink');
  
  //$mdThemingProvider.setDefaultTheme('appnea');
});




  




