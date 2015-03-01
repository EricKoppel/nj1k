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
		return find.where().eq("id", id).findUnique();
	}
	
	public static AscentDetailEntity findImage(Long id) {
		return find.select("image,lastUpdate").where().eq("id", id).findUnique();
	}

	public static List<AscentDetailEntity> findAscentDetailsByMountain(Long mountainId, int page, int num) {
		return find.select("caption")
				.fetch("ascent.mountain", "id,name")
				.fetch("ascent.climber", "id,name")
				.where().eq("ascent.mountain.id", mountainId)
				.findPagingList(num).getPage(page).getList();
	}

	public static AscentDetailEntity findThumbnail(Long id) {
		return find.select("image,thumbnail,lastUpdate").where().eq("id", id).findUnique();
	}
}
