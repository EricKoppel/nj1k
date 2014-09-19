package controllers;

import java.util.List;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;

public class MountainsController extends Controller {
	private static final Logger logger = LoggerFactory.getLogger(MountainsController.class);
	
	private static final JSONSerializer serializer;

	static {
		serializer = new JSONSerializer();
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
	}

	public static Result list() {
		return ok(views.html.mountains.render(MountainEntity.findAll()));
	}

	public static Result showMountain(Long id) {
		MountainEntity mountain = MountainEntity.find(id);

		if (mountain == null) {
			return notFound();
		}

		return ok(views.html.mountain.render(mountain, AscentEntity.findByMountainId(mountain.id), MountainEntity.findNearestHigherNeighbor(mountain.id),
				AscentDetailEntity.findAscentDetailsByMountain(mountain.id, 0, 4)));
	}

	public static Result showMountainByName(String name) {
		String mountainName = name.replace("-", " ");
		MountainEntity mountain = MountainEntity.findByName(name);
		return ok(views.html.mountain.render(mountain, AscentEntity.findByMountainId(mountain.id), MountainEntity.findNearestHigherNeighbor(mountain.id),
				AscentDetailEntity.findAscentDetailsByMountain(mountain.id)));
	}

	public static Result getThumbnail(Long id) {
		MountainEntity mountain = MountainEntity.find(id);

		response().setHeader("Cache-Control", "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(mountain.lastUpdate));

		if (mountain.thumbnail != null) {
			return ok(mountain.thumbnail).as(MediaType.ANY_IMAGE_TYPE.type());
		} else if (mountain.picture != null) {
			return ok(mountain.picture).as(MediaType.ANY_IMAGE_TYPE.type());
		}

		return notFound();
	}

	public static Result getAscents(Long mountainId, int page, int num) {
		List<AscentEntity> ascents = AscentEntity.findByMountainId(mountainId, page, num);

		if (ascents.isEmpty()) {
			return noContent();
		}

		return ok(views.html.ascentPanel.render(mountainId, ascents, page + 1, num));
	}

	public static Result getImages(Long mountainId, int page, int num) {
		List<AscentDetailEntity> details = AscentDetailEntity.findAscentDetailsByMountain(mountainId, page, num);

		if (details.isEmpty()) {
			return noContent();
		} else {
			return ok(views.html.ascentImagePanel.render(mountainId, details, page + 1, num));
		}
	}

	public static Result getImage(Long id) {
		MountainEntity mountain = MountainEntity.find(id);

		response().setHeader("Cache-Control", "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(mountain.lastUpdate));
		if (mountain.picture != null) {
			return ok(mountain.picture).as(MediaType.ANY_IMAGE_TYPE.type());
		} else {
			return notFound();
		}
	}

	public static Result showDistances(Long id, Long howMany) {
		logger.debug("Accepts: {}", request().acceptedTypes());
		if (request().accepts("text/html")) {
			return ok(views.html.radius.render(MountainEntity.find(id)));
		} else if (request().accepts("application/json")) {
			return ok(serializer.serialize(MountainEntity.findNearestNeighbors(id, howMany))).as(MediaType.JSON_UTF_8.type());
		} else {
			return badRequest();
		}
	}

	public static Result showDistance(Long id1, Long id2) {
		return ok(serializer.serialize(MountainEntity.findDistanceBetweenMountains(id1, id2)));
	}
}
