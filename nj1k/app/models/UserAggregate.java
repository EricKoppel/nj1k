package models;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.Sql;

@MappedSuperclass
@Sql
public abstract class UserAggregate {

	@OneToOne
	public UserEntity user;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserAggregate) {
			return this.user.equals(((UserAggregate) obj).user);
		}
		return false;
	}
}
