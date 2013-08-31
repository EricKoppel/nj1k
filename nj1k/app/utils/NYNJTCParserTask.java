package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NYNJTCParserTask implements Callable<List<NYNJTCNewsArticle>> {

	private static final URL NEWS_PAGE;
	
	static {
		try {
			NEWS_PAGE = new URL("http://nynjtc.org/news/news");
		} catch (MalformedURLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	@Override
	public List<NYNJTCNewsArticle> call() throws Exception {
		List<NYNJTCNewsArticle> articles = new ArrayList<NYNJTCNewsArticle>();
		Document doc = Jsoup.parse(NEWS_PAGE, 4096);
		Elements rows = doc.select("div.view-content-parks-table tbody > tr");
		
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
