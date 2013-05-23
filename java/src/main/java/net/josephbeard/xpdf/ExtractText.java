package net.josephbeard.xpdf;

import java.io.File;

public class ExtractText {

	public static void main(String[] args) throws InstantiationException {
		File libraryFile = new File(args[0]);
		System.load(libraryFile.getAbsolutePath());
		// System.loadLibrary("xpdf-jni");

		for (int i = 1; i < args.length; i++) {
			String fileName = args[i];
			File file = new File(fileName);
			if (!file.exists()) {
				System.err.println(fileName + " does not exist!");
				continue;
			}

			PDFDocument document = PDFDocument.createInstance(file);

			for (PDFPage page : document.getPages()) {
				System.out.println(page.getText());
			}
		}
	}

}
