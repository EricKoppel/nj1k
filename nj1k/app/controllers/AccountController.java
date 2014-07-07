package controllers;

import static play.data.Form.form;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.UserEntity;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import utils.ImageResizeTask;
import utils.PasswordUtil;
import utils.SecurityUtil;

import com.google.common.net.MediaType;

public class AccountController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	private static final Form<UserEntity> userForm = form(UserEntity.class);
	
	private static final CompletionService<byte[]> taskCompletionService = new ExecutorCompletionService<byte[]>(Executors.newCachedThreadPool());
	
	public static Result editAccount() {
		if (SecurityUtil.isLoggedIn()) {
			return ok(views.html.account.render(userForm.fill(SecurityUtil.getCurrentUser())));
		}

		return forbidden();
	}

	public static Result submitEdit() throws Exception {
		if (!SecurityUtil.isLoggedIn()) {
			return forbidden();
		}
		
		Form<UserEntity> accountForm = userForm.bindFromRequest();
		if (accountForm.hasErrors()) {
			return badRequest(views.html.account.render(accountForm));
		}
		
		Long id = SecurityUtil.getCurrentUser().id;
		UserEntity user = UserEntity.find(id);
		UserEntity account = accountForm.get();
		
		if (!account.password.isEmpty()) {
			if (account.password.equals(accountForm.data().get("confirmPassword"))) {
				setNewPassword(user, account.password.toCharArray());
			} else {
				accountForm.reject(Messages.get("validation.passwords.equal"));
				return badRequest(views.html.account.render(accountForm.fill(account)));
			}
		}
		
		FilePart pic = request().body().asMultipartFormData().getFile("pic");
		if (pic != null && MediaType.parse(pic.getContentType()).is(MediaType.ANY_IMAGE_TYPE)) {
			
			DynamicForm requestData = Form.form().bindFromRequest();
		    int x = (int) Math.round(Double.parseDouble(requestData.get("x")));
		    int y = (int) Math.round(Double.parseDouble(requestData.get("y")));
		    int x2 = (int) Math.round(Double.parseDouble(requestData.get("x2")));
		    int y2 = (int) Math.round(Double.parseDouble(requestData.get("y2")));
		    int w = (int) Math.round(Double.parseDouble(requestData.get("w")));
		    int h = (int) Math.round(Double.parseDouble(requestData.get("h")));
		    
			Future<byte[]> task = taskCompletionService.submit(new ImageResizeTask(pic, x, y, x2, y2, w, h, true));
			user.pic = task.get();
		}
		
		if (!account.email.isEmpty()) {
			user.email = account.email;
			String uuid = session().get("UUID");
			Cache.set(uuid, user.email);
		}
		
		if (!account.name.isEmpty()) {
			user.name = account.name;
		}
		
		user.aboutme = account.aboutme;
		user.update();
		
		flash("success", Messages.get("account.updated"));
		return ok(views.html.account.render(userForm.fill(account)));
	}

	public static Result deleteProfilePic() {
		if (SecurityUtil.isLoggedIn()) {
			UserEntity user = SecurityUtil.getCurrentUser();
			user.pic = null;
			user.update();
			return ok();
		}
		
		return forbidden();
	}
	
	private static void setNewPassword(UserEntity user, char[] password) {
		ByteSource salt = PasswordUtil.generateSalt();
		SimpleHash hashedPassword = PasswordUtil.generateHash(password, salt);
		user.password = hashedPassword.toBase64();
		user.salt = salt.toBase64();
		user.update();
	}
}
