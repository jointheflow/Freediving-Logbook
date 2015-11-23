//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.service');

/*add model services*/
appNeaClientService.service('modelService', function ($log) {
	//create an instance reference to the freediver model
    this.freediverMdl= new FreediverMdl();
    
    //populate model from login rest api data
    this.updateFreediver = function(data) {
        //populate freedive
        this.freediverMdl.externalId = data.detail.externalId;
        this.freediverMdl.id = data.detail.id;
        this.freediverMdl.externalToken = data.externalToken;
        this.freediverMdl.externalPlatformId = data.externalPlatformId;
        this.freediverMdl.depthUnit = data.deepUnit;
        this.freediverMdl.tempUnit = data.tempUnit;
        this.freediverMdl.weightUnit = data.weightUnit;
        //if there are some dive session update the model with each one
        if (data.detail.diveSessions != null) {
            divesessions = data.detail.diveSessions;
            for (i = 0; i < divesessions.length; i++) { 
                this.addOrUpdateDiveSession(divesessions[i]);
                
            }
        
        
    };
    
    //update or add divesessions
    this.addOrUpdateDiveSession = function (data) {
        //regarding the current model, if divesession exists update else add
        //create an istance of dive session
        ds = new DiveSessionMdl();
        ds.id = data.id;
        ds.diveDate = data.diveDate;
        //set all attributes......
        
        //invokes model method to update
        this.freediverMdl.addOrUpdateDiveSession(ds);
        
        
        
    }; 
    
    
    this.addOrUpdateDive = function (data) {
        //regarding the current model, if dive exists update else add
    };
    
    //
    this.removeDiveSession = function (divesessionid) {
    
    };
    
    
    this.removeDive = function (diveid) {
    
    };
    

});
                            