package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import com.avaje.ebean.FetchConfig;
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
	private static String selectGroupByDateAndClimber = "SELECT a1.id,a1.ascent_date,a1.climber_id,a1.trip_report,m.id,m.name,u.id,u.name,u.pic FROM ascent_entity a1 JOIN user_entity u ON a1.climber_id=u.id JOIN mountain_entity m ON a1.mountain_id=m.id JOIN (SELECT ascent_date,climber_id FROM ascent_entity GROUP BY ascent_date, climber_id ORDER BY ascent_date DESC LIMIT %s,%s) a2 ON a1.ascent_date=a2.ascent_date AND a1.climber_id=a2.climber_id";
	private static String selectAscentsByClimberGroupByDateAndClimber = "SELECT a1.id,a1.ascent_date,a1.climber_id,a1.trip_report,m.id,m.name,m.picture,u.id,u.name,u.pic FROM ascent_entity a1 JOIN user_entity u ON a1.climber_id=u.id JOIN mountain_entity m ON a1.mountain_id=m.id JOIN (SELECT ascent_date,climber_id FROM ascent_entity WHERE climber_id=%s GROUP BY ascent_date, climber_id ORDER BY ascent_date DESC LIMIT %s,%s) a2 ON a1.ascent_date=a2.ascent_date AND a1.climber_id=a2.climber_id";

	@Id
	public Long id;
	
	@Past
	@Temporal(TemporalType.DATE)
	public Timestamp ascent_date;
	
	@Required
	public Boolean successful;
	@Lob
	@Column(name = "trip_report")
	public String trip_report;
	
	@OneToMany(mappedBy="ascent", cascade={CascadeType.ALL}, orphanRemoval = true)
	public List<AscentDetailEntity> ascentDetails;
		
	@ManyToOne
	@PrimaryKeyJoinColumn(name="climber_id", referencedColumnName="id")
	public UserEntity climber;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="mountain_id", referencedColumnName="id")
	public MountainEntity mountain;
	
	public static AscentEntity find(Long id) {
		return find.byId(id);
	}
	
	public static List<AscentEntity> findAll() {
		return find.fetch("mountain").fetch("user").findList();
	}
	
	public static List<AscentEntity> findAscents(int page, int num) {
		return find.orderBy().desc("ascent_date").orderBy().desc("id").fetch("climber", "id,name").fetch("mountain").where().eq("club_list", true).findPagingList(num).getPage(page).getList();
	}
	
	public static List<AscentEntity> findAscentsGroupByDateAndClimber(int page, int num) {
		String query = String.format(selectGroupByDateAndClimber, page * num, num);
		RawSql sql = RawSqlBuilder.parse(query)
				.columnMapping("a1.ascent_date", "ascent_date")
				.columnMapping("a1.climber_id", "climber.id")
				.columnMapping("a1.trip_report", "trip_report")
				.columnMapping("m.id", "mountain.id")
				.columnMapping("m.name", "mountain.name")
				.columnMapping("u.id", "climber.id")
				.columnMapping("u.name", "climber.name")
				.columnMapping("u.pic", "climber.pic")
				.create();
		return Ebean.find(AscentEntity.class).setRawSql(sql).fetch("ascentDetails", "id,image", new FetchConfig().query()).findList();
	}
	
	public static List<AscentEntity> findAscentsByClimberGroupByDateAndClimber(long userId, int page, int num) {
		String query = String.format(selectAscentsByClimberGroupByDateAndClimber, userId, page * num, num);
		RawSql sql = RawSqlBuilder.parse(query)
				.columnMapping("a1.ascent_date", "ascent_date")
				.columnMapping("a1.climber_id", "climber.id")
				.columnMapping("a1.trip_report", "trip_report")
				.columnMapping("m.id", "mountain.id")
				.columnMapping("m.name", "mountain.name")
				.columnMapping("u.id", "climber.id")
				.columnMapping("u.name", "climber.name")
				.columnMapping("u.pic", "climber.pic")
				.columnMapping("m.picture", "mountain.picture")
				.create();
		return Ebean.find(AscentEntity.class).setRawSql(sql).fetch("ascentDetails", "id,image,caption", new FetchConfig().query()).findList();
	}
	
	public static AscentEntity findAscentByUserId(long ascentId, long userId) {
		return find.fetch("climber").where().eq("climber_id", userId).eq("id", ascentId).findUnique();
	}
	
	public static List<AscentEntity> findAscentByUserId(long userId, int page, int num) {
		return find.fetch("climber").where().eq("climber_id", userId).join("mountain").where().eq("club_list", true).orderBy().desc("ascent_date").findPagingList(num).getPage(page).getList();
	}
	
	public static int distinctClimbs(Long userId) {
		SqlQuery q = Ebean.createSqlQuery(distinctQuery);
		q.setParameter(1, userId);
		return q.findUnique().getInteger("ascents");
	}
	
	public static List<AscentEntity> findByMountainId(Long id) {
		return find.fetch("climber", "id,name").where().eq("mountain_id", id).join("mountain").where().eq("club_list", true).orderBy().desc("ascent_date").findList();
	}
	
	public static List<AscentEntity> findByMountainId(Long id, int page, int num) {
		return find.select("ascent_date,trip_report").fetch("climber", "id,name,pic").fetch("ascentDetails", "id,caption").where().eq("mountain_id", id).join("mountain").where().eq("club_list", true).orderBy().desc("ascent_date").findPagingList(num).getPage(page).getList();
	}
	
	public static AscentEntity findTripReport(Long id) {
		return find.select("*").fetch("climber").fetch("mountain").where().eq("id", id).findUnique();
	}

	
	@Override
	public String toString() {
		return "AscentEntity [id=" + id + ", climber=" + climber
				+ ", mountain=" + mountain + "]";
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
		AscentEntity other = (AscentEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static AscentEntity findMostRecent(Long userId) {
		RawSql sql = RawSqlBuilder.parse(mostRecent).columnMapping("MAX(`ascent_date`)", "ascent_date").create();
		Query<AscentEntity> q = Ebean.createQuery(AscentEntity.class);
		q.setParameter(1, userId);
		q.setRawSql(sql);

		return q.findUnique();	
	}

	public static void removeByUserAndDate(long userId, Date date) {
		List<AscentEntity> ascents = AscentEntity.find.where().eq("climber_id", userId).eq("ascent_date", date).findList();
		for (AscentEntity a : ascents) {
			a.delete();
		}
	}

	public static List<AscentEntity> findAscentsByUserIdAndDate(long id, Date date) {
		return find.where().eq("climber_id", id).where().eq("ascent_date", date).findList();
	}
}