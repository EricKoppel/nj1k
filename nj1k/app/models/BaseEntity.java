package models;

import java.sql.Timestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import play.db.ebean.Model;

@MappedSuperclass
public class BaseEntity extends Model {

	private static final long serialVersionUID = 4194059364487884749L;
	
	@Version
	public Timestamp lastUpdate;
}
