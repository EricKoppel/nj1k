package utils;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.StringUtils;

public class NYNJTCArticleParserFunction implements Function<Response, Promise<String>> {

	private static final Logger logger = LoggerFactory.getLogger(NYNJTCArticleParserFunction.class);
	private String url;

	public NYNJTCArticleParserFunction(String url) throws MalformedURLException {
		this.url = url;
	}

	@Override
	public Promise<String> apply(Response response) throws Exception {
		return WS.url(url).get().map(new Function<WS.Response, String>() {

			@Override
			@SuppressWarnings("unchecked")
			public String apply(Response response) throws Throwable {
				String text = ArticleSentencesExtractor.INSTANCE.getText(new InputStreamReader(response.getBodyAsStream()));
				return TextUtils.extractFirstSentence(text);
			}
		});
	}
}