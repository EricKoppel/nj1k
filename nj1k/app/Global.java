import java.lang.reflect.Method;
import java.sql.Timestamp;

import models.MountainEntity;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.play.bind.ShiroBinderAction;
import org.apache.shiro.util.Factory;
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

		registerShiroIni(app);
		registerDateConverter(app);
		registerMountainConverter(app);
	}

	private void registerShiroIni(Application app) {

		Ini ini = new Ini();
		ini.loadFromPath("conf/shiro.ini");

		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(ini);
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		
		SecurityUtils.setSecurityManager(securityManager);
	}

	private void registerDateConverter(final Application app) {

		logger.debug("Registering date converter . . .");

		Formatters.register(Timestamp.class, new CustomDateConverter(app.configuration().getString("date.format")));
	}

	@Override
	public Action<?> onRequest(Request request, Method method) {

		return new ShiroBinderAction();
	}

	private void registerMountainConverter(final Application app) {

		logger.debug("Registering mountain converter . . .");

		Formatters.register(MountainEntity.class, new CustomMountainConverter());
	}
}
