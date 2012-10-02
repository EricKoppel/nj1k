package persistence;

import models.RoleEntity;
import models.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;

public class UserEntityPersistAdapter extends BeanPersistAdapter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preInsert(BeanPersistRequest<?> request) {
		
		logger.debug("PreInsert on UserEntity");
		
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
