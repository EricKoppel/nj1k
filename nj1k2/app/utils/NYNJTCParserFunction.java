package utils;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import play.libs.F.Function;
import play.libs.ws.WSResponse;

public class NYNJTCParserFunction implements Function<WSResponse, List<NYNJTCNewsArticle>> {
	
	private int maxResults;
	
	public NYNJTCParserFunction(int maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public List<NYNJTCNewsArticle> apply(WSResponse response) throws Exception {
		Document doc = Jsoup.parse(response.getBodyAsStream(), null, response.getUri().toURL().toString());
		Elements rows = doc.select("div.view-content-parks-table tbody > tr:lt(" + maxResults + ")");
		
		List<NYNJTCNewsArticle> list = rows.stream().parallel()
			.map(row -> new NYNJTCNewsArticle(NYNJTCNewsArticle.extractTitle(row), NYNJTCNewsArticle.extractDate(row), NYNJTCNewsArticle.extractURL(row)))
			.sorted()
			.collect(Collectors.toList());
		
		return list;
	}

}
