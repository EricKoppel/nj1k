package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.libs.F.Function;
import play.libs.WS.Response;
import play.mvc.Controller;

public class NYNJTCParserFunction implements Function<Response, NYNJTCNewsArticles> {

	private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			return new SimpleDateFormat(Configuration.root().getString("render.date.format"));
		}
	}; 
	
	private static final Logger logger = LoggerFactory.getLogger(NYNJTCParserFunction.class);
	public static final String NEWS_PAGE = "http://nynjtc.org/news/news";
	
	private int maxResults;
	
	public NYNJTCParserFunction(int maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public NYNJTCNewsArticles apply(Response response) throws Exception {
		Document doc = Jsoup.parse(response.getBodyAsStream(), null, NEWS_PAGE);
		
		Elements rows = doc.select("div.view-content-parks-table tbody > tr:lt(" + maxResults + ")");
		
		List<NYNJTCNewsArticle> list = rows.stream().parallel().map(row ->
		new NYNJTCNewsArticle(extractTitle(row), extractDate(row), extractURL(row))).sorted().collect(Collectors.toList());
		
		NYNJTCNewsArticles articles = new NYNJTCNewsArticles();
		articles.setEtag(response.getHeader(Controller.ETAG));
		articles.setLastModified(response.getHeader(Controller.LAST_MODIFIED));
		articles.setArticles(list);
		
		return articles;
	}
	
	private Date extractDate(Element row) {
		try {
			return dateFormat.get().parse(row.child(0).getElementsByTag("span").get(0).text());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String extractTitle(Element row) {
		return row.child(2).getElementsByTag("a").get(0).text();
	}
	
	private String extractURL(Element row) {
		return row.child(2).getElementsByTag("a").attr("abs:href");
	}
}
