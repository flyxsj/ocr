package com.faceyspacey.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

import com.faceyspacey.util.GlobalConstant;
import com.faceyspacey.util.ToolUtil;

public class OCR {
	private final String LANG_OPTION = "-l";
	private static final String tessPath = GlobalConstant.TESSERACT_PATH;

	public String recognizeText(File imageFile, String imageFormat) throws Exception {
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer sb = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(tessPath + "\\tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(tessPath + "\\tesseract");
		}
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("eng");
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());
		cmd.set(1, imageFile.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		int w = process.waitFor();
		String tmpTxtFile = outputFile.getAbsolutePath() + ".txt";
		if (w == 0) {
			FileInputStream fis = new FileInputStream(tmpTxtFile);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader in = new BufferedReader(isr);
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str).append(ToolUtil.EOL);
			}
			fis.close();
			isr.close();
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			throw new RuntimeException(msg);
		}
		new File(tmpTxtFile).delete();
		return sb.toString();
	}
}
