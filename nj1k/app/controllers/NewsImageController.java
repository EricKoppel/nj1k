package controllers;

import models.NewsImageEntity;
import play.mvc.Controller;
import play.mvc.Result;

public class NewsImageController extends Controller {

	public static Result showNewsImage(Long imgId) {
		return ok(views.html.news_image.render(NewsImageEntity.find.byId(imgId)));
	}
	
	public static Result getImage(Long imgId) {
		return ok(NewsImageEntity.find.byId(imgId).image);
	}
}
