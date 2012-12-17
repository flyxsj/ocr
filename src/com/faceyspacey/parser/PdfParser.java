package com.faceyspacey.parser;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;

import com.faceyspacey.util.Pdf2Image;

/**
 * parser for pdf document
 * 
 * @author 21xsj@163.com
 * 
 */

public class PdfParser implements IParser {

	public String getFileType() {
		return "pdf";
	}

	public String extractText(File file) throws Exception {
		String outDirStr = file.getParentFile().getAbsolutePath() + File.separator + "tmp";
		File dir = clearOldFile(outDirStr);
		Pdf2Image.convert2Images(file);
		StringBuilder sb = new StringBuilder();
		IParser pngParser = new PngParser();
		File[] pngFiles = dir.listFiles();
		if (ArrayUtils.isEmpty(pngFiles) == false) {
			for (File png : pngFiles) {
				String text = pngParser.extractText(png);
				sb.append(text);
			}
		}
		return sb.toString();
	}

	private File clearOldFile(String outfolder) {
		File dir = new File(outfolder);
		File[] fileArray = dir.listFiles();
		if (ArrayUtils.isEmpty(fileArray) == false) {
			for (File f : fileArray) {
				f.delete();
			}
		}
		return dir;
	}
}
