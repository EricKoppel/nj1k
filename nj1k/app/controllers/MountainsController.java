package controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;
import utils.DistanceUtil;
import utils.SecurityUtil;

import com.google.common.net.MediaType;

import dto.MountainDistanceBean;
import flexjson.JSONSerializer;

public class MountainsController extends Controller {
	private static final Logger logger = LoggerFactory.getLogger(MountainsController.class);
	
	private static final JSONSerializer serializer;
	private static final JSONSerializer mSerializer;
	
	static {
		serializer = new JSONSerializer();
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
		
		mSerializer = new JSONSerializer();
		mSerializer.include("id", "name");
		mSerializer.exclude("*");
	}

	public static Result mountains() {
		return ok(views.html.mountains.render(MountainEntity.findAll()));
	}

	public static Result mountain(String name) {
		if (name.matches("\\d*")) {
			return movedPermanently(routes.MountainsController.mountain(MountainEntity.find(Long.parseLong(name)).getNameId()));
		}
			
		MountainEntity mountain = MountainEntity.findByName(name);
		
		if (mountain == null) {
			return notFound();
		}
		
		return ok(views.html.mountain.render(mountain, AscentEntity.findByMountainId(mountain.id, 0, 5), findNearestHigherNeighbor(mountain.id),
				AscentDetailEntity.findAscentDetailsByMountain(mountain.id, 0, 8)));
	}
	
	public static Result mountainById(Long id) {
		return movedPermanently(routes.MountainsController.mountain(MountainEntity.find(id).getNameId()));
	}

	public static Result getThumbnail(Long id) {
		MountainEntity mountain = MountainEntity.findThumbnail(id);

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

		return ok(views.html.mountainAscentsPanel.render(mountainId, ascents, page, num));
	}

	public static Result getImages(Long mountainId, int page, int num) {
		List<AscentDetailEntity> details = AscentDetailEntity.findAscentDetailsByMountain(mountainId, page, num);

		if (details.isEmpty()) {
			return noContent();
		} else {
			return ok(views.html.ascentImagePanel.render(mountainId, details, page, num));
		}
	}

	public static Result getImage(Long id) {
		MountainEntity mountain = MountainEntity.findImage(id);

		response().setHeader("Cache-Control", "max-age=31556926, public");
		response().setHeader(LAST_MODIFIED, Application.cacheDateFormat.get().format(mountain.lastUpdate));
		if (mountain.picture != null) {
			return ok(mountain.picture).as(MediaType.ANY_IMAGE_TYPE.type());
		} else {
			return notFound();
		}
	}

	public static Result findNearest(String latitude, String longitude, double threshold) {
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}
		
		if (latitude == null || longitude == null) {
			return badRequest();
		}
		
		logger.debug("Finding peaks near: {}, {}", latitude, longitude);
		
		return ok(mSerializer.serialize(MountainEntity.findAll().stream()
			.map(m -> new MountainDistanceBean(m, null, DistanceUtil.calculateDistance(Float.parseFloat(latitude), Float.parseFloat(longitude), m.latitude, m.longitude)))
			.filter(m -> m.getDistanceAsMiles() < threshold)
			.sorted(Comparator.comparingDouble(MountainDistanceBean::getDistance))
			.map(MountainDistanceBean::getM1)
			.collect(Collectors.toList()))).as("application/json");
	}
	
	public static Result showDistances(Long id, Integer howMany) {
		MountainEntity m = MountainEntity.find(id);
		
		if (m == null) {
			return notFound();
		}
		
		if (request().accepts("text/html")) {
			return ok(views.html.radius.render(m));
		} else if (request().accepts("application/json")) {
			return ok(serializer.serialize(MountainsController.findNearestNeighbors(m, howMany))).as("application/json");
		} else {
			return badRequest();
		}
	}

	public static Result showDistance(Long id1, Long id2) {
		MountainEntity m1 = MountainEntity.find.byId(id1);
		MountainEntity m2 = MountainEntity.find.byId(id2);
		MountainDistanceBean distance = new MountainDistanceBean(m1, m2, DistanceUtil.calculateDistance(m1.latitude, m1.longitude, m2.latitude, m2.longitude));
		return ok(serializer.serialize(distance));
	}

	public static MountainEntity findNearestHigherNeighbor(Long id) {
		MountainEntity m1 = MountainEntity.find.select("elevation").where().eq("id", id).findUnique();
		List<MountainEntity> higherMountains = MountainEntity.find.select("id,name").where().ne("id", m1.id).eq("club_list", true).gt("elevation", m1.elevation).findList();
	
		return higherMountains.stream()
		.map(m2 -> new MountainDistanceBean(m1, m2, DistanceUtil.calculateDistance(m1.latitude, m1.longitude, m2.latitude, m2.longitude)))
		.sorted(Comparator.comparingDouble(MountainDistanceBean::getDistance))
		.findFirst()
		.orElse(new MountainDistanceBean()).getM2();
	}

	public static List<MountainDistanceBean> findNearestNeighbors(MountainEntity mountain, int howMany) {
		List<MountainEntity> neighbors = MountainEntity.find.where().ne("id", mountain.id).eq("club_list", true).findList();
	
		return neighbors.stream()
		.map(m -> new MountainDistanceBean(mountain, m, DistanceUtil.calculateDistance(mountain.latitude, mountain.longitude, m.latitude, m.longitude)))
		.sorted(Comparator.comparingDouble(MountainDistanceBean::getDistance))
		.collect(Collectors.toList()).subList(0, howMany);
	}
}
