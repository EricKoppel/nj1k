package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Permission;

@Entity
public class PermissionEntity extends Model implements Permission {

	private static final long serialVersionUID = 1L;
	
	@Id
	public Long id;
	public String value;
	@Override
	
	public String getValue() {
		return value;
	}
}
