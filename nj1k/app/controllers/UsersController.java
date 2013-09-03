package controllers;

import static play.data.Form.form;

import java.util.List;

import models.AscentEntity;
import models.FinisherEntity;
import models.UserEntity;
import models.UserEntityAggregate;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.MediaType;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MailUtil;
import utils.PasswordUtil;

public class UsersController extends Controller {

	private static final Form<UserEntity> userForm = form(UserEntity.class);
	
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	public static Result list() {
		List<FinisherEntity> finishers = FinisherEntity.findFinishers();
		List<UserEntityAggregate> aspirants = UserEntityAggregate.findAll();
		aspirants.removeAll(finishers);

		return ok(views.html.users.render(finishers, aspirants));
	}

	public static Result showUser(Long id) {
		return ok(views.html.user.render(UserEntityAggregate.find(id), AscentEntity.findByUserId(id)));
	}
	
	public static Result resetPasswordForm() {
		return ok(views.html.resetpassword.render(userForm));
	}

	public static Result getProfileImage(Long id) {
		byte[] pic = UserEntity.find(id).pic;

		if (pic == null) {
			return notFound();
		}
		return ok(UserEntity.find(id).pic).as(MediaType.ANY_IMAGE_TYPE.type());
	}

	public static Result resetPassword() {
		Form<UserEntity> resetForm = userForm.bindFromRequest();

		if (!resetForm.hasErrors()) {
			UserEntity user = UserEntity.findByEmail(resetForm.get().email);
			
			if (user != null) {
				resetPassword(user);
			}
			flash("success", Messages.get("password.sent"));
			return ok(views.html.login.render(resetForm));
		}

		return badRequest(views.html.resetpassword.render(resetForm));
	}

	private static void resetPassword(UserEntity user) {
		char[] newPassword = PasswordUtil.generateRandomPassword();
		ByteSource salt = PasswordUtil.generateSalt();
		SimpleHash hashedPassword = PasswordUtil.generateHash(newPassword, salt);

		user.password = hashedPassword.toBase64();
		user.salt = salt.toBase64();
		user.update();

		sendResetPassword(user.email, newPassword);
	}

	private static void sendResetPassword(String email, char[] password) {
		MailUtil.sendMail(email, Messages.get("mail.resetpassword.subject"), Messages.get("mail.resetpassword", String.valueOf(password)));
	}
}
