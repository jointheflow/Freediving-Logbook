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
                this.addOrUpdateDiveSessionFromData(divesessions[i]);
        
            }
        }
        
    };
    
    //update or add divesessions
    this.addOrUpdateDiveSessionFromData = function (data) {
        //regarding the current model, if divesession exists update else add
        //create an istance of dive session
        ds = new DiveSessionMdl();
        //set all attributes.....
        ds.id = data.id;
        ds.diveDate = new Date(data.diveDate);
        ds.location = data.locationDesc;
        ds.depthAsMeter = data.deepAsMeter;
        ds.depthAsFeet = data.deepAsFeet;
        ds.meteo = data.meteoDesc;
        ds.equipment = data.equipment;
        ds.weightAsKilogram = data.weightAsKilogram;
        ds.weightAsPound = data.weightAsPound
        ds.tempAsCelsius = data.waterTempAsCelsius;
        ds.tempAsFarheneit = data.waterTempAsFahrehneit;
        ds.note = data.note;
            
        
        //invokes model method to update
        this.freediverMdl.addOrUpdateDiveSession(ds);
        
        
        
    }; 
    
    
    this.addOrUpdateDiveFromData = function (data) {
        //regarding the current model, if dive exists update else add
    };

});
                            