package controllers;

import models.AscentEntity;
import models.UserEntity;
import play.mvc.Controller;
import play.mvc.Result;

public class UsersController extends Controller {
	
	public static Result list() {
		return ok(views.html.users.render(UserEntity.findAll()));
	}
	
	public static Result showUser(Long id) {
		return ok(views.html.user.render(UserEntity.findCompleteUser(id), AscentEntity.findByUserId(id)));
	}
}
