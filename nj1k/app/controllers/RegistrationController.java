package controllers;

import static play.data.Form.form;

import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;

import models.RegisteringUser;
import models.UserEntity;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.PasswordUtil;

public class RegistrationController extends Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	final static Form<RegisteringUser> registrationForm = form(RegisteringUser.class);
	
    public static Result showForm() {
        return ok(views.html.register.render(registrationForm));
    }
    
	public static Result submit() throws NoSuchAlgorithmException, CharacterCodingException {
		Form<RegisteringUser> filledForm = registrationForm.bindFromRequest();

		if (!filledForm.hasErrors()) {
			RegisteringUser userToRegister = filledForm.get();
			
			UserEntity userToSave = new UserEntity();
			ByteSource salt = PasswordUtil.generateSalt();
			SimpleHash hashedPassword = PasswordUtil.generateHash(userToRegister.getPassword(), salt);
	
			logger.debug("Registering user {}", userToRegister);
			doMapping(userToSave, userToRegister);
			userToSave.password = hashedPassword.toBase64();
			userToSave.salt = salt.toBase64();
			userToSave.save();
			
			return redirect(routes.SignInController.showForm());
		}
		else {
			logger.debug("Registration form contained errors: {}", filledForm.errors().toString());
			
			return badRequest(views.html.register.render(filledForm));
		}
	}
	
	private static void doMapping(UserEntity u, RegisteringUser r) {
		u.name = r.getName();
		u.email = r.getEmail();
	}
}

