package com.faceyspacey.analyser;

import java.util.List;

import com.faceyspacey.domain.QuestionAndAnswer;

public interface IAnalyser {
	public List<QuestionAndAnswer> analyseText(String text) throws Exception;
}
