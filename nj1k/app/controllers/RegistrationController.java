package controllers;

import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;

import models.RegisteringUser;
import models.UserEntity;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

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
			userToRegister.setPassword(DigestUtils.shaHex(userToRegister.getPassword()));
			
			logger.debug("Registering user {}", userToRegister);
			
			UserEntity userToSave = new UserEntity();
			
			doMapping(userToSave, userToRegister);
			
			userToSave.save();
			
			return ok();
		}
		else {
			logger.debug("Registration form contained errors: {}", filledForm.errors().toString());
			
			return badRequest(views.html.register.render(filledForm));
		}
	}
	
	private static void doMapping(UserEntity u, RegisteringUser r) {
		u.name = r.getName();
		u.password = r.getPassword();
		u.email = r.getEmail();
	}
}

