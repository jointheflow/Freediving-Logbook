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
        
        // Defines a route for the URLs requests  
         
        router.attach("/freediver/login", org.gianluca.logbook.rest.resource.FreediverLoginResource.class);
        router.attach("/freediver/remove", org.gianluca.logbook.rest.resource.FreediverRemoveResource.class);
        router.attach("/freediver/get", org.gianluca.logbook.rest.resource.FreediverGetResource.class);
        router.attach("/freediver/divesession/add", org.gianluca.logbook.rest.resource.DiveSessionAddResource.class);
        router.attach("/freediver/divesession/update", org.gianluca.logbook.rest.resource.DiveSessionUpdateResource.class);
        router.attach("/freediver/divesession/remove", org.gianluca.logbook.rest.resource.DiveSessionRemoveResource.class);
        router.attach("/freediver/divesession/dive/add", org.gianluca.logbook.rest.resource.DiveAddResource.class);
        router.attach("/freediver/divesession/dive/update", org.gianluca.logbook.rest.resource.DiveUpdateResource.class);
        router.attach("/freediver/divesession/dive/remove", org.gianluca.logbook.rest.resource.DiveRemoveResource.class);
        return router;

       
    }
}
