package controllers;

import static play.data.Form.form;
import models.UserEntity;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cache;
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
	
	public static Result editAccount() {
		if (SecurityUtil.isLoggedIn()) {
			return ok(views.html.editaccount.render(userForm.fill(SecurityUtil.getCurrentUser())));
		}

		return forbidden();
	}

	public static Result submitEdit() throws Exception {
		Form<UserEntity> accountForm = userForm.bindFromRequest();
		if (accountForm.hasErrors()) {
			return badRequest(views.html.editaccount.render(accountForm));
		}
		
		UserEntity account = accountForm.get();
		if (!(SecurityUtil.getCurrentUser().id == account.id)) {
			return forbidden();
		}
		
		UserEntity user = UserEntity.find(account.id);
		
		if (!account.password.isEmpty()) {
			if (account.password.equals(accountForm.data().get("confirmPassword"))) {
				setNewPassword(user, account.password);
			} else {
				accountForm.reject(Messages.get("validation.passwords.equal"));
				return badRequest(views.html.editaccount.render(accountForm.fill(account)));
			}
		}
		
		FilePart pic = request().body().asMultipartFormData().getFile("pic");
		if (pic != null && MediaType.parse(pic.getContentType()).is(MediaType.ANY_IMAGE_TYPE)) {
			user.pic = new ImageResizeTask(pic).call();
			logger.debug("{}", user.pic.length);
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
		
		return ok(views.html.editaccount.render(userForm.fill(account)));
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
	
	private static void setNewPassword(UserEntity user, String password) {
		ByteSource salt = PasswordUtil.generateSalt();
		SimpleHash hashedPassword = PasswordUtil.generateHash(password, salt);
		user.password = hashedPassword.toBase64();
		user.salt = salt.toBase64();
		user.update();
	}
}