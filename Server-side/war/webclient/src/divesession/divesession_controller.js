//get reference to the app module defined in app.js
var appNeaClient = angular.module('appNeaClient');

/*controller definition */
appNeaClient.controller('diveSessionController',  
	function ($scope,$rootScope, freediverService, $log, $timeout, $mdSidenav, fbAuth, $mdDialog) {
	
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
        $scope.externalToken = data.externalToken;
        $scope.externalPlatformId = data.externalPlatformId;
        $scope.deepUnit = data.deepUnit;
        $scope.tempUnit = data.tempUnit;
        $scope.weightUnit = data.weightUnit;
        $scope.freediver=data.detail;
       
        
                
        
        
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
            
            freediverService.login(0,
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
    
    //Open dive session detail dialog
     $scope.showDivesessionDetailDialog = function(ev) {
        $mdDialog.show({
            scope: $scope.$new(),
            controller: DialogController,
            templateUrl: 'src/divesession/divesession_detail_dialog.html',
            targetEvent: ev,
        })
            .then(function(answer) {
                $log.info('dive session dialog Save press');
                $log.info($scope.divesessionDate);
                //$log.info($scope.divesession.location);
                freediverService.addDiveSession($scope.freediver.externalId,
                                                $scope.externalPlatformId,
                                                $scope.externalToken,
                                                $scope.deepUnit,
                                                $scope.tempUnit,
                                                $scope.weightUnit,
                                                $scope.divesession.date);
            
                $scope.alert = 'You said the information was "' + answer + '".';
            }, function() {
                $log.info('dive session dialog Canecel press');
                $scope.alert = 'You cancelled the dialog.';
        });
    };
    

    //cancel dive session detail dialog
      $scope.cancelDialog = function() {
        $mdDialog.cancel();
      };
    
    //save dive session detail dialog
      $scope.saveDialog = function() {
        $mdDialog.hide();
      };
      
    
    
});   

function DialogController($scope, $mdDialog) {
  $scope.hide = function() {
    $mdDialog.hide();
  };
  $scope.cancel = function() {
    $mdDialog.cancel();
  };
  $scope.answer = function(answer) {
    $mdDialog.hide(answer);
  };
};
