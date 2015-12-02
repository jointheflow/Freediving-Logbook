/*Creates all application module*/
//appNeaClient is the main module that represents the app. It needs all other modules dependencies


var appNeaClient = angular.module('appNeaClient', ['appNeaClient.service',  'ngMaterial', 'ngRoute']);

//defines appNeaClient.service module
var appNeaClientService = angular.module('appNeaClient.service', []);


/*Configuring routes*/
appNeaClient.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/divesessionlist', {
		templateUrl: 'src/divesession/divesession_list_view.html',
        controller: 'diveSessionListController'
      }).
      when('/divesessiondetail', {
		templateUrl: 'src/divesession/divesession_detail_view.html',
        controller: 'diveSessionDetailController'
      }).
      otherwise({
        redirectTo: '/divesessionlist',
		controller: 'diveSessionController'
      });
  }]);





/*CORS management*/
appNeaClient.config(['$httpProvider', function ($httpProvider) {
	  //Reset headers to avoid OPTIONS request (aka preflight)
	  $httpProvider.defaults.headers.common = {};
	  $httpProvider.defaults.headers.post = {};
	  $httpProvider.defaults.headers.put = {};
	  $httpProvider.defaults.headers.patch = {};
}]);


/*Angular Material theme configuration*/
appNeaClient.config(function($mdThemingProvider) {
  //$mdThemingProvider.theme('appnea') 
  $mdThemingProvider.theme('default')
    .primaryPalette('blue')
    .accentPalette('light-blue');
  
  //$mdThemingProvider.setDefaultTheme('appnea');
});


/*Init facebook SDK*/
appNeaClient.run(['$rootScope', '$window', 'fbAuth', 
    function($rootScope, $window, fbAuth) {
        
        /*Init facebook authentication status and app-nea status*/
        $rootScope.fbStatus= freedivingLogbookConstant.FB_STATUS_UNKNOWN;
        $rootScope.appNeaStatus = freedivingLogbookConstant.APPNEA_STATUS_UNKNOWN;
        $rootScope.externalToken=null;
        
        $window.fbAsyncInit = function() {
            // Executed when the SDK is loaded
            FB.init({ 
              /* 
               The app id of the web app;
               To register a new app visit Facebook App Dashboard
               ( https://developers.facebook.com/apps/ ) 
              */
              appId: freedivingLogbookConstant.facebook_app_id,  
              /* 
               Adding a Channel File improves the performance 
               of the javascript SDK, by addressing issues 
               with cross-domain communication in certain browsers. 
              */
              channelUrl: 'app/channel.html', 
              /* 
               Set if you want to check the authentication status
               at the start up of the app 
              */
              status: true, 
              /* 
               Enable cookies to allow the server to access 
               the session 
              */
              cookie: true,
              version: 'v2.0',    
              /* Parse XFBML */
              xfbml: true 
            });
            
           // fbAuth.getLoginStatus();
            fbAuth.watchLoginChange();
          };

  // Are you familiar to IIFE ( http://bit.ly/iifewdb ) ?
  (function(d){
    // load the Facebook javascript SDK
    var js, 
    id = 'facebook-jssdk', 
    ref = d.getElementsByTagName('script')[0];
    if (d.getElementById(id)) {
      return;
    }
    js = d.createElement('script'); 
    js.id = id; 
    js.async = true;
    js.src = "//connect.facebook.net/en_US/sdk.js";
              
    ref.parentNode.insertBefore(js, ref);
  }(document));

  }]);




