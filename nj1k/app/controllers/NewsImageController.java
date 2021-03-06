package controllers;

import models.NewsImageEntity;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.net.MediaType;

public class NewsImageController extends Controller {
	
	public static Result getImage(Long imgId) {
		return ok(NewsImageEntity.find.byId(imgId).image).as(MediaType.ANY_IMAGE_TYPE.type());
	}
}
