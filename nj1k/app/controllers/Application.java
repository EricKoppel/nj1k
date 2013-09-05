package controllers;

import static play.data.Form.form;
import models.AscentEntity;
import models.Contact;
import models.NewsEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Routes;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MailUtil;

public class Application extends Controller {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	private static final Form<Contact> contactForm = form(Contact.class);

	public static Result index() {
		return ok(views.html.index.render(NewsEntity.findAll(), AscentEntity.findRecent(10)));
	}

	public static Result contact() {
		return ok(views.html.contact.render(contactForm));
	}

	public static Result submitContact() {
		Form<Contact> filledForm = contactForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.contact.render(filledForm));
		}

		MailUtil.sendMail("erk7@njit.edu", filledForm.get().getSubject(), Messages.get("mail.message", filledForm.get().getEmail(), filledForm.get().getMessage()));
		return index();
	}

	public static Result about() {
		return TODO;
	}

	public static Result links() {
		return TODO;
	}

	public static Result javascriptRoutes() {
		return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.AscentController.remove(), 
				controllers.routes.javascript.AscentDetailController.remove(),
				controllers.routes.javascript.NewsController.delete(),
				controllers.routes.javascript.ExternalNewsController.getNewsFromNYNJTC(),
				controllers.routes.javascript.UsersController.showUser(),
				controllers.routes.javascript.MountainsController.showDistances(), 
				controllers.routes.javascript.MountainsController.showMountain())).as("text/javascript");
	}
}