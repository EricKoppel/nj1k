package utils;

import static play.libs.Akka.future;
import static play.mvc.Results.async;
import static play.mvc.Results.ok;

import java.util.List;

import play.libs.F.Function;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Result;
import flexjson.JSONSerializer;

public class NYNJTCParserFunction implements Function<WS.Response, Result> {
	public static final String NEWS_PAGE = "http://www.nynjtc.org/news/news";

	@Override
	public Result apply(Response response) throws Throwable {
		return async(future(new NYNJTCParserTask(response.getBodyAsStream())).map(new Function<List<NYNJTCNewsArticle>, Result>() {
			@Override
			public Result apply(List<NYNJTCNewsArticle> articles) throws Throwable {
				JSONSerializer serializer = new JSONSerializer();
				serializer.include("date","link","title");
				serializer.exclude("*");	
				return ok(serializer.serialize(articles));
			}
		}));
	}
}
