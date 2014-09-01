package models;

import java.util.List;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cached;
import utils.FixedSizeTreeSet;
import utils.MountainDistanceBeanComparator;

import com.avaje.ebean.annotation.EnumMapping;

import dto.MountainDistanceBean;

@Entity
public class MountainEntity extends BaseEntity {

	private static final Logger logger = LoggerFactory.getLogger(MountainEntity.class);

	private static final long serialVersionUID = 1829718688996588377L;

	@EnumMapping(nameValuePairs = "OFFICIAL=official,UNOFFICIAL=unofficial")
	public enum NameType {
		OFFICIAL, UNOFFICIAL
	}

	@Id
	public Long id;
	
	public String name;
	public String county;
	public Float latitude;
	public Float longitude;
	public Integer elevation;
	public String owner;
	public String ownership;
	@Lob
	public byte[] picture;
	@Lob
	public byte[] thumbnail;
	@Lob
	public String description;
	public String mountainrange;
	public NameType name_type;
	public Boolean trueoneker;
	public Boolean club_list;

	@ManyToMany
	public List<MapsEntity> maps;

	@OneToMany(mappedBy = "mountain")
	public List<AscentEntity> ascents;

	public static Finder<Long, MountainEntity> find = new Finder<Long, MountainEntity>(Long.class, MountainEntity.class);

	@Cached(key = "mountains")
	public static List<MountainEntity> findAll() {
		return find.where().eq("club_list", true).orderBy().asc("name").findList();
	}

	public static MountainEntity find(Long id) {
		return find.byId(id);
	}

	public static MountainEntity findByName(String name) {
		return find.where().eq("name", name).findUnique();
	}
	
	public static int listSize() {
		return find.where().eq("club_list", true).findRowCount();
	}

	public static MountainEntity findWithDetails(Long id) {
		return find.select("*").where().eq("id", id).findUnique();
	}

	public static MountainEntity findNearestHigherNeighbor(Long id) {
		MountainEntity thisMountain = find.byId(id);
		List<MountainEntity> higherMountains = find.where().ne("id", thisMountain.id).eq("club_list", true).gt("elevation", thisMountain.elevation).findList();

		double nearest = Double.MAX_VALUE;
		MountainEntity nearestMountain = thisMountain;

		for (MountainEntity m2 : higherMountains) {
			double currNearest = calculateDistanceBetweenMountains(thisMountain, m2);

			if (currNearest < nearest) {
				nearest = currNearest;
				nearestMountain = m2;
			}
		}

		return nearestMountain;
	}

	public static SortedSet<MountainDistanceBean> findNearestNeighbors(Long id, Long howMany) {
		MountainEntity thisMountain = find.byId(id);
		List<MountainEntity> neighbors = find.where().ne("id", thisMountain.id).eq("club_list", true).findList();
		SortedSet<MountainDistanceBean> distances = new FixedSizeTreeSet<MountainDistanceBean>(howMany, new MountainDistanceBeanComparator());

		for (MountainEntity n : neighbors) {
			Double distance = calculateDistanceBetweenMountains(thisMountain, n);
			distances.add(new MountainDistanceBean(thisMountain, n, distance));
		}

		for (MountainDistanceBean entry : distances) {
			logger.debug(entry.toString());
		}

		return distances;
	}

	public static MountainDistanceBean findDistanceBetweenMountains(Long id1, Long id2) {
		MountainEntity mountain1 = find.byId(id1);
		MountainEntity mountain2 = find.byId(id2);

		return new MountainDistanceBean(mountain1, mountain2, calculateDistanceBetweenMountains(mountain1, mountain2));
	}

	public static double calculateDistanceBetweenMountains(MountainEntity m1, MountainEntity m2) {
		double lat1 = m1.latitude * Math.PI / 180;
		double long1 = m1.longitude * Math.PI / 180;

		double lat2 = m2.latitude * Math.PI / 180;
		double long2 = m2.longitude * Math.PI / 180;

		double x = (long2 - long1) * Math.cos((lat1 + lat2) / 2);
		double y = lat2 - lat1;
		double d = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		return d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MountainEntity other = (MountainEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MountainEntity [id=" + id + ", name=" + name + "]";
	}
}
