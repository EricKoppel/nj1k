package auth;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.RoleEntity;
import models.UserEntity;

import org.apache.commons.codec.digest.DigestUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

import play.mvc.Http.Context;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.Helpers;

public class TestAuth {
	
	@Test
	public void createUser() {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			public void run() {
				
	            Result resultAuth = Helpers.callAction(controllers.routes.ref.Application.secure(), Helpers.fakeRequest());
	            
	            Assertions.assertThat(Helpers.status(resultAuth) != Status.OK);
	            
				String testPassword = "foobar";
				
				RoleEntity role = new RoleEntity(RoleEntity.USER);
				role.save();
				
				UserEntity user = new UserEntity();
				user.roles = new ArrayList<RoleEntity>();
				user.roles.add(role);
				user.email = "foo@bar.com";
				user.password = DigestUtils.md5Hex(testPassword);
				user.save();
				
				Map<String,String> form = new HashMap<String, String>();
	            form.put("email", "foo@bar.com");
	            form.put("password", testPassword);
	            
	            Result result = Helpers.callAction(controllers.routes.ref.SignInController.submit(), Helpers.fakeRequest().withFormUrlEncodedBody(form));
	            
	            Assertions.assertThat(Helpers.status(result) == Status.SEE_OTHER);
	            
	            Result resultAuth1 = Helpers.callAction(controllers.routes.ref.Application.secure(), Helpers.fakeRequest());
	            
	            Assertions.assertThat(Helpers.status(resultAuth1) == Status.OK);
			}
		});
	}
}
