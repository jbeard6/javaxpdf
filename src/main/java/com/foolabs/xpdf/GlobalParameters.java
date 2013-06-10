package com.foolabs.xpdf;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Java abstraction for the Xpdf GlobalParams class. Warning: this is a
 * singleton instance across the ENTIRE JVM! Options set here will affect ALL
 * users of Xpdf in the JVM.
 */
public class GlobalParameters {

	/** The singleton instance. */
	private static GlobalParameters INSTANCE;

	/**
	 * Return the singleton {@link GlobalParameters} instance.
	 * 
	 * @return the singleton instance
	 */
	public static GlobalParameters getInstance() {
		if (INSTANCE == null) {
			long _handle = _getInstance();
			INSTANCE = new GlobalParameters(_handle);

			// Initialize with UTF-8 encoding
			INSTANCE._setTextEncoding("UTF-8");
		}

		return INSTANCE;
	}

	/**
	 * Obtains the handle to the native {@code GlobalParams} object.
	 * 
	 * @return the native handle
	 */
	private static native long _getInstance();

	private GlobalParameters(long _handle) {
		this._handle = _handle;
	}

	/** The native handle. */
	private final long _handle;

	/**
	 * Returns the {@link Charset} that will be used for reading
	 * {@link PDFDocument} text.
	 * 
	 * @return the character set
	 * @throws UnsupportedCharsetException
	 *             if the {@link Charset} is not supported by this JVM
	 */
	public Charset getCharset() throws UnsupportedCharsetException {
		String encoding = _getTextEncoding();
		return Charset.forName(encoding);
	}

	/**
	 * Sets the {@link Charset} that will be used for reading
	 * {@link PDFDocument} text.
	 * 
	 * @param charset
	 *            the character set
	 * @throws NullPointerException
	 *             if the {@link Charset} is <code>null</code>
	 */
	public void setCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		_setTextEncoding(charset.name());
	}

	/**
	 * Returns the name of the {@link Charset} that will be used for reading
	 * {@link PDFDocument} text.
	 * 
	 * @return the encoding scheme
	 */
	public String getTextEncoding() {
		return _getTextEncoding();
	}

	/**
	 * Returns the encoding scheme from the native object.
	 * 
	 * @return the encoding scheme
	 */
	private native String _getTextEncoding();

	public void setTextEncoding(String encoding) {
		_setTextEncoding(encoding);
	}

	/**
	 * Sets the encoding scheme on the native object.
	 * 
	 * @param encoding
	 *            the encoding scheme
	 */
	private native void _setTextEncoding(String encoding);

	/**
	 * The end-of-line style for emitted output.
	 */
	public static enum EndOfLine {
		/** The UNIX EOL style: <code>\n</code>. */
		UNIX("unix"),

		/** The DOS EOL style: <code>\r\n</code>. */
		DOS("dos"),

		/** The Mac EOL style: <code>\r</code>. */
		MAC("mac");

		/**
		 * Construct a new {@link EndOfLine}.
		 * 
		 * @param value
		 *            the native value
		 */
		private EndOfLine(String value) {
			this.value = value;
		}

		/** The native value. */
		private final String value;

		/**
		 * Returns the native value.
		 * 
		 * @return the native value
		 */
		String getValue() {
			return value;
		}
	}

	/**
	 * Returns the {@link EndOfLine} style used for emitted text.
	 * 
	 * @return the end of line style
	 */
	public EndOfLine getEndOfLine() {
		String eol = _getTextEOL();

		for (EndOfLine e : EndOfLine.values()) {
			if (e.getValue() == eol) {
				return e;
			}
		}

		return null;
	}

	/**
	 * Returns the native value for the {@link EndOfLine} from the native
	 * object.
	 * 
	 * @return the native EOL style
	 */
	private native String _getTextEOL();

	/**
	 * Set the {@link EndOfLine} style for {@link PDFDocument} text.
	 * 
	 * @param eol
	 *            the EOL style
	 */
	public void setEndOfLine(EndOfLine eol) {
		if (eol == null) {
			throw new NullPointerException();
		}
		_setTextEOL(eol.getValue());
	}

	/**
	 * Sets the native EOL style.
	 * 
	 * @param eol
	 *            the native {@link EndOfLine} value
	 */
	private native void _setTextEOL(String eol);

	/**
	 * Returns <code>true</code> if a form feed character will be written
	 * between {@link PDFPage#getText() page text}.
	 * 
	 * @return <code>true</code> if page content is to be split
	 */
	public boolean isBreakBetweenPages() {
		return _getTextPageBreaks();
	}

	/**
	 * Returns <code>true</code> if page content is to be split according to the
	 * native object.
	 * 
	 * @return <code>true</code> if page content is to be split
	 */
	private native boolean _getTextPageBreaks();

	/**
	 * Sets if a form feed character will be written between
	 * {@link PDFPage#getText() page text}.
	 * 
	 * @param breakBetweenPages
	 *            <code>true</code> if page content is to be split
	 */
	public void setBreakBetweenPages(boolean breakBetweenPages) {
		_setTextPageBreaks(breakBetweenPages);
	}

	/**
	 * Sets if page content is to be split on the native object.
	 * 
	 * @param textPageBreaks
	 *            <code>true</code> if page content is to be split
	 */
	private native void _setTextPageBreaks(boolean textPageBreaks);

	/**
	 * Returns <code>true</code> if error output is suppressed.
	 * 
	 * @return <code>true</code> if error output is suppressed
	 */
	public boolean isErrorOutputSuppressed() {
		return _getErrQuiet();
	}

	/**
	 * Returns <code>true</code> if error output is suppressed according to the
	 * native object.
	 * 
	 * @return <code>true</code> if error output is suppressed
	 */
	private native boolean _getErrQuiet();

	/**
	 * Set to <code>true</code> to suppress error output.
	 * 
	 * @param errorOutputSuppressed
	 *            <code>true</code> if error output should be suppressed
	 */
	public void setErrorOutputSuppressed(boolean errorOutputSuppressed) {
		_setErrQuiet(errorOutputSuppressed);
	}

	/**
	 * Sets whether error output should be suppressed on the native object.
	 * 
	 * @param errQuiet
	 *            <code>true</code> if error output should be suppressed
	 */
	private native void _setErrQuiet(boolean errQuiet);

	/**
	 * Delete the native {@code GlobalParams} object.
	 */
	@Override
	protected native void finalize();

}
