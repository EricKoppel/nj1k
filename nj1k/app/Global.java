import java.lang.reflect.Method;
import java.sql.Timestamp;

import models.MountainEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.mvc.Action;
import play.mvc.Http.Request;
import utils.CustomDateConverter;
import utils.CustomMountainConverter;

public class Global extends GlobalSettings {

	private static final Logger logger = LoggerFactory.getLogger(Global.class);

	@Override
	public void onStart(Application app) {
		super.onStart(app);

		registerDateConverter(app);
		registerMountainConverter(app);
	}


	private void registerDateConverter(final Application app) {

		logger.debug("Registering date converter . . .");

		Formatters.register(Timestamp.class, new CustomDateConverter(app.configuration().getString("date.format")));
	}

//	@Override
//	public Action<?> onRequest(Request request, Method method) {
//
//		return new ShiroBinderAction();
//	}

	private void registerMountainConverter(final Application app) {

		logger.debug("Registering mountain converter . . .");

		Formatters.register(MountainEntity.class, new CustomMountainConverter());
	}
}
