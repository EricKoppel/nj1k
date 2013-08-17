import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import models.AscentDetailEntity;
import models.MountainEntity;
import models.UserEntity;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import utils.CustomDateConverter;
import utils.CustomMountainConverter;

public class Global extends GlobalSettings {

	private static final Logger logger = LoggerFactory.getLogger(Global.class);

	@Override
	public void onStart(Application app) {
		super.onStart(app);

		registerDateConverter(app);
		registerMountainConverter(app);
		
//		loadProfileImages(app);
//		loadTripReportImages(app);
	}


	private void loadTripReportImages(Application app) {
		List<AscentDetailEntity> details = AscentDetailEntity.find.all();
		
		for (AscentDetailEntity e : details) {
			if (e.descriptor != null) {
				String filename = "/public/images/tr_images/" + e.descriptor;
				logger.info("Filename: {}", filename);
				try {
					e.image = FileUtils.readFileToByteArray(app.getFile("/public/images/tr_images/" + e.descriptor));
					e.save();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}


	private void loadProfileImages(Application app) {
		File folder = app.getFile("/public/images/profile_images");
		logger.info("loading images");
		for (File p : folder.listFiles()) {
			String id = p.getName().split("\\.")[0];
			logger.info("id = {}", id);
			UserEntity u = UserEntity.find(Long.parseLong(id));

			if (u.pic != null) {
				try {
					u.pic = FileUtils.readFileToByteArray(p);
				} catch (IOException e) {
					logger.error("Exception", e);
				}
				u.save();
			}
		}
	}


	private void registerDateConverter(final Application app) {

		logger.debug("Registering date converter . . .");

		Formatters.register(Timestamp.class, new CustomDateConverter(app.configuration().getString("date.format")));
	}

	private void registerMountainConverter(final Application app) {

		logger.debug("Registering mountain converter . . .");

		Formatters.register(MountainEntity.class, new CustomMountainConverter());
	}
}
