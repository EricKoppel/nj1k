package persistence;

import models.RoleEntity;
import models.UserEntity;

import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;

public class UserEntityPersistAdapter extends BeanPersistAdapter {

	@Override
	public boolean preInsert(BeanPersistRequest<?> request) {

		UserEntity user = (UserEntity) request.getBean();
		RoleEntity role = RoleEntity.findRoleByName(RoleEntity.USER);
		user.roles.add(role);
		return super.preInsert(request);
	}

	@Override
	public boolean isRegisterFor(Class<?> clazz) {
		return clazz.equals(UserEntity.class);
	}
}
