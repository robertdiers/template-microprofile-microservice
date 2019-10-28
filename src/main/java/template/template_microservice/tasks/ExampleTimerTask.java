package template.template_microservice.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import template.template_microservice.util.ApplicationLogger;
import template.template_microservice.util.SimpleConfiguration;

/**
 * example task
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class ExampleTimerTask extends TimerTask {			
	
	public ExampleTimerTask() {
		//you might want to use constructor to use CDI (@Inject might not work here)
	}
	
	public void run() {
		try {			
	    	if (TimerStore.getInstance().isJobserver() && SimpleConfiguration.getPropertyWithoutNull(this.getClass().getSimpleName()).equalsIgnoreCase("true")) {
	    		ApplicationLogger.log(this.getClass().getSimpleName()+" executed!");
	    	}	    	
    	} catch (Exception e) {
    		ApplicationLogger.log(e);
    	} finally {
    		//reschedule
	    	TimerStore.getInstance().scheduleTimerTask(new ExampleTimerTask(), getNextExecution());
    	}
    }
	
//	/**
//	 * tomorrow 4 o'clock
//	 * @return
//	 */
//	public Date getNextExecution() {
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.HOUR_OF_DAY, 4);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		cal.add(Calendar.DAY_OF_YEAR, 1);
//		return cal.getTime();
//	}

	/**
	 * 60 seconds
	 * @return
	 */
	public Date getNextExecution() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 60);
		return cal.getTime();
	}
   
}