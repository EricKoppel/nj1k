package utils;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Function;
import play.libs.WS.Response;

public class NYNJTCParserFunction implements Function<Response, List<NYNJTCNewsArticle>> {

	private static final Logger logger = LoggerFactory.getLogger(NYNJTCParserFunction.class);
	public static final String NEWS_PAGE = "http://nynjtc.org/news/news";
	
	private int maxResults;
	
	public NYNJTCParserFunction(int maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public List<NYNJTCNewsArticle> apply(Response response) throws Exception {
		List<NYNJTCNewsArticle> articles = new ArrayList<NYNJTCNewsArticle>();
		Document doc = Jsoup.parse(response.getBodyAsStream(), null, NEWS_PAGE);
		Elements rows = doc.select("div.view-content-parks-table tbody > tr:lt(" + maxResults + ")");
		
		for (Element row : rows) {
			articles.add(new NYNJTCNewsArticle(extractTitle(row), extractDate(row), extractURL(row)));
		}

		return articles;
	}
	
	private String extractDate(Element row) throws ParseException {
		return row.child(0).getElementsByTag("span").get(0).text();
	}
	
	private String extractTitle(Element row) {
		return row.child(2).getElementsByTag("a").get(0).text();
	}
	
	private String extractURL(Element row) throws MalformedURLException {
		return row.child(2).getElementsByTag("a").attr("abs:href");
	}
}
