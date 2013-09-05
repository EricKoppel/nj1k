package controllers;


import java.util.List;

import models.AscentDetailEntity;
import play.mvc.Controller;
import play.mvc.Result;
import utils.SecurityUtil;

import com.google.common.net.MediaType;

public class AscentDetailController extends Controller {

	public static Result getImage(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		return ok(ascentDetail.image).as(MediaType.ANY_IMAGE_TYPE.type());
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
	
	public static Result showMountainImagesForAscentDetail(Long ascentDetailId) {
		AscentDetailEntity e = AscentDetailEntity.find(ascentDetailId);
		List<AscentDetailEntity> ascentDetails = AscentDetailEntity.findAscentDetailsByMountain(e.ascent.mountain.id);
		int start = 0;
		
		for (int i = 0; i < ascentDetails.size(); i++) {
			if (ascentDetails.get(i).id.equals(ascentDetailId)) {
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
		AscentDetailEntity detail = AscentDetailEntity.find(id);
		
		if (!SecurityUtil.ownsAscent(detail.ascent.id)) {
			return forbidden();
		}
		
		detail.delete();
		return ok();
	}
}
