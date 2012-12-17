package com.faceyspacey.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.faceyspacey.analyser.IAnalyser;
import com.faceyspacey.analyser.TextAnalyser;
import com.faceyspacey.domain.QuestionAndAnswer;
import com.faceyspacey.parser.IParser;
import com.faceyspacey.parser.ParserFactory;
import com.faceyspacey.util.GlobalConstant;

public class OcrStartUp {
	private static final Logger logger = LoggerFactory.getLogger(OcrStartUp.class);

	public static void main(String[] args) throws Exception {
		String filePath = "D:/ocrfile/test.pdf";
		filePath = parseArguments(args, filePath);
		String text = parseFile(filePath);
		logger.info("analyze text to question and answer.");
		List<QuestionAndAnswer> list = analyzeText(text);
		JSONArray ja = new JSONArray();
		for (QuestionAndAnswer questionAndAnswer : list) {
			JSONObject jo = new JSONObject();
			jo.put("question", questionAndAnswer.getQuestion());
			jo.put("answers", questionAndAnswer.getAnswerList());
			ja.add(jo);
		}
		System.out.println(ja.toString());
		logger.info("analyze result:" + ja.toString());
	}

	private static String parseArguments(String[] args, String filePath) {
		if (ArrayUtils.isEmpty(args)) {
			usage();
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(GlobalConstant.ARG_TESSERACT)) {
				i++;
				if (i >= args.length) {
					usage();
				}
				GlobalConstant.TESSERACT_PATH = args[i];
			} else if (args[i].equals(GlobalConstant.ARG_FILE)) {
				i++;
				if (i >= args.length) {
					usage();
				}
				filePath = args[i];
			}
		}
		return filePath;
	}

	private static String parseFile(String filePath) {
		File file = new File(filePath);
		try {
			IParser parser = ParserFactory.getParser(file);
			String path = file.getAbsolutePath();
			logger.info("parsing file={}", path);
			String text = parser.extractText(file);
			text = text.trim();
			logger.info("=======parse result=======\n");
			logger.info(text);
			logger.info("==========================\n");
			return text;
		} catch (Exception e) {
			logger.error("file path=" + filePath, e);
			return "";
		}
	}

	private static List<QuestionAndAnswer> analyzeText(String text) {
		List<QuestionAndAnswer> list = new ArrayList<QuestionAndAnswer>();
		IAnalyser analyser = new TextAnalyser();
		try {
			list = analyser.analyseText(text);
		} catch (Exception e) {
			logger.error("text=" + text, e);
		}
		return list;
	}

	private static void usage() {
		System.err.println("Usage: \n" + "-tesseract <tesseract directory> tesseract ocr software install directory\n"
				+ "-file <recognizing file> the would be recognizing file(jpg,png,gif,pdf,doc,docx)\n");
		System.exit(1);
	}

	@SuppressWarnings("unused")
	private static String readFile(String filePath) throws Exception {

		StringBuilder sb = new StringBuilder();
		String file = filePath;
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		fr.close();
		br.close();
		return sb.toString();
	}
}
