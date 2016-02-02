/*Creates all application module*/
//appNeaClient is the main module that represents the app. It needs all other modules dependencies


var appNeaClient = angular.module('appNeaClient', ['appNeaClient.service',  'ngMaterial', 'ngRoute', 'ngFacebook', 'chart.js']);

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
      when('/divedetail', {
		templateUrl: 'src/dive/dive_detail_view.html',
        controller: 'diveDetailController'
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
    .primaryPalette('yellow')
    .accentPalette('orange');
  
  //$mdThemingProvider.setDefaultTheme('appnea');
});



//configure an exception handler that override the default implementation
//showing an alert in the Dialog
appNeaClient.config(function($provide) {
    $provide.decorator('$exceptionHandler', ['$delegate', '$injector', '$log', function($delegate, $injector, $log) {
        return function(exception, cause) {
            
            $delegate(exception, cause);
			
            $log.error(exception + " caused by "+cause);
			/* Alert manager start */
			/*Avoid Circular dependency found: $modal <- $exceptionHandler <- $rootScope
			we need to call the $injector manually to resolve the dependency at runtime*/
            var ServiceDialogAlert;
			ServiceDialogAlert = $injector.get('serviceDialogAlert');
            
            
            //show a service dialog alert
            ServiceDialogAlert.show(exception.message, exception.stack);
    	};
  	}
  ]);        
 });       
        


/*Configuring ngFacebook*/
appNeaClient.config( function( $facebookProvider ) {
  $facebookProvider.setAppId(freedivingLogbookConstant.facebook_app_id);
  $facebookProvider.setPermissions("user_about_me, email, publish_actions");
});


/*Init facebook SDK*/
appNeaClient.run(['$rootScope', 
    (function($rootScope){
  // Load the facebook SDK asynchronously
      (function(){
         // If we've already installed the SDK, we're done
         if (document.getElementById('facebook-jssdk')) {return;}

         // Get the first script element, which we'll use to find the parent node
         var firstScriptElement = document.getElementsByTagName('script')[0];

         // Create a new script element and set its id
         var facebookJS = document.createElement('script'); 
         facebookJS.id = 'facebook-jssdk';

         // Set the new script's source to the source of the Facebook JS SDK
         facebookJS.src = '//connect.facebook.net/en_US/all.js';

         // Insert the Facebook JS SDK into the DOM
         firstScriptElement.parentNode.insertBefore(facebookJS, firstScriptElement);
      }());
   })    
]);



  




