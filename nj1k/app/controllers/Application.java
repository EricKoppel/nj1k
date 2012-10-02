package controllers;

import models.AscentEntity;
import models.NewsEntity;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	public static Result index() {
		return ok(views.html.index.render(NewsEntity.findAll(), AscentEntity.findRecent(10)));
	}
	
	public static Result login() {
		return ok();
	}

	public static Result javascriptRoutes() {
		return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.MountainsController.showDistances())).as("text/javascript");
	}
	
	@RequiresAuthentication
	public static Result secure() {
		return ok();
	}
}