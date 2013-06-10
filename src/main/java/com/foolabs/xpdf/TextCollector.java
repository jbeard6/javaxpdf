package com.foolabs.xpdf;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class TextCollector {

	public static TextCollector createInstance()
			throws UnsupportedCharsetException {
		Charset charset = GlobalParameters.getInstance().getCharset();

		return new TextCollector(charset);
	}

	public TextCollector(Charset charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		this.charset = charset;
		this.buffer = new StringBuilder();
	}

	private final StringBuilder buffer;

	public void append(String text) {
		this.buffer.append(text);
	}

	private final Charset charset;

	public void append(byte[] data) throws UnsupportedEncodingException {
		String text = new String(data, charset);
		this.buffer.append(text);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

}
