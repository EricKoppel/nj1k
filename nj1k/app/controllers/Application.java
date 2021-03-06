package controllers;

import static play.data.Form.form;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import models.AscentEntity;
import models.Contact;
import models.MountainEntity;
import models.NewsEntity;
import models.RegisteringUser;
import models.UserEntity;
import play.Play;
import play.Routes;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import scala.concurrent.duration.Duration;
import utils.MailUtil;
import actions.ETagAction;

public class Application extends Controller {
	private static final Form<Contact> contactForm = form(Contact.class);
	private static final Form<RegisteringUser> registrationForm = form(RegisteringUser.class);

	public static final ThreadLocal<DateFormat> cacheDateFormat = new ThreadLocal<DateFormat>() {
		public DateFormat initialValue() {
			return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
		}
	};

	public static Result index() {
		List<AscentEntity> result = AscentEntity.findAscentsGroupByDateAndClimber(0, 10);

		Map<Date, Map<UserEntity, List<AscentEntity>>> ascents = result.stream()
				.collect(Collectors.groupingBy(a -> a.ascent_date, () -> new TreeMap<Date, Map<UserEntity, List<AscentEntity>>>(Comparator.reverseOrder()),
						Collectors.groupingBy(a -> a.climber, Collectors.toList())));
		
		return ok(views.html.index.render(NewsEntity.findRecent(3), ascents, registrationForm, MountainEntity.findAll()));
	}

	public static Result contact() {
		return ok(views.html.contact.render(contactForm));
	}

	public static Result submitContact() {
		Form<Contact> filledForm = contactForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(views.html.contact.render(filledForm));
		}

		final String subject = filledForm.get().getSubject();
		final String message = Messages.get("mail.message", filledForm.get().getEmail(), filledForm.get().getMessage());

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
		case 11:
			cal.add(Calendar.YEAR, 1);
		case 0:
		case 1:
			status = ok(Play.application().resourceAsStream("public/images/winter.jpg"));
			cal.set(Calendar.MONTH, 2);
			break;
		case 2:
		case 3:
		case 4:
			status = ok(Play.application().resourceAsStream("public/images/spring.jpg"));
			cal.set(Calendar.MONTH, 5);
			break;
		case 5:
		case 6:
		case 7:
			status = ok(Play.application().resourceAsStream("public/images/summer.jpg"));
			cal.set(Calendar.MONTH, 8);
			break;
		case 8:
		case 9:
		case 10:
			status = ok(Play.application().resourceAsStream("public/images/fall.jpg"));
			cal.set(Calendar.MONTH, 11);
			break;
		default:
			status = ok(Play.application().resourceAsStream("public/images/default.png"));
			break;
		}

		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		response().setContentType("image/jpeg");
		response().setHeader(CACHE_CONTROL, "public");
		response().setHeader(EXPIRES, cacheDateFormat.get().format(cal.getTime()));

		return status;
	}

	@With(ETagAction.class)
	public static Result javascriptRoutes() {
		return ok(
				Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.AscentController.remove(), controllers.routes.javascript.AscentController.ascents(),
						controllers.routes.javascript.MountainsController.findNearest(), controllers.routes.javascript.AscentController.submit(),
						controllers.routes.javascript.AscentDetailController.updateCaption(), controllers.routes.javascript.AscentDetailController.remove(),
						controllers.routes.javascript.AscentController.removeByUserAndDate(),
						controllers.routes.javascript.AscentController.ascent(), controllers.routes.javascript.NewsController.delete(),
						controllers.routes.javascript.ExternalNewsController.getNewsFromNYNJTC(), controllers.routes.javascript.ExternalNewsController.getNewsArticleFromNYNJTC(),
						controllers.routes.javascript.UsersController.user(), controllers.routes.javascript.UsersController.userImage(), controllers.routes.javascript.UsersController.userThumbnail(),
						controllers.routes.javascript.MountainsController.showDistances(), controllers.routes.javascript.MountainsController.getImages(),
						controllers.routes.javascript.MountainsController.getThumbnail(),
						controllers.routes.javascript.UsersController.userAscentsByDate(),
						controllers.routes.javascript.UsersController.userAscents(), controllers.routes.javascript.MountainsController.mountain())).as("text/javascript");
	}
}