package controllers;

import static play.data.Form.form;

import java.util.List;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;
import models.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUtil;
import utils.SecurityUtil;

public class AscentController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AscentController.class);
	private static final Form<AscentEntity> ascentForm = form(AscentEntity.class);

	public static Result showTripReport(Long id) {
		AscentEntity e = AscentEntity.findTripReport(id);

		if (e != null) {
			return ok(views.html.ascent.render(e));
		} else {
			return notFound();
		}
	}

	public static Result showForm() {
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}

		return ok(views.html.logascent.render(ascentForm, MountainEntity.findAll()));
	}

	public static Result editForm(Long id) throws Exception {
		if (!SecurityUtil.ownsAscent(id)) {
			return forbidden();
		}

		return ok(views.html.logascent.render(ascentForm.fill(AscentEntity.find(id)), MountainEntity.findAll()));
	}

	public static Result updateTripReport(Long id) {
		if (!SecurityUtil.ownsAscent(id)) {
			return forbidden();
		}

		return TODO;
	}

	public static Result remove(Long id) {
		if (!SecurityUtil.ownsAscent(id)) {
			return forbidden();
		}

		AscentEntity.find(id).delete();
		return ok();
	}

	@SuppressWarnings("unchecked")
	public static Result submit() throws Exception {
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}

		Form<AscentEntity> filledForm = ascentForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(views.html.logascent.render(filledForm, MountainEntity.findAll()));
		}

		AscentEntity ascent = filledForm.get();
		ascent.climber = UserEntity.findByEmail(session().get(SecurityUtil.USER_ID_KEY));
		ascent.ascentDetails.addAll(ImageUtil.extractPictures(request().body().asMultipartFormData().getFiles(), AscentDetailEntity.class));

		if (ascent.id == null) {
			ascent.save();
		} else {
			ascent.update();
		}

		return redirect(routes.AscentController.showTripReport(ascent.id));
	}
}
