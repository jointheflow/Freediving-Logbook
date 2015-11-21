//get reference to the service module defined in app.js
var appNeaClientService = angular.module('appNeaClient.service');

/*add model services*/
appNeaClientService.service('modelService', function ($log) {
	//create an instance reference to the freediver model
    this.freediverMdl= new FreediverMdl();
    
    

});
                            