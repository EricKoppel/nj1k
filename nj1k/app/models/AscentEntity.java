package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlQuery;

@Entity
public class AscentEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static Finder<Long, AscentEntity> find = new Finder<Long, AscentEntity>(Long.class, AscentEntity.class);
	
	private static String distinctQuery = "SELECT COUNT(DISTINCT(`mountain_id`)) AS ascents FROM ascent_entity a WHERE climber_id=?";
	private static String mostRecent = "SELECT MAX(`ascent_date`) FROM ascent_entity WHERE climber_id=?";
	
	@Id
	public Long id;
	
	@Past
	@Temporal(TemporalType.DATE)
	public Timestamp ascent_date;
	
	@Required
	public Boolean successful;
	@Lob
	public String trip_report;
	
	@OneToMany(mappedBy="ascent", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	public List<AscentDetailEntity> ascentDetails;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="climber_id", referencedColumnName="id")
	public UserEntity climber;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="mountain_id", referencedColumnName="id")
	public MountainEntity mountain;
	
	public static List<AscentEntity> findAll() {
		return find.fetch("mountain").fetch("user").findList();
	}
	
	public static AscentEntity find(Long id) {
		return find.byId(id);
	}
	
	public static AscentEntity findAscentByUserId(Long ascentId, Long userId) {
		return find.fetch("climber").where().eq("climber_id", userId).eq("id", ascentId).findUnique();
	}
	
	public static Page<AscentEntity> findByUserId(Long userId, int page, int num) {
		return find.fetch("climber").where().eq("climber_id", userId).join("mountain").where().eq("club_list", true).orderBy().desc("ascent_date").findPagingList(num).getPage(page);
	}

	public static int distinctClimbs(Long userId) {
		SqlQuery q = Ebean.createSqlQuery(distinctQuery);
		q.setParameter(1, userId);
		return q.findUnique().getInteger("ascents");
	}
	
	public static List<AscentEntity> findByMountainId(Long id) {
		return find.setMaxRows(5).fetch("climber").where().eq("mountain_id", id).join("mountain").where().eq("club_list", true).orderBy().desc("ascent_date").findList();
	}
	
	public static AscentEntity findTripReport(Long id) {
		return find.select("*").fetch("climber").fetch("mountain").where().eq("id", id).findUnique();
	}

	public static List<AscentEntity> findRecent(int i) {
		return find.orderBy().desc("ascent_date").orderBy().desc("id").fetch("climber").fetch("mountain").where().eq("club_list", true).setMaxRows(i).findList();
	}

	@Override
	public String toString() {
		return "AscentEntity [id=" + id + ", climber=" + climber
				+ ", mountain=" + mountain + "]";
	}

	public static AscentEntity findMostRecent(Long userId) {
		RawSql sql = RawSqlBuilder.parse(mostRecent).columnMapping("MAX(`ascent_date`)", "ascent_date").create();
		Query<AscentEntity> q = Ebean.createQuery(AscentEntity.class);
		q.setParameter(1, userId);
		q.setRawSql(sql);

		return q.findUnique();	
	}
}