package models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ASCENT")
public class AscentDetailEntity extends ImageEntity {

	private static final long serialVersionUID = 3725029336413869985L;
	public static Finder<Long, AscentDetailEntity> find = new Finder<Long, AscentDetailEntity>(Long.class, AscentDetailEntity.class);
	
	@ManyToOne
	public AscentEntity ascent;
	
	public String descriptor;
	
	public static AscentDetailEntity find(Long id) {
		return find.select("*").where().eq("id", id).findUnique();
	}
	
	public static List<AscentDetailEntity> findAscentDetailsByMountain(Long mountainId) {
		return find.select("*").fetch("ascent").fetch("ascent.mountain", "id,name").fetch("ascent.climber", "id,name").where().eq("ascent.mountain.id", mountainId).findList();
	}
}
