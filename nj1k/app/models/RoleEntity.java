package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.db.ebean.Model;

@Entity
public class RoleEntity extends Model {

	public static final String USER = "user";
	public static final String ADMIN = "admin";
	
	private static final Logger log = LoggerFactory.getLogger(RoleEntity.class);
	
	public static Finder<Long, RoleEntity> find = new Finder<Long, RoleEntity>(Long.class, RoleEntity.class);
	
	private static final long serialVersionUID = 1L;
	
	@Id
	public Long id;
	
	@ManyToMany(mappedBy="roles")
	public List<UserEntity> user;
	
	@Column(unique=true)
	public String roleName;
	
	public RoleEntity(String roleName) {
		this.roleName = roleName;
	}
	
	public static RoleEntity findRoleByName(String roleName) {
		return find.where().eq("roleName", roleName).findUnique();
	}

	
	@Override
	public String toString() {
		return "RoleEntity [roleName=" + roleName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		log.debug("Are roles equal?");
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntity other = (RoleEntity) obj;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
}
