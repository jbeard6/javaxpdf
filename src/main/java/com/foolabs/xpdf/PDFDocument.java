
package com.foolabs.xpdf;

import static java.util.logging.Level.FINER;

import java.io.File;
import java.util.logging.Logger;

/**
 * Abstraction for an Xpdf document. Due to limitations of the current version of Xpdf, all {@link PDFDocument}
 * instances must be backed by a {@link File}.
 */
public final class PDFDocument {

    /** The class-level {@link Logger}. */
    private static final Logger LOGGER = Logger.getLogger(PDFDocument.class.getName());

    /**
     * Create a {@link PDFDocument} instance that represents the specified PDF {@link File}.
     * 
     * @param file the PDF file
     * @return the new instance
     * @throws InstantiationException if Xpdf does not correctly initialize the document
     */
    public static PDFDocument createInstance(File file) throws InstantiationException {
        String fileName = file.getAbsolutePath();
        long _handle = _createInstance(fileName);

        if (_handle >= 0) {
            LOGGER.log(FINER, "Created native PDFDoc instance @{0}", _handle);
            return new PDFDocument(_handle);
        } else {
            throw new InstantiationException("XPDF Blows");
        }
    }

    /**
     * Create the native objects.
     * 
     * @param fileName the absolute path to the {@link File}
     * @return the {@link #_handle}
     */
    private static native long _createInstance(String fileName);

    /**
     * Construct a new {@link PDFDocument} backed by the PDFDoc pointed to by the {@code _handle}.
     * 
     * @param _handle the {@link #_handle}
     */
    private PDFDocument(long _handle) {
        this._handle = _handle;
    }

    /** Pointer to the native object. */
    private final long _handle;

    /**
     * Returns the number of pages in this {@link PDFDocument}.
     * 
     * @return the page count
     */
    public int getPageCount() {
        return _getPageCount();
    }

    /**
     * Returns the page count from the native object.
     * 
     * @return the page count
     */
    private native int _getPageCount();

    /**
     * Returns <code>true</code> if this {@link PDFDocument} is encrypted.
     * 
     * @return <code>true</code> if this document is encrypted
     */
    public boolean isEncrypted() {
        return _isEncrypted();
    }

    /**
     * Returns the encryption status from the native object.
     * 
     * @return <code>true</code> if the document is encrypted
     */
    private native boolean _isEncrypted();

    /**
     * Returns the {@link PDFPage} that represents the specified page.
     * 
     * @param pageNumber the (one-based) page number
     * @return the page object
     */
    public PDFPage getPage(int pageNumber) {
        if (pageNumber <= 0 || pageNumber > _getPageCount()) {
            throw new IllegalArgumentException();
        }
        return _getPage(pageNumber);
    }

    /**
     * Returns the {@link PDFPage} from the native object.
     * 
     * @param pageNumber the page number (one-based)
     * @return the page object
     */
    private native PDFPage _getPage(int pageNumber);

    /**
     * Returns the {@link PDFPage pages} of this {@link PDFDocument}.
     * 
     * @return the pages
     */
    public PDFPage[] getPages() {
        int pageCount = _getPageCount();
        LOGGER.log(FINER, "Document has {0} pages.", pageCount);

        PDFPage[] pages = new PDFPage[pageCount];

        for (int i = 0; i < pageCount; i++) {
            pages[i] = _getPage(i + 1);
            LOGGER.log(FINER, "Successfully obtained Page {0}.", i);
        }

        return pages;
    }

    /**
     * Releases the native objects.
     */
    @Override
    protected native void finalize();

}
