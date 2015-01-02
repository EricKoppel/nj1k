package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class UserEntityAggregate extends UserAggregate {

	public Integer total;
	public Integer unique_successful;

	@OneToOne
	public AscentEntity most_recent;
	
	private static String query = "SELECT u.id,total,unique_successful,l.ascent_id " +
			"FROM user_entity u " +
			"LEFT JOIN (SELECT climber_id, COUNT(mountain_id) as total FROM ascent_entity GROUP BY climber_id) a " +
			"ON u.id=a.climber_id " +
			"LEFT JOIN (SELECT climber_id, COUNT(DISTINCT(mountain_id)) as unique_successful FROM ascent_entity WHERE successful=true GROUP BY climber_id) s " +
			"ON a.climber_id=s.climber_id " +
			"LEFT JOIN (SELECT climber_id,MAX(`ascent_date`) most_recent FROM ascent_entity GROUP BY climber_id) r " +
			"ON a.climber_id=r.climber_id " +
			"LEFT JOIN (SELECT climber_id,ascent_date,MAX(id) as ascent_id FROM ascent_entity GROUP BY climber_id,ascent_date) l ON l.climber_id=r.climber_id AND l.ascent_date=r.most_recent " +
			"LEFT JOIN ascent_entity q ON l.ascent_id=q.id";

	private static RawSql sql;
	
	static {
		sql = RawSqlBuilder.parse(query).columnMapping("u.id", "user.id").columnMapping("total", "total").
				columnMapping("unique_successful", "unique_successful").columnMapping("l.ascent_id", "most_recent.id").create(); 
	}
	public static UserEntityAggregate find(Long id) {
		Query<UserEntityAggregate> users = Ebean.find(UserEntityAggregate.class).setRawSql(sql);
		users.where().eq("u.id", id);
		
		return users.findUnique();
	}
	
	public static List<UserEntityAggregate> findAll() {
		Query<UserEntityAggregate> users = Ebean.find(UserEntityAggregate.class).setRawSql(sql);
		users.fetch("most_recent", "*", new FetchConfig().query());
		users.fetch("most_recent.mountain", "id,name");
		users.fetch("most_recent.climber", "id,name");
		users.orderBy("name");
		return users.findList();
	}
	
	public Float percentComplete() {
		if (unique_successful == null) {
			return 0f;
		}
		
		return (float) unique_successful / MountainEntity.listSize();
	}	
	
	public Integer peaksRemaining() {
		return MountainEntity.listSize() - unique_successful;
	}
	
	@PostLoad
	public void postLoad() {
		if (unique_successful == null) {
			unique_successful = 0;
		}
		
		if (total == null) {
			total = 0;
		}
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
