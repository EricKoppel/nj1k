package controllers;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import models.UserEntity;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;

public class TestLogin {

	private static String testPassword = "foobarpassword";
	private static String testEmail = "foo@bar.com";

	private static FakeApplication app;

	@BeforeClass
	public static void startApp() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void stopApp() {
		stop(app);
	}

	@Before
	public void createUser() {
		Map<String, String> form = new HashMap<String, String>();
		form.put("email", testEmail);
		form.put("name", "Foo");
		form.put("password", testPassword);
		form.put("confirmPassword", testPassword);

		Result result = Helpers.callAction(controllers.routes.ref.RegistrationController.submit(), Helpers.fakeRequest().withFormUrlEncodedBody(form));
		
		while (result != null) {
			if (result.getWrappedResult() == null) {
				Assert.assertEquals(Status.OK, Helpers.status(result));
				break;
			}
			result = result.getWrappedResult();
		}
		Assert.assertEquals(Status.OK, Helpers.status(result));

		Assert.assertNotNull(UserEntity.findByEmail(testEmail));
		System.out.println(UserEntity.findByEmail(testEmail).salt);
		System.out.println(UserEntity.findByEmail(testEmail).password);
	}

	@Test
	public void testRegistrationAndLogin() {
		Map<String, String> form = new HashMap<String, String>();
		form.put("email", testEmail);
		form.put("password", testPassword);

		Result result = Helpers.callAction(controllers.routes.ref.SignInController.submit(), Helpers.fakeRequest().withFormUrlEncodedBody(form));
		Assert.assertEquals(Status.OK, Helpers.status(result));
	}
}
