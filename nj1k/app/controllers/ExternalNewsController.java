package controllers;

import java.io.InputStreamReader;
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
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.NYNJTCParserFunction;
import utils.TextUtils;
import actions.ETagAction;

import com.google.common.net.MediaType;

import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

public class ExternalNewsController extends Controller {
	private static final JSONSerializer serializer;
	private static final Logger logger = LoggerFactory.getLogger(ExternalNewsController.class);
	private static final URL NEWS_PAGE;
	
	static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
		}
	};

	static {
		try {
			NEWS_PAGE = new URL(Play.application().configuration().getString("nynjtcNewsPage"));
		} catch (MalformedURLException e) {
			throw new ExceptionInInitializerError(e);
		}
		
		serializer = new JSONSerializer();
		serializer.transform(new DateTransformer(Configuration.root().getString("render.date.format")), Date.class);
		serializer.include("date", "link", "title");
		serializer.exclude("*");
	}

	@With(ETagAction.class)
	public static Promise<Result> getNewsFromNYNJTC() {
		return WS.url(NEWS_PAGE.toString()).get().map(new NYNJTCParserFunction(5)).map(articles -> {
			String ret = serializer.serialize(articles);
			return ok(ret).as(MediaType.JSON_UTF_8.toString());
		});
	};

	public static Promise<Result> getNewsArticleFromNYNJTC(String url) throws MalformedURLException, UnsupportedEncodingException {
		if (!new URL(URLDecoder.decode(url, "UTF-8")).getHost().contains(NEWS_PAGE.getHost())) {
			return Promise.pure(forbidden());
		}

		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");

		return WS.url(url).get()
			.map(r -> TextUtils.extractSentence(ArticleSentencesExtractor.INSTANCE.getText(new InputStreamReader(r.getBodyAsStream())), 1))
			.map(content -> ok(content));
	}
}
