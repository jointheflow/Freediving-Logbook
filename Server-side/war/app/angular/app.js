/*Creates all application module*/
//freedivingLogbookModule is the main module that represents the app. It needs all other modules dependencies
var freedivingLogbookModule = angular.module('freedivingLogbookModule', ['servicesModule', 'controllersModule']);
//servicesModule contains all services
var servicesModule = angular.module('servicesModule', []);
//controllersModule contains all controllers
var controllersModule = angular.module('controllersModule', []);

