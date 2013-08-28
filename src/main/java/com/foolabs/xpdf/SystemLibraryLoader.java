
package com.foolabs.xpdf;

/**
 * Loads the library using the conventional conventional <code>java.library.path</code> mechanism.
 */
class SystemLibraryLoader extends NativeLibraryLoader {

    /**
     * Construct a new {@link SystemLibraryLoader}.
     * 
     * @param contextClass the context class
     */
    public SystemLibraryLoader(Class<?> contextClass) {
        super(contextClass);
    }

    @Override
    public void loadLibrary() throws LinkageError {
        loadLibrary("xpdf-jni", false);
    }

}
