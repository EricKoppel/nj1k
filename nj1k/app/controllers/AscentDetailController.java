package controllers;


import java.util.List;

import models.AscentDetailEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUtil;
import utils.SecurityUtil;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;

public class AscentDetailController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AscentDetailController.class);
	
	public static Result upload() {
		
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}
		
		List<AscentDetailEntity> resizedImages = ImageUtil.extractPictures(request(), AscentDetailEntity.class);
		
		for (AscentDetailEntity pic : resizedImages) {
			pic.save();
			
			logger.info("Pic id: {}", pic.id);
		}
		
		JSONSerializer serializer = new JSONSerializer();
		
		serializer.include("*");
		serializer.exclude("class");
		
		return ok();
	}
	
	public static Result getImage(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		
		if (ascentDetail == null) {
			return notFound();
		}
		
		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(ascentDetail.lastUpdate));
		return ok(ascentDetail.image).as(MediaType.ANY_IMAGE_TYPE.type());
	}
	
	public static Result getThumbnail(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		
		if (ascentDetail == null) {
			return notFound();
		}
		
		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(ascentDetail.lastUpdate));
		
		byte[] img = ascentDetail.thumbnail != null ? ascentDetail.thumbnail : ascentDetail.image;
		return ok(img).as(MediaType.ANY_IMAGE_TYPE.type());
	}
	
	public static Result updateCaption(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		
		if (!SecurityUtil.ownsAscent(ascentDetail.ascent.id)) {
			return forbidden();
		}
		
		ascentDetail.caption = request().body().asText();
		ascentDetail.update();
		return ok();
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
