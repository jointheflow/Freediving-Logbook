/*controller definition */
appPublishedClient.controller('publicationFbController',  function ($scope, modelService, freediverService, $log, $filter, $timeout) {
    
    $scope.applicationName = freedivingLogbookConstant.applicationName;
    $scope.myJson = null;
    $scope.$watch('divesession_id', function () {
        if ($scope.divesession_id !=null) {
            $log.info("divesession_id received from jsp="+$scope.divesession_id); 
            //invoking dive session retrieve using divesession_id
            freediverService.getDetailPublishedDiveSession(
                                                      $scope.divesession_id,
                                                      $scope.onGetDiveSessionSuccess,
                                                      $scope.onGetDiveSessionError);
        }else
            $log.info("divesession_id received from jsp is NULL");

    });
    
    
    
    
    /*manages get detail dive session success*/
	$scope.onGetDiveSessionSuccess = function(data) {
        
		//update model with detailed dive session
        modelService.addOrUpdateDiveSessionFromData(data.detail,
                                                    data.deepUnit,
                                                    data.weightUnit,
                                                    data.tempUnit);
        //modelService.addOrUpdateDiveSessionFromData(data);
        $scope.divesession = modelService.freediverMdl.currentDiveSession;
        $scope.divesession.dives = $filter('orderBy')(modelService.freediverMdl.currentDiveSession.dives, 'diveTime', false);
        $log.info("model instantiated....creating zing chart");
        $timeout(
                $scope.createZingChart, 3000);
        
		
	}; 
    
    
   /*manages get detail dive session error*/
	$scope.onGetDiveSessionError = function(data) {
        //TODO manage the error
		
	}; 
    
    
    
    /******ZING CHART MANAGEMENT START******/
    /*Populate an array of duration in the following form [d1, d2, d3....]*/
    $scope.populateDurationArray = function () {
        yChart = []; 
        for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
                yChart.push(modelService.freediverMdl.currentDiveSession.dives[i].getDuration());
        } 
        return yChart;
    };
    
    /*populate a series of the following element [[i, depth_i, duration_i], [y, depth_y, duraion_y]...]*/
    $scope.populateDepthAndDurationBubbleChartSerie = function() {
        bubbleSeries = [];
        for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
                        seriesElement = [];
                        seriesElement.push(i);
                seriesElement.push(modelService.freediverMdl.currentDiveSession.dives[i].maxDepth);
                seriesElement.push(modelService.freediverMdl.currentDiveSession.dives[i].duration)
                        $log.info("Series element "+i+" created:"+seriesElement);
                        bubbleSeries.push(seriesElement);
        } 
        return bubbleSeries;
    };
    /*populate label chart (time of dive) for x axis*/
    $scope.populateLabelXAxisBubbleChart = function() {
        xAxis= []; 
        for (var i = 0; i < modelService.freediverMdl.currentDiveSession.dives.length; i++){
            xAxis.push(modelService.freediverMdl.currentDiveSession.dives[i].getTimeHHMM());
        } 
        return xAxis;
        
    };
        
    $scope.createZingChart = function() {
        /*loading statistic graph if there is a valid JSON object to rendering */  
        if ((modelService.freediverMdl.currentDiveSession != null) &&       (modelService.freediverMdl.currentDiveSession.dives.length) > 0) {
            $scope.myJson = {
                    "type":"bubble",
                    "plot":{
                        "value-box":{
                            "text": "%data-timeHHMM",  //Use the %data-timeHHMM token to display bubble size (formatted duration).
                            "font-color":"white",
                            "font-size":10
                        },
                        "tooltip":{
                            "text":"%v meters "
                        },
                        //show different colors for depth
                        "marker":{
                            "rules":[
                                {"rule":"%v < 10",
                                 "background-color":"#ccd8ff"
                                },
                                 {"rule":"%v >= 10 && %v < 20",
                                 "background-color":"#668aff"
                                },
                                {"rule":"%v >= 20 && %v < 30",
                                 "background-color":"#003cff"
                                },
                                 {"rule":"%v >= 30 && %v < 40",
                                 "background-color":"#002ab3"
                                },
                                 {"rule":"%v >= 40 && %v < 60",
                                 "background-color":"#001866"
                                },
                                {"rule":"%v >= 60",
                                 "background-color":"#00061a"
                                }
                            ]
                        }
                    },
                    "title":{
                        "text":"Depth vs Duration",
                        "font-size":12
                    },
                    "scale-x":{
                        "labels": $scope.populateLabelXAxisBubbleChart()
                    },
                    "scale-y":{
                        //define max limit adding 30 meters to the y axis
                        "values":"0:"+Math.floor($scope.divesession.getMaxDiveDepth(0)+30)+":5",
                        "label": {
                                "text":"meters"
                                }
                    },
                    "series":[
                        {
                            "values": $scope.populateDepthAndDurationBubbleChartSerie(),
                            "data-timeHHMM": $scope.populateDurationArray(),
                            "scaling": "radius", 
                            "scales":"scale-x,scale-y",
                            "size-factor": 0.25,
                            //"aspect": "spline",
                            "text": "Depth chart"
                        }

                    ]
            };
        }else
            {$scope.myJson=null;   
            };
    }
    /********ZING CHART MANAGEMENT END*********/
            
});