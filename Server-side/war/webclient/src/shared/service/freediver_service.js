/*Contains wrapper of all freediver http rest services*/

//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.rest.service');

/*add freediver services*/
appNeaClientService.service('freediverService', function ($http, $log) {
	/*add login/signin service*/
	this.login = function (externalPlatform, externalToken, pageSize, okCallBack, errorCallBack) {
		var loginUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiFreediverLoginPath +
						'?'+freedivingLogbookConstant.pNameExternalPlatform+'='+externalPlatform+
						'&'+freedivingLogbookConstant.pNamePageSize+'='+pageSize+
						'&'+freedivingLogbookConstant.pNameExternalToken+'='+externalToken;
		$log.info('freediverService.login executing:'+loginUrl);
		
		
		
		var loginPromiseResponse = $http({method: 'GET', url: loginUrl}); 
		
		
	    //managing success response
		loginPromiseResponse.success(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	okCallBack(data);
	    });
	    
	    //managing error response
		loginPromiseResponse.error(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	errorCallBack(data);
	    });
	};
    
    /*add a dive session*/
    this.addDiveSession = function (freediverId, externalPlatform, externalToken, deepUnit, tempUnit, weightUnit, diveDate, location, meteo, equipment, weight, temp, deep, okCallBack, errorCallBack) {
        var addDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionAdd;
        
        var dataParam = "external_platform_id="+externalPlatform+
                        "&external_token="+externalToken+
                        "&freediver_id="+freediverId+
                        "&dive_date="+diveDate+
                        "&deep_unit="+deepUnit+
                        "&weight_unit="+weightUnit+
                        "&temp_unit="+tempUnit+
                        "&deep="+deep+
                        "&equipment="+equipment+
                        "&location="+location+
                        "&meteo="+meteo+
                        "&water_temp="+temp+
                        "&weight="+weight;
        
        $log.info('freediverService.addDiveSession executing:'+addDiveSessionUrl);
        $log.info('Params:'+ dataParam);
        
        var addDiveSessionPromiseResponse = $http({method: 'POST',
                                                    url: addDiveSessionUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
        addDiveSessionPromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            //TODO
        }); 
        
        //managin error
         addDiveSessionPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            //TODO
        }); 
    
    
    
    }
    
	
	
	
	/*TODO: add remove service*/
	this.remove = function (externalPlatform, externalToken, freediverId) {
		return 'fake remove action';
	};
});