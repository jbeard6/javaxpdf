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
		TextCollector collector = new TextCollector();
		_getText(collector, false, false);
		return collector.toString();
	}

	private native void _getText(TextCollector collector, boolean fixedPitch,
			boolean rawLayout);

	@Override
	protected native void finalize() throws Throwable;

}
