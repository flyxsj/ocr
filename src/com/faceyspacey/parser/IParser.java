package com.faceyspacey.parser;

import java.io.File;

public interface IParser {

	public static final int BUFFER_SIZE = 4096;
	public String getFileType();

	public String extractText(File file) throws Exception;
}
