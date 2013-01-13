package security.deadbolt;
import play.mvc.Http.Context;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;


public class NJ1KDynamicResourceHandler implements DynamicResourceHandler {

	@Override
	public boolean checkPermission(String permissionValue, DeadboltHandler handler, Context context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAllowed(String name, String meta, DeadboltHandler handler, Context context) {
		// TODO Auto-generated method stub
		return false;
	}

}
