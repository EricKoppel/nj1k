package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cached;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.annotation.EnumMapping;

@Entity
@CacheStrategy(readOnly=true, warmingQuery="order by name")
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
