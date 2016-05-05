<!--This page uses mixed code ("accrocco") jsp and javascript to generate dynamic result.
The jsp code is used to rendering meta property in the HEAD for facebook in order to show story preview in the users log.
The javascript code is used to rendering in the BODY the user depth graph when a user click on the user story from facebook 
The page can receive the following parameters in query string:
*****divesessionID
*****location
*****maxDepth
*****maxDuration
*****userName-->

<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!--init the divesessionid by parameter during jsp compilation-->
<html ng-app="appPublishedClient" ng-controller="publicationFbController" ng-init="divesession_id='${param.divesessionID}'">
    <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# org_appnea: http://ogp.me/ns/fb/org_appnea#">
        
        <title>appnea, a social apnea logbook for freediver </title>
        <meta property="fb:app_id" content="132053467142365" />
        <meta property="og:title" content="Dive Session @${param.location}" />
        <meta property="og:url" content="https://app-nea-it.appspot.com/webclient/published_dive_session.jsp?divesessionID=${param.divesessionID}&userName=${param.userName}&location=${param.location}&maxDepth=${param.maxDepth}&maxDuration=${param.maxDuration}" />
        <meta property="og:type" content="org_appnea:dive_session" />
        <!--change value according the query parameter-->
        <meta property="og:description" content="max dive depth: ${param.maxDepth}, max dive duration: ${param.maxDuration}" />
       
        
        <!--link rel="canonical" href="https://app-nea-it.appspot.com/" /-->
        
        <meta name="viewport" content="width=device-width" content="initial-scale=1" />
     
        <!-- app-nea custom css -->
        <link rel="stylesheet" href="src/shared/custom.css">
        
       <!-- Angular Material CSS now available via Google CDN; version 0.11.2 used here -->
        <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/1.0.0/angular-material.min.css">
        
        <!-- material icon -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

        <!-- Angular Material Dependencies -->
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.6/angular.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.6/angular-route.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.6/angular-animate.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.6/angular-aria.min.js"></script>

         <!-- Angular Material Javascript now available via Google CDN; version 0.11.2 used here -->
        <script src="https://ajax.googleapis.com/ajax/libs/angular_material/1.0.0/angular-material.min.js"></script>
        
        
        <!--Zing chart dependency-->
        <script src="assets/libs/zing-chart/zingchart.min.js"></script>
        <script src="assets/libs/zing-chart/zingchart-angularjs.js"></script>
        
        <!-- app-nea source code reference -->    
        <script src="src/appPublishedDiveSession.js"></script> 
        <script src="src/shared/constant.js"></script>
        <script src="src/shared/helper.js"></script>
        <script src="src/shared/service/freediver_service.js"></script>
        <script src="src/shared/service/model_service.js"></script>
        <script src="src/shared/model/model.js"></script>
        <script src="src/fbpublication/fb_publication.js"></script>

    </head>
      
    <body>
                
        <div layout="column" layout-fill>
        <!-- toobar -->
            <md-toolbar>
                <div class="md-toolbar-tools" layout-align="center end">
                    <p>{{applicationName}}</p>
                    <!--fill right space with a blank div -->
                    <div style="height:48px; width:48px"></div>
                </div>                    
            </md-toolbar>
            <!-- show only if there are dive sessions -->
       <!-- show only if there are dive sessions -->
            <md-content  layout-margin>
                <div layout="column" layout-align="center">
                    <div layout="column">
                        <table>
                            <tr>
                                <label><b>Freediver</b>: ${param.userName}</label>
                            </tr>
                            <tr>
                                <label><b>Location</b>: ${param.location}</label>
                            </tr>
                        </table>
                    </div>
                            <div layout="column" layout-align="center center"> 
                                <zingchart style="display:block" id="myChart" zc-json="myJson" zc-height="400" zc-width="100%"></zingchart>
                            </div>
                            
                        
                    <div layout="column" layout-align="center center">
                        <table>    
                            <tr>
                                <th>Max</th>
                                <th>Average</th>
                            </tr>
                            <tr>    
                                <td><label>Depth = <i>{{divesession.getMaxDiveDepth(0)}}</i></label></td>
                                <td><label>Depth = <i>{{divesession.getAvgDiveDepth(0)}}</i></label></td>
                            </tr> 
                            <tr>
                                <td><label>Duration = <i>{{divesession.getMaxDiveDuration()}}</i></label></td>
                                <td><label>Duration = <i>{{divesession.getAvgDiveDuration()}}</i></label></td>
                            </tr>
                            
                        </table>
                    </div>
                    <div layout="column" layout-align="center center">
                        <table>
                            <tr><p></p></tr>
                            <tr><a ng-href="https://app-nea-it.appspot.com">GO to APP-NEA!</a></tr>    
                        </table>
                    </div>
                </div>
            </md-content>
        </div>
                 
    </body>
    
    <!--Google analytics settings-->
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', 'UA-71882773-1', 'auto');
        ga('send', 'pageview');
    </script>
</html>