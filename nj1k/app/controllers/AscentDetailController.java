package controllers;

import org.apache.shiro.play.bind.ShiroBinderAction;

import models.AscentDetailEntity;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@With(ShiroBinderAction.class)
public class AscentDetailController extends Controller {

	public static Result showImage(Long id) {
		
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);

		return ok(ascentDetail.image);
	}
	
	public static Result showCaption(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.find(id);
		
		return TODO;
	}
}
