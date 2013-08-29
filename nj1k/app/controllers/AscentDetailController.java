package controllers;


import java.util.List;

import models.AscentDetailEntity;
import play.mvc.Controller;
import play.mvc.Result;

public class AscentDetailController extends Controller {

	public static Result getImage(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		return ok(ascentDetail.image);
	}
	
	public static Result showImage(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		List<AscentDetailEntity> ascentDetails = ascentDetail.ascent.ascentDetails;
		int start = 0;
		
		for (int i = 0; i < ascentDetails.size(); i++) {
			if (ascentDetails.get(i).id.equals(id)) {
				start = i;
				break;
			}
		}
		
		return ok(views.html.ascent_image.render(ascentDetails, start));
	}
	
	public static Result showCaption(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		
		return TODO;
	}
	
	public static Result remove(Long id) {
		AscentDetailEntity.find(id).delete();
		return ok();
	}
}
