package controllers;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.HashMap;
import java.util.Map;

import models.RoleEntity;
import models.UserEntity;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;

public class TestLogin {

	private static String testPassword = "foobarpassword";

	public static FakeApplication app;

	public void createUser() {
		RoleEntity role = new RoleEntity(RoleEntity.USER);

		role.save();

		UserEntity userEntity = new UserEntity();

		userEntity.email = "foo@bar.com";
		userEntity.password = DigestUtils.md5Hex(testPassword);

		userEntity.save();
	}

	@Test
	public void testLogin() {
		running(app, new Runnable() {
			public void run() {

				createUser();

				Map<String, String> form = new HashMap<String, String>();
				form.put("email", "foo@bar.com");
				form.put("password", testPassword);

				Result result = Helpers.callAction(controllers.routes.ref.SignInController.submit(),
				Helpers.fakeRequest().withFormUrlEncodedBody(form));
				Assert.assertEquals(Status.OK, Helpers.status(result));
			}
		});
	}
}
