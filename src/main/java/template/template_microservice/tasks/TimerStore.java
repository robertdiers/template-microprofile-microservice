package template.template_microservice.tasks;

import java.net.InetAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import template.template_microservice.util.ApplicationLogger;
import template.template_microservice.util.SimpleConfiguration;

/**
 * Timer Store Singleton
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class TimerStore {
	
	private static TimerStore instance = new TimerStore();
	private Timer timer = new Timer();
	
	private TimerStore() {}
	
	public static TimerStore getInstance() {
		return instance;
	}
	
	/**
	 * schedule all tasks
	 */
	public void scheduleTasks() {
		try {			
			if (isJobserver()) {			
				
				//schedule timer tasks here
				ExampleTimerTask prt = new ExampleTimerTask();
				scheduleTimerTask(prt, prt.getNextExecution());				
				
			}
		} catch (Exception e) {
			ApplicationLogger.log(e);
    	}
	}
	
	/**
	 * check if we are running the job server
	 * @return
	 */
	public boolean isJobserver() {
		try {
			String jobserver = SimpleConfiguration.getPropertyWithoutNull("jobserver");
			String hostname = InetAddress.getLocalHost().getHostName();
			
			if (jobserver.equalsIgnoreCase("localhost") || (jobserver.length() > 0 && hostname.contains(jobserver))) {
				return true;
			}
			
			return false;
		} catch (Exception e) {
			ApplicationLogger.log(e);
    		return false;
    	}
	}
	
	/**
	 * cancel all tasks
	 */
	public void cancelTasks() {
		try {
			//cancel all planned timer tasks
			timer.cancel();
			ApplicationLogger.log(ApplicationLogger.WARNING, "TimerStore: timer canceled");
		} catch (Exception e) {
			ApplicationLogger.log(e);
    	}
	}
	
	/**
	 * schedule one-time-execution
	 * @param task
	 * @param date
	 */
	public void scheduleTimerTask(TimerTask task, Date date) {
		try {
			timer.schedule(task, date);
			ApplicationLogger.log(ApplicationLogger.WARNING, "TimerStore: " + task.getClass().getSimpleName() + " scheduled at " + date);
		} catch (Exception e) {
			ApplicationLogger.log(e);
    	}
	}
	
	/**
	 * schedule interval
	 * @param task
	 * @param date
	 * @param period
	 */
	public void scheduleAtFixedRate(TimerTask task, Date date, long period) {
		try {
			timer.scheduleAtFixedRate(task, date, period);
			ApplicationLogger.log(ApplicationLogger.WARNING, "TimerStore: " + task.getClass().getSimpleName() + " scheduled at " + date + " every "+(period/1000)+" sec");
		} catch (Exception e) {
			ApplicationLogger.log(e);
    	}
	}

}