/* rootController is used to manage features shared between differents view in the app*/

//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

appNeaClient.controller('rootController',  
                         
    function ($rootScope, $scope, $log, $timeout, $location, $mdDialog, ezfb, deviceDetector) {
        //show the spinner
        $rootScope.showWaitingSpinner = function() {
            $mdDialog.show({
                    //controller: 'diveSessionDialogController',
                    templateUrl: 'src/spinnerdialog/spinner_dlg.html'
            });
        };
        //hide the spinner                
        $rootScope.closeWaitingSpinner = function() {
            $mdDialog.cancel();
        };
    
        //manage facebook login
        $rootScope.login = function() {
            //$timeout(function() {
                //start fix issue for Chrome in iOS
                $log.log('browser detected:'+deviceDetector.browser);
                $log.log('os detected:'+deviceDetector.os);
                if( deviceDetector.browser=='chrome' && deviceDetector.os=='ios') {
                    window.open('https://www.facebook.com/dialog/oauth?client_id='+freedivingLogbookConstant.facebook_app_id+'&redirect_uri='+ freedivingLogbookConstant.applicationHomePageUrl +'&scope=email,public_profile', '_self', 'null');
                }else { 
                //end fix issue
                    ezfb.login().then(function() {
                        $rootScope.getLoginStatus();
                    });
                }
                //},1000,
               // false);
        };
    
        $rootScope.alert = function() {
            alert('alert!!');
            
        }
    
        //manage facebook login
        $rootScope.logout = function() {
            ezfb.logout().then(function() {
                $rootScope.getLoginStatus();
            });
        };
    
        //check the login status using easy-fb api
        $rootScope.getLoginStatus = function() {
            $log.info('getting login status.......');
            
            ezfb.getLoginStatus().then( 
              function(response) {

                 if (response.status === 'connected') {
                    // the user is logged in and has authenticated your
                    // app, and response.authResponse supplies
                    // the user's ID, a valid access token, a signed
                    // request, and the time the access token 
                    // and signed request each expire
                    //$rootScope.$apply(function() {
                        $log.info('got login status! connected');
                        $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_LOGGED;
                        $rootScope.externalToken = response.authResponse.accessToken;
                    //});
                } else if (response.status === 'not_authorized') {
                    // the user is logged in to Facebook, 
                    // but has not authenticated your app
                    //$rootScope.$apply(function() { 
                        $log.info('got login status! not_authorized');
                        $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_NOT_AUTH;
                        $rootScope.externalToken = null;
                    //});
                } else {
                    // the user isn't logged in to Facebook.
                     //$rootScope.$apply(function() { 
                        $log.info('got login status! unknown');
                        $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_UNKNOWN;
                        $rootScope.externalToken = null;
                    //});
                }

              },
              function(err) {
                    $log.info('got login error! unknown');
                    $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_UNKNOWN;
                    $rootScope.externalToken = null;

              });
        };
        
    //go to setting view
    $rootScope.setting = function() {
        $location.path('/setting');
    };
   
    //every time the controller starts get the login status and refresh the info accordly
    $rootScope.getLoginStatus();
    
		
});
