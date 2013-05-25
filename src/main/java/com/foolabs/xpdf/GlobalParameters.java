package com.foolabs.xpdf;

import java.nio.charset.Charset;

/**
 * Java abstraction for the Xpdf GlobalParams class. Warning: this is a
 * singleton instance across the ENTIRE JVM! Options set here will affect ALL
 * users of Xpdf in the JVM.
 */
public class GlobalParameters {

	private static GlobalParameters INSTANCE;

	public static GlobalParameters getInstance() {
		if (INSTANCE == null) {
			long _handle = _getInstance();
			INSTANCE = new GlobalParameters(_handle);
			
			// Initialize with UTF-8 encoding
			INSTANCE._setTextEncoding("UTF-8");
		}

		return INSTANCE;
	}

	private static native long _getInstance();

	private GlobalParameters(long _handle) {
		this._handle = _handle;
	}

	private final long _handle;

	public Charset getCharset() {
		String encoding = _getTextEncoding();
		return Charset.forName(encoding);
	}

	public void setCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		_setTextEncoding(charset.name());
	}

	public String getTextEncoding() {
		return _getTextEncoding();
	}

	private native String _getTextEncoding();

	public void setTextEncoding(String encoding) {
		_setTextEncoding(encoding);
	}

	private native void _setTextEncoding(String encoding);

	public static enum EndOfLine {
		UNIX("unix"), DOS("dos"), MAC("mac");

		private EndOfLine(String value) {
			this.value = value;
		}

		private final String value;

		String getValue() {
			return value;
		}
	}

	public EndOfLine getEndOfLine() {
		String eol = _getTextEOL();

		for (EndOfLine e : EndOfLine.values()) {
			if (e.getValue() == eol) {
				return e;
			}
		}

		return null;
	}

	private native String _getTextEOL();

	public void setEndOfLine(EndOfLine eol) {
		if (eol == null) {
			throw new NullPointerException();
		}
		_setTextEOL(eol.getValue());
	}

	private native void _setTextEOL(String eol);

	public boolean isBreakBetweenPages() {
		return _getTextPageBreaks();
	}

	private native boolean _getTextPageBreaks();

	public void setBreakBetweenPages(boolean breakBetweenPages) {
		_setTextPageBreaks(breakBetweenPages);
	}

	private native void _setTextPageBreaks(boolean textPageBreaks);

	public boolean isErrorOutputSuppressed() {
		return _getErrQuiet();
	}

	private native boolean _getErrQuiet();

	public void setErrorOutputSuppressed(boolean errorOutputSuppressed) {
		_setErrQuiet(errorOutputSuppressed);
	}

	private native void _setErrQuiet(boolean errQuiet);

	protected native void finalize();

}
