package com.faceyspacey.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * parser for ms doc document
 * 
 * @author 21xsj@163.com
 * 
 */

public class DocParser implements IParser {

	public String getFileType() {
		return "doc";
	}

	public String extractText(File file) throws Exception {
		String text = "";
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fi, BUFFER_SIZE);
		HWPFDocument doc = new HWPFDocument(bis);
		WordExtractor extractor = new WordExtractor(doc);
		text = extractor.getText();
		fi.close();
		bis.close();
		return text;
	}
}
