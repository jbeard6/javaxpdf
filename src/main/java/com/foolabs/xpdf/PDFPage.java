package com.foolabs.xpdf;

/**
 * Representation of a page within a {@link PDFDocument}.
 */
public class PDFPage {

	private PDFPage(PDFDocument document, long _handle) {
		if (document == null) {
			throw new NullPointerException();
		}
		this.document = document;
		this._handle = _handle;
	}

	private final PDFDocument document;

	private long _handle;

	public int getNumber() {
		return _getNumber();
	}

	private native int _getNumber();

	public String getText() {
		TextCollector collector = TextCollector.createInstance();
		_getText(document, collector, false, 0.0, false);
		return collector.toString();
	}

	private native void _getText(PDFDocument document, TextCollector collector,
			boolean physicalLayout, double fixedPitch, boolean rawOrder);

	@Override
	protected native void finalize() throws Throwable;

}
