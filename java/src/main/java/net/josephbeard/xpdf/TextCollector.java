package net.josephbeard.xpdf;

public class TextCollector {

	public TextCollector() {
		this.buffer = new StringBuilder();
	}

	private final StringBuilder buffer;

	public void append(String text) {
		this.buffer.append(text);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

}
