package security.deadbolt;
import models.UserEntity;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;


public class NJ1KDeadboltHandler implements DeadboltHandler {

	public final static String USER_ID_KEY = "USER_ID";
	
	@Override
	public Result beforeAuthCheck(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicResourceHandler getDynamicResourceHandler(Context context) {
		return new NJ1KDynamicResourceHandler();
	}

	@Override
	public Subject getSubject(Context context) {
		
		if (context.session().containsKey(USER_ID_KEY)) {
			String userId = context.session().get(USER_ID_KEY);
			return UserEntity.findByEmail(userId);
		}
		
		return null;
		
	}

	@Override
	public Result onAccessFailure(Context context, String arg1) {
		return Controller.forbidden();
	}
}
