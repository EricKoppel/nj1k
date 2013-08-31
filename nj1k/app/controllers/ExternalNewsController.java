package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cached;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.NYNJTCNewsArticle;
import utils.NYNJTCParserTask;
import flexjson.JSONSerializer;

public class ExternalNewsController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(ExternalNewsController.class);
	
	@Cached(key = "nynjtc", duration = 3600)
	public static Result getNewsFromNYNJTC() {
		final JSONSerializer serializer = new JSONSerializer();
		serializer.include("date","link","title");
		serializer.exclude("*");
		Promise<List<NYNJTCNewsArticle>> promise = play.libs.Akka.future(new NYNJTCParserTask());
		return async(promise.map(new Function<List<NYNJTCNewsArticle>, Result>() {
			public Result apply(List<NYNJTCNewsArticle> articles) {
				logger.debug("Got result: {}", articles);
				return ok(serializer.serialize(articles));
			}
		}));
	}
}
