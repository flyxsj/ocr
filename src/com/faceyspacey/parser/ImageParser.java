package com.faceyspacey.parser;

import java.io.File;

import com.faceyspacey.ocr.OCR;

public abstract class ImageParser implements IParser {

	@Override
	public String extractText(File file) throws Exception {
		OCR ocr = new OCR();
		String text = ocr.recognizeText(file, getFileType());
		return text;
	}

}
