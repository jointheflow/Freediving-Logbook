//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.service');

/*add model services*/
appNeaClientService.service('modelService', function ($log) {
	//create an instance reference to the freediver model
    this.freediverMdl= new FreediverMdl();
    
    
    //populate model from login rest api data
    this.updateFreediverFromData = function(data) {
        //populate freedive
        this.freediverMdl.externalId = data.detail.externalId;
        this.freediverMdl.id = data.detail.id;
        this.freediverMdl.externalToken = data.externalToken;
        this.freediverMdl.externalPlatformId = data.externalPlatformId;
        this.freediverMdl.depthUnit = data.deepUnit;
        this.freediverMdl.tempUnit = data.tempUnit;
        this.freediverMdl.weightUnit = data.weightUnit;
        this.freediverMdl.username = data.detail.externalUsername;
        //if there are some dive session update the model with each one
        if (data.detail.diveSessions != null) {
            divesessions = data.detail.diveSessions;
            for (i = 0; i < divesessions.length; i++) { 
                this.addOrUpdateDiveSessionFromData(divesessions[i], 
                                                    this.freediverMdl.tempUnit,
                                                    this.freediverMdl.weightUnit,
                                                    this.freediverMdl.depthUnit);
        
            }
        }
        
    };
    
    //update or add divesessions
    this.addOrUpdateDiveSessionFromData = function (data, _tempUnit, _weightUnit, _depthUnit) {
        //regarding the current model, if divesession exists update else add
        //create an istance of dive session
        ds = new DiveSessionMdl();
        //set all attributes.....
        ds.id = data.id;
        ds.diveDate = new Date(data.diveDate);
        ds.location = data.locationDesc;
        ds.meteo = data.meteoDesc;
        ds.equipment = data.equipment;
        ds.note = data.note;
        
        if (_depthUnit == freedivingLogbookConstant.DEEP_METER) ds.depth = data.deepAsMeter;
        if (_depthUnit == freedivingLogbookConstant.DEEP_FEET) ds.depth = data.deepAsFeet;
               
        if (_weightUnit == freedivingLogbookConstant.WEIGHT_KILOGRAM) ds.weight = data.weightAsKilogram;
        if (_weightUnit == freedivingLogbookConstant.WEIGHT_POUND) ds.weight = data.weightAsPound;
        
        if (_tempUnit == freedivingLogbookConstant.TEMPERATURE_CELSIUS) ds.temp = data.waterTempAsCelsius;
        if (_tempUnit == freedivingLogbookConstant.TEMPERATURE_FAHRHENEIT) ds.temp = data.waterTempAsFahrehneit;
        
        
        //get dives
        if (data.dives!=null) {
            dives = data.dives;
            for (i = 0; i < dives.length; i++) {
                //dive
                dive = this.addOrUpdateDiveFromData(dives[i], 
                                                    this.freediverMdl.tempUnit,
                                                    this.freediverMdl.weightUnit,
                                                    this.freediverMdl.depthUnit);
                ds.addOrUpdateDive(dive);
            } 
        }
        
        //invokes model method to update
        this.freediverMdl.addOrUpdateDiveSession(ds);
        
        //set the current dive session
        this.freediverMdl.currentDiveSession=ds;
        
        
        
    }; 
    
    
    this.addOrUpdateDiveFromData = function (data, _tempUnit, _weightUnit, _depthUnit) {
        //regarding the current model, if dive exists update else add
        d = new DiveMdl();
        
        d.id = data.id;
        d.diveTime = data.diveTime;
        d.equipment = data.equipment;
        d.duration = data.duration;
        d.diveType = data.diveType;
        d.note = data.note;
        
        if (_depthUnit == freedivingLogbookConstant.DEEP_METER) d.maxDepth = data.maxDeepAsMeter;
        if (_depthUnit == freedivingLogbookConstant.DEEP_FEET) d.maxDepth = data.maxDeepAsFeet;
        
        if (_depthUnit == freedivingLogbookConstant.DEEP_METER) d.neutralBuoyance = data.neutralBuoyanceAsMeter;
        if (_depthUnit == freedivingLogbookConstant.DEEP_FEET) d.neutralBuoyance = data.neutralBuoyanceAsFeet;
        
        if (_weightUnit == freedivingLogbookConstant.WEIGHT_KILOGRAM) d.weight = data.weightAsKilogram;
        if (_weightUnit == freedivingLogbookConstant.WEIGHT_POUND) d.weight = data.weightAsPound;
        
        if (_tempUnit == freedivingLogbookConstant.TEMPERATURE_CELSIUS) d.depthTemp = data.depthWaterTempAsCelsius;
        if (_tempUnit == freedivingLogbookConstant.TEMPERATURE_FAHRHENEIT) d.depthTemp = data.depthWaterTempAsFahrehneit;
        
        return d;
       
    };

});
                            