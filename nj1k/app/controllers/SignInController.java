package controllers;

import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;

import models.UserEntity;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.play.bind.ShiroBinderAction;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.PasswordUtil;

@With(ShiroBinderAction.class)
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
		
		UsernamePasswordToken token = new UsernamePasswordToken(email, password);
		
		try {
			currentUser.login(token);
			upgradePassword(currentUser, password);
			logger.debug("User fully authenticated. Redirecting ..................................................");
		}
		catch(AuthenticationException e) {
			filledForm.reject(Messages.get("validation.login.invalid"));
			return badRequest(views.html.login.render(filledForm));
		}

		
		return redirect(routes.Application.index());
	}
	
	private static void upgradePassword(Subject subject, String password) {
		if (subject.isAuthenticated()) {
			String email = subject.getPrincipals().getPrimaryPrincipal().toString();
			
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
}
