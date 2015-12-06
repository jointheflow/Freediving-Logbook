//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('diveSessionListController',  
	function ($scope, $rootScope, freediverService, modelService, $log, $timeout, $mdSidenav, fbAuth, $location) {
	
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
        $scope.spinner="";
        
        //alert('Login success '+ data.message);
        //initialize the the scope and $rootscope with value fetch from login
        /*$scope.externalToken = data.externalToken;
        $scope.externalPlatformId = data.externalPlatformId;
        $scope.deepUnit = data.deepUnit;
        $scope.tempUnit = data.tempUnit;
        $scope.weightUnit = data.weightUnit;
        */
        
        //populare model service!!!!!
        modelService.updateFreediverFromData(data);
        //put the freediver on the scope
        $scope.freediver=modelService.freediverMdl;
        
                
        
        
	};
	
	/*manages rest server login error*/
	$scope.onLoginError = function(data) {
		//stop the spinner
        $scope.spinner="";
        alert('Login error '+ data.errorMessage);
		
	};
	
   
    /*Watching externalToken updating. Every time externalToken changes, means the uses has executed login/logout
    function on facebook side. Then we need to refresh the model regarding freediver data*/	
    $rootScope.$watch('externalToken', function() {
        $log.info('externalToken change to:'+$rootScope.externalToken);
        
        /*If external token has been changed to new value, we need to do Login on server side*/
        if ($rootScope.externalToken!=null) {
            //start the spinner
            $scope.spinner="indeterminate";
            
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
    
    
    //manage login on fb
    $scope.login = function() {
        fbAuth.login();
        
    };
    
    //manage logout on fb but we let fb manage logout directly +/
    $scope.logout = function() {
        fbAuth.logout();
        window.location.reload();
    };
    
    //Open dive session detail view in view or insert mode depending on aDivesession parameter
     $scope.showDivesessionDetail = function(aDivesession) {
        if (aDivesession == null) {
            //set the current divesession on model to null (add new divesession)
            modelService.freediverMdl.currentDiveSession=null;
            //change location path
            $location.path('/divesessiondetail');
        }else{
            //get the complete dive session detail from service, is asynchronous, must manage result with callback
            freediverService.getDetailDiveSession(freedivingLogbookConstant.PLATFORM_FACEBOOK,
                                                  $rootScope.externalToken,
                                                  aDivesession.id,
                                                  $scope.onGetDiveSessionSuccess,
                                                  $scope.onGetDiveSessionError);
            
            //set the current divesession on model to the divesession selected (edit/display an old dive session)   
            //modelService.freediverMdl.currentDiveSession=aDivesession;
            
        }
        //change location to detail 
       
         /*$mdDialog.show({
            controller: 'diveSessionDialogController',
            templateUrl: 'src/divesession/divesession_detail_dialog.html',
            targetEvent: ev,
        })
            .then(function(answer) {
                $log.info('dive session dialog Save pressed');
                //TODO update scope with model service
                
            }, function() {
                $log.info('dive session dialog Canecel press');
                $scope.alert = 'You cancelled the dialog.';
        });
        */
    };
    
    
   /*manages get detail dive session success*/
	$scope.onGetDiveSessionSuccess = function(data) {
		//update model with detailed dive session
        modelService.addOrUpdateDiveSessionFromData(data.detail,
                                                    $scope.freediver.tempUnit,
                                                    $scope.freediver.weightUnit,
                                                    $scope.freediver.depthUnit);
        //change location to detail dive session view
        $location.path('/divesessiondetail');
		
	}; 
    
    
   /*manages get detail dive sessio error*/
	$scope.onGetDiveSessionError = function(data) {
		
        alert('Get dive session error:'+ data.errorMessage);
		
	}; 
          
        
});   

