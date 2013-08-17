package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ASCENT")
public class AscentDetailEntity extends ImageEntity {

	private static final long serialVersionUID = 3725029336413869985L;
	public static Finder<Long, AscentDetailEntity> find = new Finder<Long, AscentDetailEntity>(Long.class, AscentDetailEntity.class);
	
	@ManyToOne
	public AscentEntity ascent;
	
	public String descriptor;
	
	@Lob
	public byte[] thumbnail;
	
	public static AscentDetailEntity find(Long id) {
		return find.select("*").where().eq("id", id).findUnique();
	}
}
