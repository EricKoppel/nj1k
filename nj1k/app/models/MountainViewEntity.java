package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class MountainViewEntity extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	
	@Lob
	public byte[] picture;
	public String description;
	
	@ManyToOne
	public MountainEntity mountain;
}
