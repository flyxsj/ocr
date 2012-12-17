package com.faceyspacey.parser;

import java.io.File;

import com.faceyspacey.util.ToolUtil;

public class ParserFactory {
	private ParserFactory() {

	}

	public static IParser getParser(File file) {
		String path = file.getAbsolutePath();
		if (file.exists() == false) {
			throw new RuntimeException("file=" + path + ", it's not existed.");
		}
		IParser parser = null;
		String type = ToolUtil.getFileTypeByMime(file);
		if ("jpeg".equals(type)) {
			parser = new JpgParser();
		} else if ("png".equals(type)) {
			parser = new PngParser();
		} else if ("gif".equals(type)) {
			parser = new GifParser();
		} else if ("pdf".equals(type)) {
			parser = new PdfParser();
		} else if ("msword".equals(type)) {
			parser = new DocParser();
		} else if ("zip".equals(type) && "docx".equals(ToolUtil.getFileTypeByFileHeader(file))) {
			parser = new DocxParser();
		}
		if (parser == null) {
			throw new RuntimeException("the file=" + path + " is not a valid file to parse.");
		}
		return parser;
	}
}
