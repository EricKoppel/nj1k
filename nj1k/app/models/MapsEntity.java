package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

@Entity
public class MapsEntity extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public Long id;
	public String name;
	public String publisher;
	
	@ManyToMany
	public List<MountainEntity> mountains;
}
