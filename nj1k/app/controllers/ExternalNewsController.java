package controllers;

import play.cache.Cached;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import utils.NYNJTCParserFunction;

public class ExternalNewsController extends Controller {
	@Cached(key = "nynjtc", duration = 3600)
	public static Result getNewsFromNYNJTC() {
		return async(WS.url(NYNJTCParserFunction.NEWS_PAGE).get().map(new NYNJTCParserFunction()));
	}
}
