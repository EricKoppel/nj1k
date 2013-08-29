package controllers;

import static play.data.Form.form;

import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;

import models.UserEntity;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import security.shiro.authentication.NJ1KCredentialsMatcher;
import security.shiro.realm.NJ1KAuthenticatingRealm;
import utils.PasswordUtil;
import utils.SecurityUtil;

public class SignInController extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
	final static Form<UserEntity> signInForm = form(UserEntity.class);

	public static Result showForm() {
		return ok(views.html.login.render(signInForm));
	}

	public static Result logout() {

		ctx().session().remove(SecurityUtil.USER_ID_KEY);
		return redirect(routes.Application.index());
	}

	public static Result submit() throws NoSuchAlgorithmException, CharacterCodingException {
		Form<UserEntity> filledForm = signInForm.bindFromRequest();

		if (!filledForm.hasErrors()) {
			String email = filledForm.get().email;
			String password = filledForm.get().password;

			try {
				login(email, password.toCharArray());
				upgradePassword(email, password);
				logger.debug("User fully authenticated. Redirecting ..................................................");
			} catch (AuthenticationException e) {
				filledForm.reject(Messages.get("validation.login.invalid"));
				return badRequest(views.html.login.render(filledForm));
			} catch (Exception e) {
				logger.error("Deadbolt exception", e);
			}

			return redirect(routes.Application.index());
		} else {
			logger.debug("Signin form contained errors: {}", filledForm.errors().toString());

			return badRequest(views.html.login.render(filledForm));
		}

	}

	private static void login(String username, char[] password) throws AuthenticationException {

		AuthorizingRealm realm = new NJ1KAuthenticatingRealm();
		realm.setCredentialsMatcher(new NJ1KCredentialsMatcher());

		AuthenticationToken token = new UsernamePasswordToken(username, password);
		AuthenticationInfo authenticationInfo = realm.getAuthenticationInfo(token);

		session().put(SecurityUtil.USER_ID_KEY, authenticationInfo.getPrincipals().getPrimaryPrincipal().toString());
	}

	private static void upgradePassword(String email, String password) {

		UserEntity user = UserEntity.findByEmail(email);

		if (user.salt == null) {
			ByteSource salt = PasswordUtil.generateSalt();
			SimpleHash hashedPassword = PasswordUtil.generateHash(password, salt);

			user.salt = salt.toBase64();
			user.password = hashedPassword.toBase64();
			user.update();

			logger.debug("User has old password hash. Upgrading with hash {} and salt {}", hashedPassword.toString(), salt.getBytes());
		}
	}
}
