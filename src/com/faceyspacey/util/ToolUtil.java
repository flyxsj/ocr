package com.faceyspacey.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

public class ToolUtil {

	public static final String EOL = System.getProperty("line.separator");
	
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
	static {
		getAllFileTypes();
	}

	private static void getAllFileTypes() {
		FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); // MS doc
		FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); // Adobe Acrobat
		FILE_TYPE_MAP.put("504b0304140006000800", "docx");// docx
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String getFileTypeByFileHeader(File file) {
		String res = null;
		try {
			FileInputStream is = new FileInputStream(file);
			byte[] b = new byte[10];
			is.read(b, 0, b.length);
			String fileCode = bytesToHexString(b);
			Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				if (key.toLowerCase().startsWith(fileCode.toLowerCase())
						|| fileCode.toLowerCase().startsWith(key.toLowerCase())) {
					res = FILE_TYPE_MAP.get(key);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public static String getFileTypeByMime(File file) {
		try {
			MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
			Collection<MimeType> mimeTypes = MimeUtil.getMimeTypes(file);
			List<MimeType> type = new ArrayList<MimeType>(mimeTypes);
			return type.get(0).getSubType();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getFileTypeByMime(new File("D:/test.gif")));
		System.out.println(getFileTypeByMime(new File("D:/question.png")));
		System.out.println(getFileTypeByMime(new File("D:/test.jpg")));
		System.out.println(getFileTypeByMime(new File("D:/test.pdf")));
		System.out.println(getFileTypeByMime(new File("D:/test.docx")));
		System.out.println(getFileTypeByMime(new File("D:/test1.doc")));
		System.out.println(getFileTypeByMime(new File("D:/fonts.tiff")));
	}
}
