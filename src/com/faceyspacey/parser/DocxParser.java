package com.faceyspacey.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * parser for ms docx document
 * 
 * @author 21xsj@163.com
 * 
 */
class DocxParser implements IParser {

	public String getFileType() {
		return "docx";
	}

	public String extractText(File file) throws Exception {
		String text = "";
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
		XWPFDocument document = new XWPFDocument(bis);
		XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		text = extractor.getText();
		fis.close();
		bis.close();
		return text;
	}
}
