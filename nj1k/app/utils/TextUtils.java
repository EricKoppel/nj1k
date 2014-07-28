package utils;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.StringUtils;

public class TextUtils {

	public static String extractFirstSentence(String text) {
		DocumentPreprocessor pr = new DocumentPreprocessor(new StringReader(text));

		Iterator<List<HasWord>> i = pr.iterator();
		if (i.hasNext()) {
			List firstSentence = i.next();
			return StringUtils.joinWithOriginalWhiteSpace(firstSentence);
		} else {
			return "";
		}
	}
}
