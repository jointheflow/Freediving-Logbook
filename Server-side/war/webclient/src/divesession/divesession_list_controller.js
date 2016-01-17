//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('diveSessionListController',  
	function ($scope, $rootScope, freediverService, modelService, $log, $timeout, $mdSidenav, $location, $mdDialog, $facebook) {
	
    
    
    //******model definition
    //application setting
    $scope.settings = null;
    //current freediver
    $scope.freediver = null;
    //current dive session
    $scope.divesession = {date: null, location: null};
    
    
    $scope.applicationName = freedivingLogbookConstant.applicationName;
    
    //toggle left side menu  
    $scope.openLeftMenu = function() {
        $mdSidenav('left').toggle();
    };
    
    
	
	/*manages rest server login success*/
	$scope.onLoginSuccess = function(data) {
		//stop the spinner
        $rootScope.closeWaitingSpinner();
        
        //populare model service!!!!!
        modelService.updateFreediverFromData(data);
        //put the freediver on the scope
        $scope.freediver=modelService.freediverMdl;
        
                
        
        
	};
	
	/*manages rest server login error*/
	$scope.onLoginError = function(data) {
		//stop the spinner
        $rootScope.closeWaitingSpinner();
        //throw the eexception
        var error = new Error(data.errorMessage);
        throw error;
        
		
	};
	
   
    /*Watching externalToken updating. Every time externalToken changes, means the uses has executed login/logout
    function on facebook side. Then we need to refresh the model regarding freediver data*/	
    $rootScope.$watch('externalToken', function() {
        $log.info('externalToken change to:'+$rootScope.externalToken);
        
        /*If external token has been changed to new value, we need to do Login on server side*/
        if ($rootScope.externalToken!=null) {
            //start the spinner
            $rootScope.showWaitingSpinner();
            
            freediverService.login(freedivingLogbookConstant.PLATFORM_FACEBOOK,
    	    					$rootScope.externalToken,
    	    					100, 
    	    					$scope.onLoginSuccess, 
    	    					$scope.onLoginError);
        /*if external token is null clean the context because user has been logged out*/
        } else {
        	$scope.externalToken=$rootScope.externalToken;
            $scope.freediver=null;
           
        	
        }
    });
    
    
    //Open dive session detail view in view or insert mode depending on aDivesession parameter
     $scope.showDivesessionDetail = function(aDivesession) {
        //throw new Error("You must select a current user!"); 
        if (aDivesession == null) {
            //set the current divesession on model to null (add new divesession)
            modelService.freediverMdl.currentDiveSession=null;
            modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_NEW;
            //change location path
            $location.path('/divesessiondetail');
            
        }else{
            
            //start the spinner
            $rootScope.showWaitingSpinner();
            modelService.freediverMdl.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;
            //get the complete dive session detail from service, is asynchronous, must manage result with callback
            freediverService.getDetailDiveSession(freedivingLogbookConstant.PLATFORM_FACEBOOK,
                                                  $rootScope.externalToken,
                                                  aDivesession.id,
                                                  $scope.onGetDiveSessionSuccess,
                                                  $scope.onGetDiveSessionError);
            
            //set the current divesession on model to the divesession selected (edit/display an old dive session)   
            //modelService.freediverMdl.currentDiveSession=aDivesession;
            
        }
    };
    
    
   /*manages get detail dive session success*/
	$scope.onGetDiveSessionSuccess = function(data) {
        //stop the spinner
        $rootScope.closeWaitingSpinner();
        
		//update model with detailed dive session
        modelService.addOrUpdateDiveSessionFromData(data.detail,
                                                    $scope.freediver.tempUnit,
                                                    $scope.freediver.weightUnit,
                                                    $scope.freediver.depthUnit);
        //change location to detail dive session view
        modelService.freediverMdl.diveSessionActiveTabIndex = freedivingLogbookConstant.TAB_DETAIL;
        $location.path('/divesessiondetail');
		
	}; 
    
    
   /*manages get detail dive session error*/
	$scope.onGetDiveSessionError = function(data) {
        //stop the spinner
        $rootScope.closeWaitingSpinner();
        //throw the eexception
        var error = new Error(data.errorMessage);
        throw error;
		
	}; 
    
    //redirect login to $rootScope login
    $scope.login = function() {
        $rootScope.login();
    };
    
    //every time the controller starts get the login status and refresh the info accordly
    $rootScope.getLoginStatus();

});   

