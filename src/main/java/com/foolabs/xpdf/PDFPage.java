package com.foolabs.xpdf;


/**
 * Representation of a page within a {@link PDFDocument}.
 */
public class PDFPage {

	private PDFPage(long _handle) {
		this._handle = _handle;
	}

	private long _handle;

	public int getNumber() {
		return _getNumber();
	}

	private native int _getNumber();

	public String getText() {
		TextCollector collector = TextCollector.createInstance();
		_getText(collector, false, 0.0, false);
		return collector.toString();
	}

	private native void _getText(TextCollector collector,
			boolean physicalLayout, double fixedPitch, boolean rawOrder);

	@Override
	protected native void finalize() throws Throwable;

}
