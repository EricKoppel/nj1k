import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import models.AscentDetailEntity;
import models.MountainEntity;
import models.NewsImageEntity;
import models.UserEntity;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.Configuration;
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

//		 loadProfileImages(app);
//		 loadTripReportImages(app);
//		 loadMountainPics(app);
//		loadNewsImages(app);
	}

	private void loadNewsImages(Application app) {
		List<NewsImageEntity> details = NewsImageEntity.find.all();

		for (NewsImageEntity e : details) {
			if (e.image == null) {
				String filename = "/public/images/news_images/" + e.caption;

				try {
					File f = app.getFile(filename);
					if (f.exists()) {
						logger.info("Filename: {}", f);
						e.image = FileUtils.readFileToByteArray(app.getFile(filename));
						e.save();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void loadMountainPics(Application app) {
		List<MountainEntity> details = MountainEntity.find.all();

		for (MountainEntity e : details) {
			if (e.picture == null) {
				String filename = "/public/images/mountains/" + e.id + ".jpg";

				try {
					File f = app.getFile(filename);
					if (f.exists()) {
						logger.info("Filename: {}", f);
						e.picture = FileUtils.readFileToByteArray(app.getFile(filename));
						e.save();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
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
		List<UserEntity> details = UserEntity.find.all();

		for (UserEntity e : details) {
			if (e.id != null) {
				String filename = "/public/images/profile_images/" + e.id + ".jpg";
				logger.info("Filename: {}", filename);
				try {
					File f = app.getFile(filename);
					if (f.exists()) {
						e.pic = FileUtils.readFileToByteArray(f);
						e.save();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private void registerDateConverter(final Application app) {
		logger.debug("Registering date converter . . .");
		Formatters.register(Timestamp.class, new CustomDateConverter());
	}

	private void registerMountainConverter(final Application app) {
		logger.debug("Registering mountain converter . . .");
		Formatters.register(MountainEntity.class, new CustomMountainConverter());
	}
}
