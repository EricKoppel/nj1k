package controllers;

import models.AscentEntity;
import models.FinisherEntity;
import models.UserEntity;
import models.UserEntityAggregate;
import play.Play;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.PasswordUtil;
import static play.data.Form.form;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

public class UsersController extends Controller {
	
	final static Form<UserEntity> userForm = form(UserEntity.class);
	
	public static Result list() {
		return ok(views.html.users.render(FinisherEntity.findFinishers(), UserEntityAggregate.findAll()));
	}
	
	public static Result showUser(Long id) {
		return ok(views.html.user.render(UserEntity.findCompleteUser(id), AscentEntity.findByUserId(id)));
	}
	
	public static Result resetPasswordForm() {
		return ok(views.html.resetpassword.render(userForm));
	}
	
	public static Result resetPassword() {
		
//		Form<UserEntity> resetForm = userForm.bindFromRequest();
//		
//		if (!resetForm.hasErrors()) {
//			
//			UserEntity user = UserEntity.findByEmail(resetForm.get().email);
//			
//			resetPassword(user);
//			
//			return ok(views.html.login.render(resetForm));
//		}
//		
//		return badRequest(views.html.resetpassword.render(resetForm));
		
		return TODO;
	}

//	private static void resetPassword(UserEntity user) {
//		
//		String newPassword = String.valueOf(PasswordUtil.generateRandomPassword());
//		ByteSource salt = PasswordUtil.generateSalt();
//		SimpleHash hashedPassword = PasswordUtil.generateHash(newPassword, salt);
//		
//		user.password = hashedPassword.toBase64();
//		user.salt = salt.toBase64();
//		user.update();
//		
//		sendResetPassword(user.email, newPassword);
//	}
//
//	private static void sendResetPassword(String email, String password) {
//		MailerAPI mailer = Play.application().plugin(MailerPlugin.class).email();
//		
//		mailer.addFrom("abc@cfg.com");
//		mailer.addRecipient(email);
//		mailer.send(Messages.get("mail.resetpassword", password));
//	}
}
