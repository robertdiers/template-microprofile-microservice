package template.template_microservice.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import template.template_microservice.tasks.TimerStore;

/***
 * StartupHandler
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
@ApplicationScoped
public class StartupHandler {
	
	//sometimes this is executing twice, we have to check this
  	private boolean initializedalreadyexecuted = false;	
  	private boolean destroyedalreadyexecuted = false;	   
      
  	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
  	
  		if (!initializedalreadyexecuted) {
	    	initializedalreadyexecuted = true;	    	
	    		    			
	    	//read config
	    	SimpleConfiguration.getInstance();
	    	
	    	//schedule timer tasks
	    	TimerStore.getInstance().scheduleTasks();
	    	
	    	//something to do for startup?
	    	
	    	ApplicationLogger.log("StartupHandler: applicationStartup done.");   
      	}
    }
   
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {    	
      	
      	if (!destroyedalreadyexecuted) {
      		destroyedalreadyexecuted = true;    
      		
      		//something to do before shutdown?
	    	
	    	ApplicationLogger.log("StartupHandler: applicationShutdown done.");  	        
	    	
	    	//close the logger
	    	ApplicationLogger.forceInit();  
      	}
    }

}
