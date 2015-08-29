package controllers;

import models.AscentDetailEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.ImageUtil;
import utils.SecurityUtil;
import actions.ETagAction;

import com.google.common.net.MediaType;

public class AscentDetailController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AscentDetailController.class);

	public static Result upload() {

		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}

		ImageUtil.extractPictures(request(), AscentDetailEntity.class).forEach(pic -> {
			pic.save();
			logger.info("Pic id: {}", pic.id);
		});

		return ok();
	}

	@With(ETagAction.class)
	public static Result getImage(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.findImage(id);

		if (ascentDetail == null) {
			return notFound();
		}

		response().setHeader(CACHE_CONTROL, "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(ascentDetail.lastUpdate));
		return ok(ascentDetail.image).as(MediaType.ANY_IMAGE_TYPE.type());
	}

	@With(ETagAction.class)
	public static Result getThumbnail(Long id) {
		AscentDetailEntity ascentDetail = AscentDetailEntity.findThumbnail(id);

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
