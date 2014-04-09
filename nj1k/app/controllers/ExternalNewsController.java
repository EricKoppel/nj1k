package controllers;

import play.cache.Cached;
import play.libs.F.Promise;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import utils.NYNJTCParserFunction;
import flexjson.JSONSerializer;

public class ExternalNewsController extends Controller {
	@Cached(key = "nynjtc", duration = 3600)
	public static Promise<Result> getNewsFromNYNJTC() {

		return WS.url(NYNJTCParserFunction.NEWS_PAGE).get().map(new NYNJTCParserFunction()).map(articles -> {
			JSONSerializer serializer = new JSONSerializer();
			serializer.include("date", "link", "title");
			serializer.exclude("*");
			return ok(serializer.serialize(articles));
		});
	}
}
