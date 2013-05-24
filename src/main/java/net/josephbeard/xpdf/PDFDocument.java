package net.josephbeard.xpdf;

import static java.util.logging.Level.FINER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public final class PDFDocument {

	private static final Logger LOGGER = Logger.getLogger(PDFDocument.class
			.getName());

	public static PDFDocument createInstance(File file)
			throws InstantiationException {
		String fileName = file.getAbsolutePath();
		long _handle = _createInstance(fileName);

		if (_handle >= 0) {
			LOGGER.log(FINER, "Created native PDFDoc instance @{0}", _handle);
			return new PDFDocument(_handle);
		} else {
			throw new InstantiationException("XPDF Blows");
		}
	}

	private static final TemporaryFileService<PDFDocument> tempFileService;

	static {
		tempFileService = new TemporaryFileService<PDFDocument>("xpdf", ".pdf");
		tempFileService.start();
	}

	public static PDFDocument createInstance(InputStream inputStream)
			throws IOException, InstantiationException {
		File tempFile = tempFileService.createTemporaryFile(inputStream);

		PDFDocument instance = createInstance(tempFile);

		// Delete file when garbage collected
		tempFileService.cleanup(instance, tempFile);

		return instance;
	}

	private static native long _createInstance(String fileName);

	private PDFDocument(long _handle) {
		this._handle = _handle;
	}

	private final long _handle;

	/**
	 * Returns the number of pages in this {@link PDFDocument}.
	 * 
	 * @return the page count
	 */
	public int getPageCount() {
		return _getPageCount();
	}

	private native int _getPageCount();

	/**
	 * Returns <code>true</code> if this {@link PDFDocument} is encrypted.
	 * 
	 * @return <code>true</code> if this document is encrypted
	 */
	public boolean isEncrypted() {
		return _isEncrypted();
	}

	private native boolean _isEncrypted();

	public PDFPage getPage(int pageNumber) {
		if (pageNumber <= 0 || pageNumber > _getPageCount()) {
			throw new IllegalArgumentException();
		}
		return _getPage(pageNumber);
	}

	private native PDFPage _getPage(int pageNumber);

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

	protected native void finalize();

}
