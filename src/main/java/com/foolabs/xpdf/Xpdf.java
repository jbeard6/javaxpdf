
package com.foolabs.xpdf;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstraction for determining whether Xpdf is available at runtime.
 */
public class Xpdf {

    private static final Logger LOGGER = Logger.getLogger("com.foolabs.xpdf.Xpdf");

    private static final boolean available;

    /**
     * Returns <code>true</code> if Xpdf is available for use.
     * 
     * @return <code>true</code> if Xpdf is available, <code>false</code> otherwise
     */
    public static boolean isAvailable() {
        return available;
    }

    static void checkAvailable() {
        if (!available) {
            throw new IllegalStateException("Xpdf is not available!");
        }
    }

    /**
     * A listing of all of the Xpdf classes that contain native methods provided by the {@link NativeLibraryLoader}.
     * 
     * TODO Register all new native method classes here to ensure correct functionality.
     */
    private static final String[] JNI_CLASSES = {// @formatter:off
        "com.foolabs.xpdf.GlobalParameters",
        "com.foolabs.xpdf.PDFDocument",
        "com.foolabs.xpdf.PDFPage"
    }; // @formatter:on

    private static Class<?> loadJniClasses(ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> lastClass = null;

        for (int i = 0; i < JNI_CLASSES.length; i++) {
            String className = JNI_CLASSES[i];
            LOGGER.log(Level.FINER, "Loading {0}", className);
            lastClass = Class.forName(className, true, classLoader);
            LOGGER.log(Level.FINER, "Loaded {0}", className);
        }

        return lastClass;
    }

    static {
        boolean avail = false;
        try {
            LOGGER.info("Attempting to load Xpdf...");
            ClassLoader classLoader = Xpdf.class.getClassLoader();

            /*
             * JNI code (native and Java) must all be loaded into the SAME ClassLoader (the usual parental hierarchy
             * doesn't apply). This is not typically an issue for standalone Java applications, but it can become hairy
             * very quickly for code executing in a container (like Tomcat) or with Groovy.
             * 
             * Load the classes first so that the library can be loaded with the same ClassLoader
             */
            Class<?> xpdfClass = loadJniClasses(classLoader);
            LOGGER.fine("Xpdf Java components loaded!");

            LOGGER.fine("Attempting to load native Xpdf code...");
            NativeLibraryLoader.getInstance(xpdfClass).loadLibrary();
            LOGGER.fine("Xpdf native components loaded!");

            avail = true;
            LOGGER.info("Xpdf loaded successfully!");
        } catch (LinkageError err) {
            LOGGER.log(Level.INFO, "Xpdf (native) is not available!", err);
            avail = false;
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Xpdf is not available!", ex);
            avail = false;
        } finally {
            available = avail;
        }
    }

    /**
     * {@link Xpdf} is a utility class and should not be instantiated.
     */
    private Xpdf() {
        // Prevent instantiation
    }

}
