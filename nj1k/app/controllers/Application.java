package controllers;

import models.AscentEntity;
import models.NewsEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	public static Result index() {
		return ok(views.html.index.render(NewsEntity.findAll(), AscentEntity.findRecent(10)));
	}
	
	public static Result contact() {
		return TODO;
	}
	
	public static Result about() {
		return TODO;
	}
	
	public static Result links() {
		return TODO;
	}
	
	public static Result javascriptRoutes() {
		return ok(Routes.javascriptRouter("jsRoutes", 
				controllers.routes.javascript.MountainsController.showDistances(),
				controllers.routes.javascript.MountainsController.showMountain())).as("text/javascript");
	}
}