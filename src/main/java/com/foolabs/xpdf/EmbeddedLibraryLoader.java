
package com.foolabs.xpdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link NativeLibraryLoader} that extracts the binary off of the classpath (usually contained within a jar file).
 */
class EmbeddedLibraryLoader extends NativeLibraryLoader {

    private static final Logger LOG = Logger.getLogger("com.foolabs.xpdf.EmbeddedLibraryLoader");

    /**
     * Construct a new EmbeddedLibraryLoader
     * 
     * @param contextClass
     * @param next
     */
    public EmbeddedLibraryLoader(Class<?> contextClass, NativeLibraryLoader next) {
        super(contextClass, next);
    }

    @Override
    public void loadLibrary() throws LinkageError {
        String path = "/META-INF/lib/" + OperatingSystem.getName() + "/Xpdf-" + OperatingSystem.getArchitecture()
            + ".bin";
        LOG.log(Level.FINE, "Searching Path: {0}", path);

        URL resource = EmbeddedLibraryLoader.class.getResource(path);
        LOG.log(Level.FINE, "Resource URL: {0}", resource);

        if (resource == null) {
            LOG.log(Level.INFO, "Embedded Library not found at {0}", path);
            super.loadLibrary();

        } else {
            try {
                File libFile = File.createTempFile("libxpdf-jni-", ".bin").getAbsoluteFile();
                libFile.deleteOnExit();
                LOG.log(Level.FINE, "Extracting {0} to {1}...", new Object[] { path, libFile });

                copyTo(resource, libFile);

                LOG.log(Level.FINE, "Loading {0} as native library...", libFile);
                loadLibrary(libFile.getAbsolutePath(), true);
                LOG.log(Level.INFO, "Loaded {0}", libFile);
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Failed to copy embedded library to temporary file.", ex);
                super.loadLibrary();
            }
        }

    }

    private static void copyTo(URL resource, File file) throws IOException {
        InputStream inputStream = resource.openStream();

        try {
            copyTo(inputStream, file);
        } finally {
            inputStream.close();
        }
    }

    private static void copyTo(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);

        try {
            byte[] buffer = new byte[4096];

            int n = 0;
            while ((n = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, n);
            }
        } finally {
            outputStream.close();
        }
    }

    private static class OperatingSystem {

        public static String getName() {
            // String name = System.getProperty("os.name");
            //
            // // Mutate into a standard form compatible with package names
            // name = name.replaceAll("\\s", "");
            // name = name.toLowerCase();
            //
            // return name;
            return System.getProperty("os.name");
        }

        public static String getArchitecture() {
            return System.getProperty("os.arch");
        }

    }

}
