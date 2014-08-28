package controllers;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.libs.F.Promise;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Http.Response;
import play.mvc.Result;
import utils.NYNJTCArticleParserFunction;
import utils.NYNJTCParserFunction;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

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
		serializer.transform(new DateTransformer(Configuration.root().getString("render.date.format")), Date.class);
		serializer.include("date", "link", "title");
		serializer.exclude("*");
	}

	public static Promise<Result> getNewsFromNYNJTC() {
		final Response response = response();
		
		response.setHeader(CACHE_CONTROL, "max-age=3600, public");
		return WS.url(NYNJTCParserFunction.NEWS_PAGE).get().map(new NYNJTCParserFunction(5)).map(articles -> {
			response.setHeader(ETAG, articles.getEtag());
			response.setHeader(LAST_MODIFIED, articles.getLastModified());

			if (articles.getEtag() != null && articles.getEtag().equals(request().getHeader(IF_NONE_MATCH))) {
				return status(NOT_MODIFIED);
			} else {
				logger.debug("Serializing: {}", articles.getArticles());
				String ret = serializer.serialize(articles.getArticles());
				return ok(ret).as(MediaType.JSON_UTF_8.toString());
			}
		});
	};

	public static Promise<Result> getNewsArticleFromNYNJTC(String url) throws MalformedURLException, UnsupportedEncodingException {
		if (!new URL(URLDecoder.decode(url, "UTF-8")).getHost().contains("nynjtc.org")) {
			return Promise.pure(forbidden());
		}

		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");

		return WS.url(url).get().flatMap(new NYNJTCArticleParserFunction(url)).map(content -> {
			return ok(content);
		});
	}
}
