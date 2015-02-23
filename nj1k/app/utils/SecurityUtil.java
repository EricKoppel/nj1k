package utils;

import models.AscentEntity;
import models.RoleEntity;
import models.UserEntity;
import play.mvc.Controller;

public class SecurityUtil {

	public static final String USER_ID_KEY = "USER_ID_KEY";

	public static UserEntity getCurrentUser() {
		if (Controller.session().containsKey(USER_ID_KEY)) {
			return UserEntity.findByEmail(Controller.session().get(USER_ID_KEY));
		}
		
		return null;
	}

	public static boolean hasRole(String role) {
		if (!isLoggedIn()) {
			return false;
		}

		for (RoleEntity r : getCurrentUser().roles) {
			if (role.equals(r.roleName)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isLoggedIn() {
		if (Controller.session().containsKey(USER_ID_KEY)) {
			return UserEntity.userExists(Controller.session().get(USER_ID_KEY));
		}
		return false;
	}

	public static boolean isUser(Long userId) {
		return (isLoggedIn() && getCurrentUser().id == userId);
	}

	public static boolean ownsAscent(long ascentId) {
		if (isLoggedIn()) {
			return AscentEntity.findAscentByUserId(ascentId, getCurrentUser().id) != null;
		}
		return false;
	}
}
