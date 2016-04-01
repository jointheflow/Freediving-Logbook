<!--This page uses mixed code ("accrocco") jsp and javascript to generate dynamic result.
The jsp code is used to rendering meta property in the HEAD for facebook in order to show story preview in the users log.
The javascript code is used to rendering in the BODY the user depth graph when a user click on the user story from facebook 
The page can receive the following parameters in query string:
divesessionID
location
maxDepth
maxDuration-->

<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<html ng-app="appPublishedClient">
    <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# org_test_appnea: http://ogp.me/ns/fb/org_test_appnea#">
        
        <title>appnea, a social logbook for freediver </title>
        <meta property="fb:app_id" content="164570873890624" />
        <meta property="og:title" content="Sample Dive Session" />
        <meta property="og:url" content="https://app-nea-it.appspot.com/webclient/published_dive_session_test.jsp" />
        <meta property="og:type" content="org_test_appnea:dive_session" />
        <!--TODO: change value according the parameter-->
        <meta property="og:description" content="Dive session @${param.location}, max dive depth: ${param.maxDepth}, max dive duration: ${param.maxDuration}" />
       
        
        <link rel="canonical" href="https://app-nea-it.appspot.com/" />
        
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
        
        
        <!-- Angular moment-->
        <script src="assets/libs/moment/moment-with-locales.js"></script>
        
         <!--ngFacebook dependecy -->
        <script src="assets/libs/ng-facebook/ngFacebook.js"></script>
        
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
    <!--TODO init the divesessionid by parameter during jsp compilation-->  
    <body ng-controller="publicationFbController" ng-init="divesession_id=${param.divesessionID}" >
                
        <div layout="column" flex>
            <p> test</p>
            <md-content>
                <p>Lorem ipsum dolor sit amet, ne quod novum mei. Sea omnium invenire mediocrem at, in lobortis conclusionemque nam. Ne deleniti appetere reprimique pro, inani labitur disputationi te sed. At vix sale omnesque, id pro labitur reformidans accommodare, cum labores honestatis eu. Nec quem lucilius in, eam praesent reformidans no. Sed laudem aliquam ne.</p>
                <p>Facete delenit argumentum cum at. Pro rebum nostrum contentiones ad. Mel exerci tritani maiorum at, mea te audire phaedrum, mel et nibh aliquam. Malis causae equidem vel eu. Noster melius vis ea, duis alterum oporteat ea sea. Per cu vide munere fierent.
                </p>
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