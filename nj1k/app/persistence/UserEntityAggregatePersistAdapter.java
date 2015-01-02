package persistence;

import java.util.Set;

import models.UserEntityAggregate;

import com.avaje.ebean.event.BeanPersistAdapter;

public class UserEntityAggregatePersistAdapter extends BeanPersistAdapter {

	@Override
	public boolean isRegisterFor(Class<?> clazz) {
		return clazz.isAssignableFrom(UserEntityAggregate.class);
	}
	
	@Override
	public void postLoad(Object bean, Set<String> includedProperties) {
		if (bean instanceof UserEntityAggregate) {
			if (((UserEntityAggregate) bean).unique_successful == null) {
				((UserEntityAggregate) bean).unique_successful = 0;
			}
			
			if (((UserEntityAggregate) bean).total == null) {
				((UserEntityAggregate) bean).total = 0;
			}
		}
	}
}
