/*Contains wrapper of all facebook sdk services*/

//get reference to the service module defined in app.js
var appNeaClientFacebookService = angular.module('appNeaClient.facebook.service');

/*add facebook authentication services*/
appNeaClientFacebookService.service('fbAuth', function ($log, $rootScope) {

    this.watchLoginChange = function(fbConnectedCallBack, fbDisconnectedCallBack) {
        var _self = this;
        FB.Event.subscribe('auth.authResponseChange', function(res) {
            //if (res.status === 'connected') {
                /* 
                The user is already logged, 
                is possible retrieve his personal info
                */
                //_self.getUserInfo();
                $log.info('auth.authResponseChange event received: '+res.status);
                //invoking callback
                $rootScope.$apply(function() { 
                    if (res.status === 'connected') {
                        $rootScope.externalToken = res.authResponse.accessToken;
                    }else {
                        $rootScope.externalToken = null;
                    }
                });
                
                /*
                This is also the point where you should create a 
                session for the current user.
                For this purpose you can use the data inside the 
                res.authResponse object.
                */
            /*} else {
                $log.info('user not connected '+res.authResponse);
                /*
                The user is not logged to the app, or into Facebook:
                destroy the session on the server.
                */
               /* $rootScope.$apply(function() { 
                    $rootScope.externalToken = $rootScope.externalToken =null;
                });
              */  

                
                
            
        });
    };

    this.login = function () {
        FB.login(function(response) {
            if (response.authResponse) {
                $log.info('Welcome!  Fetching your information.... ');     
            } else {
                $log.info('User cancelled login or did not fully authorize.');
            }
        });
    };
    
    this.getUserInfo = function() {
        var _self = this;
        FB.api('/me', function(res) {
            $rootScope.$apply(function() { 
                $rootScope.user = _self.user = res; 
            });
        });
    };


    
    this.logout = function() {
        var _self = this;
        FB.logout(function(response) {
            $rootScope.$apply(function() { 
                $rootScope.user = _self.user = {}; 
            }); 
        });
    };
    
    this.getLoginStatus = function() {
        FB.getLoginStatus(function(response) {
            if (response.status === 'connected') {
                // the user is logged in and has authenticated your
                // app, and response.authResponse supplies
                // the user's ID, a valid access token, a signed
                // request, and the time the access token 
                // and signed request each expire
                $rootScope.$apply(function() { 
                    $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_LOGGED;
                    $rootScope.externalToken = response.authResponse.accessToken;
                });
            } else if (response.status === 'not_authorized') {
                // the user is logged in to Facebook, 
                // but has not authenticated your app
                $rootScope.$apply(function() { 
                    $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_NOT_AUTH;
                    $rootScope.externalToken = $rootScope.externalToken =null;
                });
            } else {
                // the user isn't logged in to Facebook.
                 $rootScope.$apply(function() { 
                    $rootScope.fbStatus = freedivingLogbookConstant.FB_STATUS_UNKNOWN;
                    $rootScope.externalToken = $rootScope.externalToken =null;
                });
            }
        });
    };

});