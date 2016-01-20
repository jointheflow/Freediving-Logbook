    
/*controller definition */
appNeaClient.controller('sidenavController',  function ($scope,$rootScope, modelService, $log, $mdSidenav) {
    
    $scope.freediver = modelService.freediverMdl;
    
    //toggle left side menu  
    $scope.openLeftMenu = function() {
        $mdSidenav('left').toggle();
    };
    
    //manage logout on fb but we let fb manage logout directly +/
    $scope.logout = function() {
        $rootScope.logout();
        $mdSidenav('left').close();
    };
    
});