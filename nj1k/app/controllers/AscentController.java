package controllers;

import java.util.Collections;
import java.util.List;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;
import models.UserEntity;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUtil;
import utils.MountainComparator;

public class AscentController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AscentController.class);
	final static Form<AscentEntity> ascentForm = form(AscentEntity.class);
	final static List<MountainEntity> sortedMountains = MountainEntity.findAll();
	
	static {
		Collections.sort(sortedMountains, new MountainComparator());
	}
	
	public static Result showTripReport(Long id) {
		return ok(views.html.ascent.render(AscentEntity.findTripReport(id)));
	}
	
	@RequiresAuthentication
	public static Result showForm() {
		return ok(views.html.logascent.render(ascentForm, sortedMountains));
	}

	public static Result editForm(Long id) {
		return ok(views.html.logascent.render(ascentForm.fill(AscentEntity.find(id)), sortedMountains));
	}
	
	@RequiresAuthentication
	public static Result submit() {
		
		Form<AscentEntity> filledForm = ascentForm.bindFromRequest();

		if (!filledForm.hasErrors()) {
			AscentEntity ascent = filledForm.get();
			ascent.climber = UserEntity.find(Long.parseLong(Application.session().get("userId")));
			ascent.ascentDetails = (List<AscentDetailEntity>) ImageUtil.extractPictures(request().body().asMultipartFormData().getFiles(), AscentDetailEntity.class);
			ascent.save();
			
			return redirect(routes.AscentController.showTripReport(ascent.id));
		}
		else {
			logger.debug("Ascent form contained errors: {}", filledForm.errors().toString());
			return badRequest(views.html.logascent.render(filledForm, sortedMountains));
		}
	}
}
