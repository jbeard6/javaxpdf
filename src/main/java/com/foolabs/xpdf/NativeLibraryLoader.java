
package com.foolabs.xpdf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Loads the native library component for the Xpdf classes.
 * 
 * An attempt is first made to locate the native library component on the classpath (embedded in the jar, for example).
 * If it is located on the classpath, it is copied to the <code>java.io.tmpdir</code> directory and
 * {@link ClassLoader#loadLibrary(Class, String, boolean) loaded} from there. Otherwise, an attempt is made to load the
 * library by name.
 */
abstract class NativeLibraryLoader {

    private static final Logger LOG = Logger.getLogger("com.foolabs.xpdf.NativeLibraryLoader");

    public static NativeLibraryLoader getInstance(Class<?> contextClass) {
        NativeLibraryLoader systemLoader = new SystemLibraryLoader(contextClass);

        return new EmbeddedLibraryLoader(contextClass, systemLoader);
    }

    protected NativeLibraryLoader(Class<?> contextClass) {
        this(contextClass, null);
    }

    protected NativeLibraryLoader(Class<?> contextClass, NativeLibraryLoader next) {
        if (contextClass == null) {
            throw new NullPointerException("context class required");
        }
        this.contextClass = contextClass;
        this.next = next;
    }

    protected final Class<?> contextClass;

    private final NativeLibraryLoader next;

    public void loadLibrary() throws LinkageError {
        if (next != null) {
            next.loadLibrary();
        }
    }

    /**
     * Invokes the {@link ClassLoader#loadLibrary(Class, String, boolean)} method by reflection to load the library with
     * the specified {@code name} into the {@link ClassLoader} that loaded the {@link #context} {@link Class}.
     * 
     * @param name the library name
     * @param isAbsolute <code>true</code> if the {@code name} represents an absolute path, <code>false</code> otherwise
     */
    protected final void loadLibrary(String name, boolean isAbsolute) {
        try {
            Method loadLibrary = ClassLoader.class.getDeclaredMethod("loadLibrary", Class.class, String.class,
                boolean.class);

            loadLibrary.setAccessible(true); // Allow us to invoke the method

            loadLibrary.invoke(null, contextClass, name, isAbsolute);
        } catch (SecurityException ex) {
            LOG.warning("Not permitted to invoke ClassLoader.loadLibrary(Class, String, boolean)!");
            throw (LinkageError)(new UnsatisfiedLinkError().initCause(ex));

        } catch (NoSuchMethodException ex) {
            LOG.warning("ClassLoader.loadLibrary(Class, String, boolean) does not exist!");
            throw (LinkageError)(new UnsatisfiedLinkError().initCause(ex));

        } catch (IllegalAccessException ex) {
            LOG.warning("Method.setAccessible(true) doesn't work.");
            throw (LinkageError)(new UnsatisfiedLinkError().initCause(ex));

        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof LinkageError) {
                throw (LinkageError)ex.getCause();
            }
            throw (LinkageError)(new UnsatisfiedLinkError().initCause(ex.getCause()));
        }
    }

}
