package utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NYNJTCParserTask implements Callable<List<NYNJTCNewsArticle>> {

	private static final Logger logger = LoggerFactory.getLogger(NYNJTCParserTask.class);
	private static final String NEWS_PAGE = "http://nynjtc.org/news/news";
	
	private InputStream in;
	
	public NYNJTCParserTask(InputStream in) {
		this.in = in;
	}
	
	@Override
	public List<NYNJTCNewsArticle> call() throws Exception {
		List<NYNJTCNewsArticle> articles = new ArrayList<NYNJTCNewsArticle>();
		Document doc = Jsoup.parse(in, null, NEWS_PAGE);
		Elements rows = doc.select("div.view-content-parks-table tbody > tr");
		
		for (Element row : rows) {
			articles.add(new NYNJTCNewsArticle(extractTitle(row), extractDate(row), extractURL(row)));
		}
		
		logger.debug("Found response\n\n{}", articles);
		
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
