package controllers;

import models.AscentEntity;
import models.MountainEntity;
import play.mvc.Controller;
import play.mvc.Result;
import flexjson.JSONSerializer;

public class MountainsController extends Controller {

	public static Result list() {
		return ok(views.html.mountains.render(MountainEntity.findAll()));
	}
	
	public static Result showMountain(Long id) {
		return ok(views.html.mountain.render(MountainEntity.findWithDetails(id), AscentEntity.findByMountainId(id), MountainEntity.findNearestHigherNeighbor(id)));
	}
	
	public static Result showDistances(Long id, Long howMany) {
		JSONSerializer serializer = new JSONSerializer();
		
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
		return ok(serializer.serialize(MountainEntity.findNearestNeighbors(id, howMany)));
	}
	
	public static Result showDistance(Long id1, Long id2) {
		JSONSerializer serializer = new JSONSerializer();
		
		serializer.include("distanceAsMiles", "m1.id", "m2.id", "m1.name", "m2.name", "m1.latitude", "m1.longitude", "m2.latitude", "m2.longitude");
		serializer.exclude("*");
		return ok(serializer.serialize(MountainEntity.findDistanceBetweenMountains(id1, id2)));
	}
}
