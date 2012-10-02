package base;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import models.RoleEntity;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;
import play.test.Helpers;


public abstract class AbstractBaseTest {

	public static FakeApplication app;
	
	@BeforeClass
	public static void startApp() {
		app = fakeApplication(inMemoryDatabase());
		Helpers.start(app);
		
		running(app, new Runnable() {
			public void run() {
				
				RoleEntity role = new RoleEntity(RoleEntity.USER);
				
				role.save();
			}
		});
	}
	
	@AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }
}
