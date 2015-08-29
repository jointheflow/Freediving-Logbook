package org.gianluca.logbook.rest.application;



import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class LogbookApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
      
    	// Create a router Restlet that routes each call to a
        // new instance resources.
        Router router = new Router(getContext());
        
        // Defines a route for the resource Customer  
         
        router.attach("/freediver/login", org.gianluca.logbook.rest.resource.FreediverLoginResource.class);
   
        return router;

       
    }
}
