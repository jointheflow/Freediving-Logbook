/*controller definition */
appPublishedClient.controller('publicationFbController',  function ($scope, modelService, freediverService, $log) {
    
    
    $scope.$watch('divesession_id', function () {
        $log.info("divesession_id received from jsp="+$scope.divesession_id); 
    });
            
});