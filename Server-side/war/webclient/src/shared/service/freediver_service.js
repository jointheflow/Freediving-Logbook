/*Contains wrapper of all freediver http rest services*/

//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.service');

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
    this.addDiveSession = function (freediverId, externalPlatform, externalToken, deepUnit, tempUnit, weightUnit, diveDate, location, meteo, equipment, weight, temp, deep, note, okCallBack, errorCallBack) {
        var addDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionAdd;
        
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&freediver_id='+freediverId+
                        '&dive_date='+diveDate+
                        ((deepUnit == null) ? '' : '&deep_unit='+deepUnit)+
                        ((weightUnit == null) ? '' :'&weight_unit='+weightUnit)+
                        ((tempUnit == null) ? '' : "&temp_unit="+tempUnit)+
                        ((deep == null) ? '' :'&deep='+deep)+
                        ((equipment == null) ? '' : '&equipment='+equipment)+
                        ((location == null) ? '' : '&location='+location)+
                        ((meteo == null) ? '' : '&meteo='+meteo)+
                        ((temp == null) ? '' : '&water_temp='+temp)+
                        ((weight == null) ? '' : '&weight='+weight)+
                        ((note == null) ? '' : '&note='+note);
        
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
            okCallBack(data);
        }); 
        
        //managin error
         addDiveSessionPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    }
    
     /*update a dive session*/
    this.updateDiveSession = function (externalPlatform, externalToken, deepUnit, tempUnit, weightUnit, divesessionId, diveDate, location, meteo, equipment, weight, temp, deep, note, okCallBack, errorCallBack)   {
        var updateDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionUpdate;
        
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&divesession_id='+divesessionId+
                        '&dive_date='+diveDate+
                        ((deepUnit == null) ? '' : '&deep_unit='+deepUnit)+
                        ((weightUnit == null) ? '' :'&weight_unit='+weightUnit)+
                        ((tempUnit == null) ? '' : "&temp_unit="+tempUnit)+
                        ((deep == null) ? '' :'&deep='+deep)+
                        ((equipment == null) ? '' : '&equipment='+equipment)+
                        ((location == null) ? '' : '&location='+location)+
                        ((meteo == null) ? '' : '&meteo='+meteo)+
                        ((temp == null) ? '' : '&water_temp='+temp)+
                        ((weight == null) ? '' : '&weight='+weight)+
                        ((note == null) ? '' : '&note='+note);
        
        $log.info('freediverService.updateDiveSession executing:'+updateDiveSessionUrl);
        $log.info('Params:'+ dataParam);
        
        var updateDiveSessionPromiseResponse = $http({method: 'POST',
                                                    url: updateDiveSessionUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
        updateDiveSessionPromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data);
        }); 
        
        //managin error
         updateDiveSessionPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    } 
    
	
	
	
	/*remove dive session service*/
	this.removeDiveSession = function (externalPlatform, externalToken, divesessionId, okCallBack, errorCallBack) {
		var removeDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionRemove;
       
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&divesession_id='+divesessionId;
        $log.info('freediverService.removeDiveSession executing:'+removeDiveSessionUrl);
        $log.info('Params:'+ dataParam);
        
        var removeDiveSessionPromiseResponse = $http({method: 'POST',
                                                    url: removeDiveSessionUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
       removeDiveSessionPromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data, divesessionId);
        }); 
        
        //managin error
        removeDiveSessionPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
         
	};
    
    /*facebook publish dive session service*/
	this.publishDiveSession = function (externalPlatform, externalToken, divesessionId, userName, location, maxDepth, maxDuration, okCallBack, errorCallBack) {
		var publishDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionPublish;
       
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&divesession_id='+divesessionId+
                        '&userName='+userName+
                        '&location='+location+
                        '&maxDepth='+maxDepth+
                        '&maxDuration='+maxDuration;
        
        $log.info('freediverService.publishDiveSession executing:'+publishDiveSessionUrl);
        $log.info('Params:'+ dataParam);
        
        var publishDiveSessionPromiseResponse = $http({method: 'POST',
                                                    url: publishDiveSessionUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
       publishDiveSessionPromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data, divesessionId);
        }); 
        
        //managin error
       publishDiveSessionPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
         
	};
    
    /*get detailed dive session*/
    this.getDetailDiveSession = function (externalPlatform, externalToken, divesessionId, okCallBack, errorCallBack) {
        var getDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveSessionGet +
						'?'+freedivingLogbookConstant.pNameExternalPlatform+'='+externalPlatform+
						'&'+freedivingLogbookConstant.pNameExternalToken+'='+externalToken+
                        '&'+freedivingLogbookConstant.pNameDiveSessionId+'='+divesessionId;
        
		$log.info('freediverService.getDetailDiveSession executing:'+getDiveSessionUrl);
		
		
		
		var getDiveSessionPromiseResponse = $http({method: 'GET', url: getDiveSessionUrl}); 
		
		
	    //managing success response
		getDiveSessionPromiseResponse.success(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	okCallBack(data);
	    });
	    
	    //managing error response
		getDiveSessionPromiseResponse.error(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	errorCallBack(data);
	    });
    };
    
    
     /*get detailed published dive session*/
    this.getDetailPublishedDiveSession = function (divesessionId, okCallBack, errorCallBack) {
        var getPublishedDiveSessionUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiPublishedDiveSessionGet +
						'?'+freedivingLogbookConstant.pNameDiveSessionId+'='+divesessionId;
        
		$log.info('freediverService.getDetailPublishedDiveSession executing:'+getPublishedDiveSessionUrl);
		
		
		
		var getPublishedDiveSessionPromiseResponse = $http({method: 'GET', url: getPublishedDiveSessionUrl}); 
		
		
	    //managing success response
		getPublishedDiveSessionPromiseResponse.success(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	okCallBack(data);
	    });
	    
	    //managing error response
		getPublishedDiveSessionPromiseResponse.error(function(data, status, headers, config) {
	    	console.log(data);
	    	//invoking callback function
	    	errorCallBack(data);
	    });
    };
    
    
    /*add a dive*/
    this.addDive = function (divesessionId, externalPlatform, externalToken, deepUnit, tempUnit, weightUnit, diveTime, duration, equipment, weight, temp, deep, neutralBuoyance, note, diveType, customFieldDiveMap, okCallBack, errorCallBack) {
        var addDiveUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveAdd;
         //iterate on customFieldDiveMap
        var customFieldParams='';
        if (customFieldDiveMap != null) {
               
               for (var key in customFieldDiveMap) {
                    customFieldParams = customFieldParams +'&'+key+'='+customFieldDiveMap[key];
                    $log.info(customFieldParams); 
                }
        }
       
            
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&divesession_id='+divesessionId+
                        '&dive_time='+diveTime+
                        ((deepUnit == null) ? '' : '&deep_unit='+deepUnit)+
                        ((weightUnit == null) ? '' :'&weight_unit='+weightUnit)+
                        ((tempUnit == null) ? '' : "&temp_unit="+tempUnit)+
                        ((deep == null) ? '' :'&max_depth='+deep)+
                        ((duration == null) ? '' :'&duration='+duration)+
                        ((neutralBuoyance == null) ? '' : '&neutral_buoyance='+neutralBuoyance)+
                        ((equipment == null) ? '' : '&equipment='+equipment)+
                        ((temp == null) ? '' : '&depth_water_temp='+temp)+
                        ((weight == null) ? '' : '&weight='+weight)+
                        ((diveType == null) ? '' : '&dive_type='+diveType)+
                        ((note == null) ? '' : '&note='+note)+
                        customFieldParams;
        
        $log.info('freediverService.addDive executing:'+addDiveUrl);
        $log.info('Params:'+ dataParam);
        
        var addDivePromiseResponse = $http({method: 'POST',
                                                    url: addDiveUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
        addDivePromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data);
        }); 
        
        //managin error
         addDivePromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    };
    
    
    
    /*update a dive*/
    this.updateDive = function (diveId, externalPlatform, externalToken, deepUnit, tempUnit, weightUnit, diveTime, duration, equipment, weight, temp, deep, neutralBuoyance, note, diveType, customFieldDiveMap, okCallBack, errorCallBack) {
        var updateDiveUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveUpdate;
        
         //iterate on customFieldDiveMap
        var customFieldParams='';
        if (customFieldDiveMap != null) {
               
               for (var key in customFieldDiveMap) {
                    customFieldParams = customFieldParams +'&'+key+'='+customFieldDiveMap[key];
                    $log.info(customFieldParams); 
                }
        }
        
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&dive_id='+diveId+
                        '&dive_time='+diveTime+
                        ((deepUnit == null) ? '' : '&deep_unit='+deepUnit)+
                        ((weightUnit == null) ? '' :'&weight_unit='+weightUnit)+
                        ((tempUnit == null) ? '' : "&temp_unit="+tempUnit)+
                        ((deep == null) ? '' :'&max_depth='+deep)+
                        ((duration == null) ? '' :'&duration='+duration)+
                        ((neutralBuoyance == null) ? '' : '&neutral_buoyance='+neutralBuoyance)+
                        ((equipment == null) ? '' : '&equipment='+equipment)+
                        ((temp == null) ? '' : '&depth_water_temp='+temp)+
                        ((weight == null) ? '' : '&weight='+weight)+
                        ((diveType == null) ? '' : '&dive_type='+diveType)+
                        ((note == null) ? '' : '&note='+note)+
                        customFieldParams;
        
        $log.info('freediverService.updateDive executing:'+updateDiveUrl);
        $log.info('Params:'+ dataParam);
        
        var updateDivePromiseResponse = $http({method: 'POST',
                                                    url: updateDiveUrl,
                                                    data: dataParam,
                                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                                  });
        
        //managing success
        updateDivePromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data);
        }); 
        
        //managin error
         updateDivePromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    };
    
    /*remove a dive*/
    this.removeDive = function(diveId, externalPlatform, externalToken, okCallBack, errorCallBack) {
        var removeDiveUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiDiveRemove;
        
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&dive_id='+diveId;
        
        $log.info('freediverService.removeDive executing:'+removeDiveUrl);
        $log.info('Params:'+ dataParam);
        
        var removeDivePromiseResponse = $http({method: 'POST',
                                               url: removeDiveUrl,
                                               data: dataParam,
                                               headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                            });
        
        //managing success
        removeDivePromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data, diveId);
        }); 
        
        //managin error
        removeDivePromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    };
    
    
     /*update freediver settings*/
    this.updateFreediverSetting = function(freediverId, externalPlatform, externalToken, customFieldListOfDiveSession, okCallBack, errorCallBack) {
        var updateFreediverSettingUrl = freedivingLogbookConstant.apiHostName+freedivingLogbookConstant.apiUpdateFreediverSetting;
        
        var dataParam = 'external_platform_id='+externalPlatform+
                        '&external_token='+externalToken+
                        '&freediver_id='+freediverId;
        
        /*add custom field list of dive*/
        for (var i = 0; i < customFieldListOfDiveSession.length; i++){
            dataParam = dataParam +'&custom_field_of_dive='+customFieldListOfDiveSession[i];
        }
        $log.info('freediverService.updateFreediverSetting executing:'+updateFreediverSettingUrl);
        $log.info('Params:'+ dataParam);
        
        var updateFreediverSettingPromiseResponse = $http({method: 'POST',
                                               url: updateFreediverSettingUrl,
                                               data: dataParam,
                                               headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                                            });
        
        //managing success
        updateFreediverSettingPromiseResponse.success(function(data, status, headers, config) {
            $log.info(data);
            okCallBack(data);
        }); 
        
        //managin error
        updateFreediverSettingPromiseResponse.error(function(data, status, headers, config) {
            $log.info(data);
            errorCallBack(data);
        }); 
    
    };
    
});