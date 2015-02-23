package controllers;

import static play.data.Form.form;
import models.UserEntity;

import org.apache.shiro.util.ByteSource;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MailUtil;
import utils.PasswordUtil;
import utils.SecurityUtil;

public class ResetPasswordController extends Controller {

	private static final byte[] RESET_SALT = {'p','a','s','s','w','o','r','d','R','e','s','e','t','1','2','3'};
	private static final Form<UserEntity> userForm = form(UserEntity.class);

	public static Result resetPasswordForm() {
		return ok(views.html.resetpassword.render(userForm));
	}

	public static Result sendConfirmResetPasswordEmail() {
		Form<UserEntity> resetForm = userForm.bindFromRequest();

		if (!resetForm.hasErrors()) {
			UserEntity user = UserEntity.findByEmail(resetForm.get().email);

			if (user != null) {
				String token = PasswordUtil.generateHash(user.password.toCharArray(), ByteSource.Util.bytes(RESET_SALT)).toHex();
				String link = routes.ResetPasswordController.resetPassword(user.id, token).absoluteURL(request());
				MailUtil.sendMail(user.email, Messages.get("mail.resetpassword.subject"), Messages.get("mail.resetpassword", link));
			}

			flash("success", Messages.get("password.sent"));
			return ok(views.html.login.render(resetForm));
		}

		return badRequest(views.html.resetpassword.render(resetForm));
	}

	public static Result resetPassword(long userId, String hash) {
		UserEntity user = UserEntity.find(userId);

		if (user == null || !hash.equals(PasswordUtil.generateHash(user.password.toCharArray(), ByteSource.Util.bytes(RESET_SALT)).toHex())) {
			return forbidden(views.html.errorResponse.render("Four Oh Three: Forbidden!"));
		} else {
			session().put(SecurityUtil.USER_ID_KEY, user.email);
			userForm.fill(user);
			return redirect(routes.AccountController.editAccount());
		}
	}
}
