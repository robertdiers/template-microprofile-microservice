package template.template_microservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * simple configuration reader
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class SimpleConfiguration {
	
	/* internal variables */
	private static SimpleConfiguration instance = null;
	private Properties configFile = new Properties();

	/* without Staging --> later we have to use staging variables */	
    private final static String ENV_PATH_VAR = "com.bmw.mastersolutions.gf.project.data";
    private final static String ENV_DOMAIN_DIR_VAR = "com.bmw.mastersolutions.gf.domain.dir";
    private final static String ENV_PROFILE_VAR = "com.bmw.mastersolutions.gf.profile";
    private final static String ENV_CUSTOM_PROFILE_VAR = "com.bmw.mastersolutions.gf.custom_profile";
    private final static String LOKAL_PROFILE = "LOCAL";
    private final static String PROPERTIES_FOLDER = "application";
    private final static String PROPERTIES_FILENAME = "template.ini";
    private final static boolean PROPERTIES_USEEXTENSION = true;
    
    /** 
     * create filename with extension
     * @param filename
     * @param with_extension
     * @return
     */
    private static String getFilename(String filename, boolean with_extension) {
    	if (!with_extension) return filename;
		
    	//environment
    	String profile = getProfile();  	
    	
    	//with extension    	
		return filename + "_" + profile;
    }
	
    /** path to config file */
	public static String getPropertyPath(String filename)
	{		
		//project-data
		String systempath = System.getProperty(ENV_PATH_VAR);
    	if (systempath == null) systempath = "./";
    	else systempath = systempath.replaceAll("project-local", "project-synced");
    	if (!systempath.endsWith("/")) systempath = systempath + "/";
    	return systempath + PROPERTIES_FOLDER + "/" + filename;    	
	}
	
	/**
	 * get domain dir path
	 * @return
	 */
	public static String getPathDomainDir() {
		//domain dir
		String systempath = System.getProperty(ENV_DOMAIN_DIR_VAR);
    	if (systempath == null)
    	{
    		//local or XS
    		systempath = "./";
    	}
    	if (!systempath.endsWith("/")) systempath = systempath + "/";
    	return systempath;
	}
	
	/** read Staging Profile **/
	public static String getProfile() {
		//environment
    	String profile = System.getProperty(ENV_CUSTOM_PROFILE_VAR);    	
    	if (profile == null || profile.trim().length() < 1 || profile.trim().equalsIgnoreCase("null")) profile = System.getProperty(ENV_PROFILE_VAR);
    	System.out.println("#####PROFILE##### " + profile);
    	if (profile == null) profile = LOKAL_PROFILE;
    	return profile.toUpperCase();
	}
	
	/** get config file as properties */
	public static Properties getPropertyPathProperties(String filename, boolean with_extension) throws Exception
	{	
		//no cache
		String filenamewithext = getFilename(filename, with_extension);		
			
		Properties props = new Properties();
		try{
			//check for file
			String configpath = getPropertyPath(filenamewithext);
			File configfile = new File(configpath);
			
			InputStream is = null;
			if (configfile.exists()) {
				//use file on drive
				ApplicationLogger.log("loading from file: " + configpath);
				is = new FileInputStream(configpath);	
			} else {
				//use files in WAR				
				ApplicationLogger.log("loading from deployment: " + filenamewithext);
				is = SimpleConfiguration.class.getClassLoader().getResourceAsStream(filenamewithext);
			}			
			props.load(is);
			
			is.close();
		} catch (IOException e)
		{
			ApplicationLogger.log(e, filename + " " + with_extension);
			throw new Exception("property file not found: " + filename);
		}			
		
		return props;
	}
	
	/** private constructor */
	private SimpleConfiguration()
	{
		loadProperties();
	}
	
	/**
	 * (re)load properties
	 */
	public void loadProperties()
	{
		try {	
			configFile = getPropertyPathProperties(PROPERTIES_FILENAME, PROPERTIES_USEEXTENSION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** get Property */
	public String getPropertyInternally(String key)
	{		
		return configFile.getProperty(key);
	}	
	
	/** get an instance */
	public static SimpleConfiguration getInstance()
	{
		if (instance == null) instance = new SimpleConfiguration();
		return instance;
	}
	
	/** get Property */
	public static String getProperty(String key)
	{		
		return getInstance().getPropertyInternally(key);
	}
	
	/** get Property with null replacement */
	public static String getPropertyWithoutNull(String key)
	{
		String res = getProperty(key);
		if (res == null) res = "";
		return res;
	}		
		
}
