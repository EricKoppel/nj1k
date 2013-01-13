package controllers;

import static play.data.Form.form;

import java.util.Collections;
import java.util.List;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;
import models.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.utils.PluginUtils;

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

	@SubjectPresent
	public static Result showForm() {
		return ok(views.html.logascent.render(ascentForm, sortedMountains));
	}

	@SubjectPresent
	public static Result editForm(Long id) {
		return ok(views.html.logascent.render(ascentForm.fill(AscentEntity.find(id)), sortedMountains));

	}

	@SubjectPresent
	public static Result submit() throws Exception {

		Form<AscentEntity> filledForm = ascentForm.bindFromRequest();

		if (!filledForm.hasErrors()) {
			AscentEntity ascent = filledForm.get();
			ascent.climber = (UserEntity) PluginUtils.getDeadboltHandler().getSubject(Controller.ctx());
			ascent.ascentDetails = (List<AscentDetailEntity>) ImageUtil.extractPictures(request().body().asMultipartFormData().getFiles(), AscentDetailEntity.class);
			ascent.save();

			return redirect(routes.AscentController.showTripReport(ascent.id));
		} else {
			logger.debug("Ascent form contained errors: {}", filledForm.errors().toString());
			return badRequest(views.html.logascent.render(filledForm, sortedMountains));
		}
	}
}
