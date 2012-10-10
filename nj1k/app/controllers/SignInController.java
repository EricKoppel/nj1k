package controllers;

import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;

import models.UserEntity;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

public class SignInController extends Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
	final static Form<UserEntity> signInForm = form(UserEntity.class);
	
    public static Result showForm() {
        return ok(views.html.login.render(signInForm));
    }
    
    public static Result logout() {
    	SecurityUtils.getSubject().logout();
    	
    	return redirect(routes.Application.index());
    }
    
	public static Result submit() throws NoSuchAlgorithmException, CharacterCodingException {
		Form<UserEntity> filledForm = signInForm.bindFromRequest();

		String email = filledForm.get().email;
		String password = filledForm.get().password;
		Subject currentUser = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(email, password, true);
		
		try {
			currentUser.login(token);
			logger.debug("User fully authenticated. Redirecting ..................................................");
		}
		catch(AuthenticationException e) {
			filledForm.reject(Messages.get("validation.login.invalid"));
			return badRequest(views.html.login.render(filledForm));
		}

		
		return redirect(routes.Application.index());
	}
}
