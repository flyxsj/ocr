package com.faceyspacey.util;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

public class Pdf2Image {

	private static final String password = "";
	private static final String format = "png";
	private static final int startPage = 1;
	private static final int endPage = Integer.MAX_VALUE;
	// private static final int endPage = 3; // TODO
	private static final int resolution = 200;

	private Pdf2Image() {
	}

	public static void convert2Images(File file) throws Exception {

		PDDocument doc = null;
		doc = PDDocument.load(file.getAbsolutePath());
		if (doc.isEncrypted()) {
			doc.decrypt(password);
		}
		String ourDirStr = file.getParentFile().getAbsolutePath() + File.separator + "tmp" + File.separator;
		File outDir = new File(ourDirStr);
		if (outDir.exists() == false) {
			outDir.mkdirs();
		}
		int imageType = BufferedImage.TYPE_BYTE_GRAY;
		PDFImageWriter imageWriter = new PDFImageWriter();
		String prefix = ourDirStr + file.getName();
		imageWriter.writeImage(doc, format, password, startPage, endPage, prefix, imageType, resolution);
		doc.close();
	}
}