package org.rce4j.util;

public final class SystemInformation {
    public static final String OS_NAME;
    public static final String OS_VERSION;
    public static final String OS_ARCH;
    public static final String JAVA_VERSION;
    public static final String JAVA_HOME;
    public static final String ROOT;
    
    static {
        OS_NAME = System.getProperty("os.name");
        OS_VERSION = System.getProperty("os.version");
        OS_ARCH = System.getProperty("os.arch");
        
        JAVA_VERSION = System.getProperty("java.version");
        JAVA_HOME = System.getProperty("java.home");
        ROOT = System.getProperty("user.name");
    }
}
