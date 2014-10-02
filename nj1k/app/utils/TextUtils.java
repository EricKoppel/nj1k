package utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.StringUtils;

public class TextUtils {
	
	public static String extractSentence(String text, int howMany) {
		DocumentPreprocessor pr = new DocumentPreprocessor(new StringReader(text));
		Iterator<List<HasWord>> it = pr.iterator();
		List<String> sentences = new ArrayList<String>();
		
		int i = 0;
		
		while (i < howMany && it.hasNext()) {
			List firstSentence = it.next();
			sentences.add(StringUtils.joinWithOriginalWhiteSpace(firstSentence));
			i++;
		}
		
		return sentences.stream().collect(Collectors.joining(" "));
	}
}
