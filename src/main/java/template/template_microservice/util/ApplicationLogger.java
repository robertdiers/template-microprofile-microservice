package template.template_microservice.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * simple application logger with auto-deletion
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public final class ApplicationLogger {
	
	private final static String NODELETE = "NODELETE";
	private final static long fONCE_PER_DAY = (long)1000*60*60*24;
	private final static int WEEKS_TO_KEEP = 1;	
	
	public static final int INFO = 1;	
	public static final int WARNING = 2;	
	public static final int SEVERE = 3;	
	
	private static boolean duringInitialization = false;
	private static FileWriter fileWritter = null;
	private static BufferedWriter logger = null;
	private static int level = 3;
	private static boolean taskScheduled = false;

	private ApplicationLogger () {}	
	
	/**
	 * new logfile every day
	 * @author robert.diers
	 *
	 */
	public static class NewLogfileTask extends TimerTask {			
		public void run() { 
			try{				
				//force init
				ApplicationLogger.forceInit();				
				ApplicationLogger.log(ApplicationLogger.WARNING, "NewLogfileTask executed.");
			} catch (Exception e)
			{				
				ApplicationLogger.log(e, "NewLogfileTask:");
			}
	    }  	   
	}
	
	/**
	 * delete logs older than 2 weeks
	 * @author robert.diers
	 *
	 */
	public static class DeleteLogfileTask extends TimerTask {	
		
		/**
		 * run
		 */
		public void run() {
			try{
				//sleep because of Cluster (max 10 sec)
				java.security.SecureRandom sr = new java.security.SecureRandom();
	        	int zufall = sr.nextInt(10) * 1000;	        	
	        	Thread.sleep(zufall);	    		
				//get log folder	        	
	        	String logfolder = SimpleConfiguration.getPropertyWithoutNull("logfolder");
	        	delete(logfolder, getTimeWeeksAgo(WEEKS_TO_KEEP));
				ApplicationLogger.log(ApplicationLogger.WARNING, "DeleteLogfileTask executed.");
			} catch (Exception e)
			{				
				ApplicationLogger.log(e, "DeleteLogfileTask:");
			}
	    }  
		
		/**
		 * delete log files in folder by age
		 * @param actual_folder
		 * @param time
		 */
		private void delete(String actual_folder, long time)
	    {    	
	    	long deleted_files = deleteOlderFilesFromFolder(actual_folder, time);
	    	ApplicationLogger.log("DeleteTask: " + actual_folder + " files deleted: " + deleted_files);
	    }	    
		
		/**
		 * get date of weeks ago
		 * @param weeks
		 * @return
		 */
	    private long getTimeWeeksAgo(int weeks)
	    {
	    	Calendar date = new GregorianCalendar();
	    	date.add(Calendar.WEEK_OF_YEAR, -weeks);
		    return date.getTime().getTime();
	    }
	    
	    /**
	     * clean folder recursive
	     * @param folder
	     * @param oldestTime
	     * @return
	     */
	    private long deleteOlderFilesFromFolder(String folder, long oldestTime)    
	    {	   
	    	//false positive vulnerability as folder is configured and no user input
	    	File fDir = new File(folder);
	    	
	    	ApplicationLogger.log("DeleteTask: checking " + fDir.getAbsolutePath());
	    	long deletedfiles = 0;
	    	
	    	File[] files = fDir.listFiles();
	    	if (files == null) { 
	    		//not existent
	    		ApplicationLogger.log("DeleteLogfileTask: " + fDir.getPath() + " is not existing or is not a folder");
	    		return 0;
	    	} 
	    	
	    	//check every file
    		for (int i=0; i<files.length; i++) { 
    			
    			File temp = files[i];
    			
    			if (temp.isDirectory())	{
    				//search in all subfolders
    				if (!temp.getPath().contains("tx"))
    					deletedfiles += deleteOlderFilesFromFolder(temp.getPath(), oldestTime);    
    				continue;
    			}   			
				
				//check timestamp    			
    			if (temp.lastModified() < oldestTime && !temp.getPath().contains(NODELETE)) {
    				//to old --> delete
    				if (deleteFile(temp)) deletedfiles++;    				 				
    			}				
	    		
	    	}	
    		
	    	//files deleted?
	    	return deletedfiles;
	    }
	}
	
	/**
	 * delete file
	 * @param temp
	 * @return
	 */
	private static boolean deleteFile(File temp) {
		try {
			return temp.delete();
		} catch (Exception e) {
			ApplicationLogger.log("DeleteLogfileTask: " + temp.getPath() + " could not be deleted (" + e.getMessage() + ")");
			return false;
		}  
	}
	
	/**
	 * init the logger object
	 */
	private static void init() {
		try{
			//start
			duringInitialization = true;
			
			if (!taskScheduled) {
				//schedule new File every day
				Timer timer = new Timer();
				TimerTask loginittask  = new NewLogfileTask();			
				timer.scheduleAtFixedRate(loginittask, getNextDate(0,0), (fONCE_PER_DAY));
				ApplicationLogger.log(ApplicationLogger.WARNING, "ApplicationLogger: NewLogfileTask scheduled every day");
				TimerTask logdeltask  = new DeleteLogfileTask();			
				timer.scheduleAtFixedRate(logdeltask, getNextDate(0,5), (fONCE_PER_DAY));
				ApplicationLogger.log(ApplicationLogger.WARNING, "ApplicationLogger: DeleteLogfileTask scheduled every day");
				taskScheduled = true;
			}
			
			//read configuration - ATTENTION: it's using this Logger
			String loglevel = SimpleConfiguration.getPropertyWithoutNull("loglevel");
			String logfile = SimpleConfiguration.getPropertyWithoutNull("logfile");	
			String logfolder = SimpleConfiguration.getPropertyWithoutNull("logfolder");
			if (logfolder.length() > 0 && !logfolder.endsWith("/")) logfolder = logfolder + "/";
			
			//validate log folder existence
			//false positive vulnerability as folder is configured and no user input
			File logdir = new File(logfolder);
			if (!logdir.exists() || !logdir.isDirectory()) {
				logfolder = "./";
			}
			
			//set log level
			switch (loglevel) {			
			case ("INFO"):
				level = INFO;		
				break;
			case ("WARNING"):
				level = WARNING;		
				break;
			case ("SEVERE"):
				level = SEVERE;
				break;
        	default:
        		level = INFO;
                break;
			}	   	   
			
			//Set File			
			SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
			logfile = logfile.replaceAll("#ServerName#", InetAddress.getLocalHost().getHostName() + "_" + sdf_day.format(new Date()));		
			
			//false positive vulnerability as folder is configured and no user input
			fileWritter = new FileWriter(logfolder+logfile,true);			
			logger =  new BufferedWriter(fileWritter);			
			
			//finished
			duringInitialization = false;
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}	
	
	/**
	 * force init
	 */
	public static void forceInit() {		
		try{ if (logger != null) { logger.flush(); } } catch (Exception e) { Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e); }
		try{ if (fileWritter != null) { fileWritter.flush(); } } catch (Exception e) { Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e); }
		try{ if (logger != null) { logger.close(); } } catch (Exception e) { Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e); }
		try{ if (fileWritter != null) { fileWritter.close(); } } catch (Exception e) { Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e); }		
		logger = null;
	}
	
	/**
	 * log
	 * @param message
	 */
	public static void log (String message) {		
		ApplicationLogger.log(INFO, message);
	}
	
	/**
	 * log
	 * @param throwable
	 */
	public static void log (Throwable throwable) {
		if (throwable != null) {
			ApplicationLogger.log(SEVERE, throwable.getMessage());			
			Logger.getGlobal().log(Level.SEVERE, throwable.getMessage(), throwable);
		} else {
			ApplicationLogger.log(SEVERE, "throwable = null");
		}
	}
	
	/**
	 * log
	 * @param throwable
	 * @param message
	 */
	public static void log (Throwable throwable, String message) {		
		if (throwable != null) {
			ApplicationLogger.log(SEVERE, message);
			ApplicationLogger.log(SEVERE, throwable.getMessage());
			Logger.getGlobal().log(Level.SEVERE, throwable.getMessage(), throwable);
		} else {
			ApplicationLogger.log(SEVERE, message + " - throwable = null");
		}
	}	
	
	/**
	 * log
	 * @param levelMessage
	 * @param message
	 */
	public static void log (int levelMessage, String message) {
		if (duringInitialization) {
			System.out.println(message);
			System.err.println(message);
			return;
		}
		if (logger == null) init();
		if (levelMessage >= level) {
			//write
			try{
				//logger.newLine(); synchronized required, so we will use \n
				SimpleDateFormat sdf_day_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				logger.append("\n"+sdf_day_time.format(new Date()) + " <"+levelMessage+"> " + message);				
				logger.flush();
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}	
	
	/**
	 * get next date
	 * @param hour
	 * @param minute
	 * @return
	 */
	private static Date getNextDate(int hour, int minute){
	    Calendar now = new GregorianCalendar();
	    //secure delay
	    now.add(Calendar.MINUTE, 1);
	    //planned time
	    Calendar target = new GregorianCalendar(
	    		now.get(Calendar.YEAR),
	    		now.get(Calendar.MONTH),
	    		now.get(Calendar.DATE),
	    		hour,
	    		minute);	    
	    if (target.before(now))
	    {
	    	target.add(Calendar.DATE, 1);
	    }
	    return target.getTime();
	}	
	
}