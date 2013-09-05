package controllers;

import models.AscentDetailEntity;
import models.AscentEntity;
import models.MountainEntity;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;

public class MountainsController extends Controller {

	public static Result list() {
		return ok(views.html.mountains.render(MountainEntity.findAll()));
	}
	
	public static Result showMountain(Long id) {
		return ok(views.html.mountain.render(MountainEntity.findWithDetails(id), AscentEntity.findByMountainId(id), MountainEntity.findNearestHigherNeighbor(id), AscentDetailEntity.findAscentDetailsByMountain(id)));
	}
	
	public static Result getImage(Long id) {
		MountainEntity ascentDetail = MountainEntity.find(id);
		if (ascentDetail.picture != null) {
			return ok(ascentDetail.picture).as(MediaType.ANY_IMAGE_TYPE.type());
		} else {
			return notFound();
		}
	}
	
	public static Result getImagePopup(Long id) {
		return ok(views.html.mountain_image.render(MountainEntity.find(id)));
	}
	
	public static Result showDistances(Long id, Long howMany) {
		JSONSerializer serializer = new JSONSerializer();
		
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
		return ok(serializer.serialize(MountainEntity.findNearestNeighbors(id, howMany)));
	}
	
	public static Result showRadius(Long id) {
		return ok(views.html.radius.render(MountainEntity.find(id)));
	}
	
	public static Result showDistance(Long id1, Long id2) {
		JSONSerializer serializer = new JSONSerializer();
		
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
		return ok(serializer.serialize(MountainEntity.findDistanceBetweenMountains(id1, id2)));
	}
}
