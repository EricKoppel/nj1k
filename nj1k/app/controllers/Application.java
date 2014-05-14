package controllers;

import static play.data.Form.form;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import models.AscentEntity;
import models.Contact;
import models.NewsEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;
import play.Routes;
import play.cache.Cached;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import utils.MailUtil;

public class Application extends Controller {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	private static final Form<Contact> contactForm = form(Contact.class);

	static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
		}
	};
	
	public static Result index() {
		return ok(views.html.index.render(NewsEntity.findRecent(5), AscentEntity.findRecent(5)));
	}

	public static Result contact() {
		return ok(views.html.contact.render(contactForm));
	}

	public static Result submitContact() {
		final Form<Contact> filledForm = contactForm.bindFromRequest();
		final String subject = filledForm.get().getSubject();
		final String message = Messages.get("mail.message", filledForm.get().getEmail(), filledForm.get().getMessage());
		
		if (filledForm.hasErrors()) {
			return badRequest(views.html.contact.render(filledForm));
		}
		
		Akka.system().scheduler().scheduleOnce(Duration.create(0, TimeUnit.SECONDS), () -> {
			MailUtil.sendMail("erk7@njit.edu", subject, message);
		}, Akka.system().dispatcher());
		
		flash("success", Messages.get("email.sent"));
		return index();
	}

	public static Result about() {
		return ok(views.html.aboutus.render());
	}

	public static Result links() {
		return ok(views.html.links.render());
	}

	public static Result background() {
		Calendar cal = Calendar.getInstance();
		Status status = null;
		
		switch (cal.get(Calendar.MONTH)) {
		case 11: case 0: case 1:
			status = ok(Play.class.getClassLoader().getResourceAsStream("public/images/winter.png"));
			cal.set(Calendar.MONTH, 2);
			break;
		case 2: case 3: case 4:
			status = ok(Play.class.getClassLoader().getResourceAsStream("public/images/spring.png"));
			cal.set(Calendar.MONTH, 5);
			break;
		case 5: case 6: case 7:
			status = ok(Play.class.getClassLoader().getResourceAsStream("public/images/summer.png"));
			cal.set(Calendar.MONTH, 8);
			break;
		case 8: case 9: case 10:
			status = ok(Play.class.getClassLoader().getResourceAsStream("public/images/fall.png"));
			cal.set(Calendar.MONTH, 11);
			break;
		default: 
			status = ok(Play.class.getClassLoader().getResourceAsStream("public/images/default.png"));
			break;
		}
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		response().setHeader(CACHE_CONTROL, "public");
		response().setHeader(EXPIRES, dateFormat.get().format(cal.getTime()));
		
		return status;
	}
	
	@Cached(key = "javascriptRoutes")
	public static Result javascriptRoutes() {
		return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.AscentController.remove(), 
				controllers.routes.javascript.AscentDetailController.remove(),
				controllers.routes.javascript.NewsController.delete(),
				controllers.routes.javascript.ExternalNewsController.getNewsFromNYNJTC(),
				controllers.routes.javascript.ExternalNewsController.getNewsArticleFromNYNJTC(),
				controllers.routes.javascript.UsersController.showUser(),
				controllers.routes.javascript.MountainsController.showDistances(), 
				controllers.routes.javascript.MountainsController.showMountain())).as("text/javascript");
	}
}