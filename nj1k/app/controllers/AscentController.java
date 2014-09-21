package controllers;

import static play.data.Form.form;

import java.sql.Timestamp;
import java.util.List;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;
import models.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.FirstSentenceTransformer;
import utils.HasPictureTransformer;
import utils.ImageUtil;
import utils.SecurityUtil;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

public class AscentController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AscentController.class);
	private static final Form<AscentEntity> ascentForm = form(AscentEntity.class);
	private static final JSONSerializer serializer;
	
	static {
		serializer = new JSONSerializer();
		serializer.include("id", "ascent_date", "mountain.id", "mountain.name", "climber.id", "climber.name", "climber.pic", "trip_report");
		serializer.exclude("*");
		serializer.transform(new DateTransformer(Configuration.root().getString("render.date.format")), Timestamp.class);
		serializer.transform(new FirstSentenceTransformer(), "trip_report");
		serializer.transform(new HasPictureTransformer(), "climber.pic");	
	}
	
	public static Result ascent(Long id) {
		AscentEntity e = AscentEntity.findTripReport(id);

		if (e != null) {
			return ok(views.html.ascent.render(e));
		} else {
			return notFound();
		}
	}
	
	public static Result ascents(int page, int size) {
		List<AscentEntity> ascents = AscentEntity.findAscents(page, size);
		
		if (request().accepts(MediaType.HTML_UTF_8.type())) {
			return ok(views.html.recentAscentsPanel.render(ascents, page + 1, size));
		} else {
			return ok(serializer.serialize(ascents)).as(MediaType.JSON_UTF_8.toString());	
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

	public static Result submit() throws Exception {
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}

		Form<AscentEntity> filledForm = ascentForm.bindFromRequest();
		
		if (filledForm.hasErrors()) {
			return badRequest(views.html.logascent.render(filledForm, MountainEntity.findAll()));
		}

		AscentEntity form = filledForm.get();
		form.climber = UserEntity.findByEmail(session().get(SecurityUtil.USER_ID_KEY));
		form.ascentDetails.addAll(ImageUtil.extractPictures(request(), AscentDetailEntity.class));

		
		if (form.id == null) {
			form.save();
		} else if (SecurityUtil.ownsAscent(form.id)) {
			AscentEntity ascent = AscentEntity.find(form.id);
			ascent.ascentDetails.addAll(form.ascentDetails);
			ascent.ascent_date = form.ascent_date;
			ascent.trip_report = form.trip_report;
			ascent.successful = form.successful;
			ascent.update();
		}

		return redirect(routes.AscentController.ascent(form.id));
	}
}
