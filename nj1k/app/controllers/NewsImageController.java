package controllers;

import com.google.common.net.MediaType;

import models.NewsImageEntity;
import play.mvc.Controller;
import play.mvc.Result;

public class NewsImageController extends Controller {

	public static Result showNewsImage(Long imgId) {
		return ok(views.html.news_image.render(NewsImageEntity.find.byId(imgId)));
	}
	
	public static Result getImage(Long imgId) {
		return ok(NewsImageEntity.find.byId(imgId).image).as(MediaType.ANY_IMAGE_TYPE.type());
	}
}
