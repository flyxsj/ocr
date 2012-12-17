package com.faceyspacey.analyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.faceyspacey.domain.QuestionAndAnswer;
import com.faceyspacey.util.ToolUtil;

public class TextAnalyser implements IAnalyser {
	private static final Pattern answerPrefixPattern = Pattern.compile("^[0-9a-zA-Z]+\\).*");

	public List<QuestionAndAnswer> analyseText(String text) throws Exception {
		//remove the redundant \n
		text = text.replaceAll("[\r|\n]{3,}", "\n\n");
		List<String> lineList = readLine(text);
		Map<Integer, List<String>> questionMap = new LinkedHashMap<Integer, List<String>>();
		Map<String, List<String>> answerMap = new LinkedHashMap<String, List<String>>();
		boolean isAnswer = false;
		int qIndex = 0;
		int aIndex = 0;
		for (int i = 0; i < lineList.size(); i++) {
			String item = lineList.get(i);
			if (item.trim().length() == 0) {
				continue;
			}
			if (isAnswerPrefix(item) == true || (isAnswerPrefix(item) == false && isAnswer == true)) {
				isAnswer = true;
				String key = qIndex + "_" + aIndex;
				setAnswerLine(answerMap, item, key);
				int nextOne = i + 1;
				if (nextOne <= lineList.size() - 1) {
					if (lineList.get(nextOne).trim().length() == 0) {
						aIndex++;
						int nextTwo = i + 2;
						if (nextTwo <= lineList.size() - 1) {
							if (isAnswerPrefix(lineList.get(nextTwo)) == false) {
								isAnswer = false;
								aIndex = 0;
								qIndex++;
							} else {
								
							}
						}
					}else if(isAnswerPrefix(lineList.get(nextOne)) == true){
						aIndex++;
					}
				}
			} else if (isAnswerPrefix(item) == false && isAnswer == false) {
				setQuestionLine(questionMap, qIndex, item);
			} 
		}
		List<QuestionAndAnswer> result = convertMap2ResultList(questionMap, answerMap);
		return result;
	}

	/**
	 * convert the map result to list result
	 * @param questionMap
	 * @param answerMap
	 * @return
	 */
	private List<QuestionAndAnswer> convertMap2ResultList(Map<Integer, List<String>> questionMap,
			Map<String, List<String>> answerMap) {
		List<QuestionAndAnswer> result = new ArrayList<QuestionAndAnswer>();
		Iterator<Entry<Integer, List<String>>> iterator = questionMap.entrySet().iterator();
		int index = 1;
		while (iterator.hasNext()) {
			Map.Entry<Integer, List<String>> entry = (Map.Entry<Integer, List<String>>) iterator.next();
			QuestionAndAnswer qaa = new QuestionAndAnswer();
			Integer qKey = entry.getKey();
			String question = StringUtils.join(entry.getValue(), ToolUtil.EOL);
			qaa.setIndex(index);
			qaa.setQuestion(question);
			List<String> answerList = new ArrayList<String>();
			Iterator<String> answerIt = answerMap.keySet().iterator();
			while (answerIt.hasNext()) {
				String answerKey = (String) answerIt.next();
				if(answerKey.startsWith(qKey + "_")){
					answerList.add(StringUtils.join(answerMap.get(answerKey), ToolUtil.EOL));
				}
			}
			qaa.setAnswerList(answerList);
			result.add(qaa);
			index++;
		}
		return result;
	}

	private List<String> readLine(String text) throws IOException {
		List<String> lineList = new ArrayList<String>();
		StringReader sr = new StringReader(text);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {
			lineList.add(line);
		}
		sr.close();
		br.close();
		return lineList;
	}

	private void setQuestionLine(Map<Integer, List<String>> questionMap, int qIndex, String item) {
		if (questionMap.containsKey(qIndex)) {
			questionMap.get(qIndex).add(item);
		} else {
			List<String> tmpList = new ArrayList<String>();
			tmpList.add(item);
			questionMap.put(qIndex, tmpList);
		}
	}

	private void setAnswerLine(Map<String, List<String>> answerMap, String item, String key) {
		if (answerMap.containsKey(key)) {
			answerMap.get(key).add(item);
		} else {
			List<String> tmpList = new ArrayList<String>();
			tmpList.add(item);
			answerMap.put(key, tmpList);
		}
	}

	public static void main(String[] args) {
		String tmp = "1";
		Matcher m = answerPrefixPattern.matcher(tmp);
		System.out.println(m.matches());
	}

	private boolean isAnswerPrefix(String line) {
		Matcher m = answerPrefixPattern.matcher(line);
		return m.matches();
	}

}
