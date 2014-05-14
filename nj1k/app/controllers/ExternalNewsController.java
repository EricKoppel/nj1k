package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cached;
import play.libs.F.Promise;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import utils.NYNJTCArticleParserFunction;
import utils.NYNJTCParserFunction;
import flexjson.JSONSerializer;

public class ExternalNewsController extends Controller {

	static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
		}
	};
	
	private static final Logger logger = LoggerFactory.getLogger(ExternalNewsController.class);

	@Cached(duration = 3600, key = "nynjtc")
	public static Promise<Result> getNewsFromNYNJTC() {
		return WS.url(NYNJTCParserFunction.NEWS_PAGE).get().map(new NYNJTCParserFunction(5)).map(list -> {
			logger.debug("Serializing: {}", list);
			JSONSerializer serializer = new JSONSerializer();
			serializer.include("date", "link", "title");
			serializer.exclude("*");
			return ok(serializer.serialize(list));
		});
	};

	public static Promise<Result> getNewsArticleFromNYNJTC(String url) throws MalformedURLException {
		if (!new URL(url).getHost().contains("nynjtc.org")) {
			return Promise.pure(forbidden());
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		
		response().setHeader(CACHE_CONTROL, "public");
		response().setHeader(EXPIRES, dateFormat.get().format(cal.getTime()));
		response().setHeader(LAST_MODIFIED, dateFormat.get().format(new Date(0)));
		
		return WS.url(url).get().map(new NYNJTCArticleParserFunction(url)).map(content -> {
			return ok(content);
		});
	}
}
