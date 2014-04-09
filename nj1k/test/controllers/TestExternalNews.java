package controllers;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import org.junit.Assert;
import org.junit.Test;

import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.Helpers;

public class TestExternalNews {

	@Test
	public void testRegistrationAndLogin() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Result result = Helpers.callAction(controllers.routes.ref.ExternalNewsController.getNewsFromNYNJTC(), Helpers.fakeRequest());
				Assert.assertEquals(Status.OK, Helpers.status(result));
			}
		});
	}
}
