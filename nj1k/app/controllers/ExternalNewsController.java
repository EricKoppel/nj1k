package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Promise;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import utils.NYNJTCArticleParserFunction;
import utils.NYNJTCParserFunction;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;

public class ExternalNewsController extends Controller {
	private static final JSONSerializer serializer;
	private static final Logger logger = LoggerFactory.getLogger(ExternalNewsController.class);

	static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
		}
	};

	static {
		serializer = new JSONSerializer();
		serializer.include("date", "link", "title");
		serializer.exclude("*");
	}

	public static Promise<Result> getNewsFromNYNJTC() {
		
		response().setHeader(CACHE_CONTROL, "max-age=3600, public");
		
		return WS.url(NYNJTCParserFunction.NEWS_PAGE).get().map(new NYNJTCParserFunction(5)).map(list -> {
			logger.debug("Serializing: {}", list);
			return ok(serializer.serialize(list)).as(MediaType.JSON_UTF_8.toString());
		});
	};

	public static Promise<Result> getNewsArticleFromNYNJTC(String url) throws MalformedURLException {
		if (!new URL(url).getHost().contains("nynjtc.org")) {
			return Promise.pure(forbidden());
		}
		
		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");
		
		return WS.url(url).get().flatMap(new NYNJTCArticleParserFunction(url)).map(content -> {
			return ok(content);
		});
	}
}
