import java.sql.Timestamp;

import models.MountainEntity;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.data.format.Formatters;
import play.filters.gzip.GzipFilter;
import utils.CustomDateConverter;
import utils.CustomMountainConverter;

public class Global extends GlobalSettings {

	public <T extends EssentialFilter> Class<T>[] filters() {
		return new Class[] { GzipFilter.class };
	}

	@Override
	public void onStart(Application app) {
		super.onStart(app);
		Formatters.register(Timestamp.class, new CustomDateConverter());
		Formatters.register(MountainEntity.class, new CustomMountainConverter());
	}
}
