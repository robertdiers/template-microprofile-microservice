package template.template_microservice.util;

/**
 * thread exposer (using LocalThread)
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public final class ThreadExposer {

    /**
     * Can not instantiate.
     */
    private ThreadExposer() {
    }

    /** The exposed objects. */        
    private static ThreadLocal<Long> exposedStartTime = new ThreadLocal<Long>();
    private static ThreadLocal<String> exposedCaller = new ThreadLocal<String>();
    
    /**
     * exposeStart.
     */
    public static void exposeStartTime(Long lang) {
    	exposedStartTime.set(lang);
    }

    /**
     * getExposedStart.
     */
    public static Long getExposedStartTime() {
        return (Long) exposedStartTime.get();
    }
    
    /**
     * exposeCaller.
     */
    public static void exposeCaller(String caller) {
    	exposedCaller.set(caller);
    }

    /**
     * getCaller.
     */
    public static String getExposedCaller() {
        return (String) exposedCaller.get();
    }   

}
