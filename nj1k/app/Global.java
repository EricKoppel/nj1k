import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import models.AscentDetailEntity;
import models.MountainEntity;
import models.NewsImageEntity;
import models.UserEntity;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.CustomDateConverter;
import utils.CustomMountainConverter;
import utils.ImageResizeTask;

public class Global extends GlobalSettings {

	private static final Logger logger = LoggerFactory.getLogger(Global.class);

	@Override
	public void onStart(Application app) {
		super.onStart(app);

		registerDateConverter(app);
		registerMountainConverter(app);

//		try {
//			loadTripReportImages(app);
//			loadMountainPics(app);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void loadMountainPics(Application app) throws Exception {
		List<MountainEntity> details = MountainEntity.find.all();
		ExecutorService service = Executors.newFixedThreadPool(4);

		for (MountainEntity e : details) {
			if (e.picture != null) {

				service.execute(() -> {
					try {
						File file = new File("mt" + e.id + ".jpg");
						FileOutputStream out = new FileOutputStream(file);
						out.write(e.picture);
						out.close();

						FilePart part = new FilePart("image", "image.jpg", "image/jpg", file);
						ImageResizeTask<NewsImageEntity> image = new ImageResizeTask<NewsImageEntity>(part, NewsImageEntity.class);

						e.thumbnail = image.call().thumbnail;
						e.update();

						file.delete();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
			}
		}
	}

	private void loadTripReportImages(Application app) throws Exception {
		List<AscentDetailEntity> details = AscentDetailEntity.find.all();
		ExecutorService service = Executors.newFixedThreadPool(4);

		for (AscentDetailEntity e : details) {

			if (e.image != null) {
				service.execute(() -> {
					try {
					File file = new File("tr" + e.id + ".jpg");
					FileOutputStream out = new FileOutputStream(file);
					out.write(e.image);
					out.close();

					FilePart part = new FilePart("image", "image.jpg", "image/jpg", file);
					ImageResizeTask<AscentDetailEntity> image = new ImageResizeTask<AscentDetailEntity>(part, AscentDetailEntity.class);

					e.thumbnail = image.call().thumbnail;
					e.update();

					file.delete();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
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
