package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Function;
import play.libs.WS.Response;

public class NYNJTCArticleParserFunction implements Function<Response, String> {

	private static final Logger logger = LoggerFactory.getLogger(NYNJTCArticleParserFunction.class);
	private String url;

	public NYNJTCArticleParserFunction(String url) {
		this.url = url;
	}

	@Override
	public String apply(Response response) throws Exception {
		Document doc = Jsoup.parse(response.getBodyAsStream(), "UTF-8", url);

		doc.select("p:matchesOwn((?is)\u00a0)").remove();
		Elements paragraphs = doc.select("#main .content p");

		for (Element e : paragraphs) {
			if (!e.text().trim().isEmpty()) {
				logger.debug("Text: {}", e.text().trim());
				return e.text().trim();
			}
		}

		Element paragraph = doc.select("#main .content .link-title").first();

		if (paragraph != null) {
			return paragraph.text().trim();
		}
		
		return "";
	}
}
