package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.cache.Cached;

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
	
	private static String query = "SELECT a.climber_id,total,unique_successful,l.ascent_id " +
			"FROM (SELECT climber_id,COUNT(mountain_id) as total FROM ascent_entity GROUP BY climber_id) a " +
			"JOIN " +
			"(SELECT climber_id, COUNT(DISTINCT(mountain_id)) as unique_successful FROM ascent_entity WHERE successful=1 GROUP BY climber_id) s " +
			"ON a.climber_id=s.climber_id " +
			"JOIN (SELECT climber_id,MAX(`ascent_date`) most_recent FROM ascent_entity GROUP BY climber_id) r " +
			"ON a.climber_id=r.climber_id " +
			"JOIN (SELECT climber_id,ascent_date,MAX(id) as ascent_id FROM ascent_entity GROUP BY climber_id,ascent_date) l ON l.climber_id=r.climber_id AND l.ascent_date=r.most_recent " +
			"JOIN ascent_entity q ON l.ascent_id=q.id " +
			"JOIN user_entity u ON u.id=a.climber_id";

	public static UserEntityAggregate find(Long id) {
		RawSql sql = RawSqlBuilder.parse(query).columnMapping("a.climber_id", "user.id").columnMapping("total", "total").
				columnMapping("unique_successful", "unique_successful").columnMapping("l.ascent_id", "most_recent.id").create();
		
		Query<UserEntityAggregate> users = Ebean.find(UserEntityAggregate.class).setRawSql(sql);
		users.fetch("most_recent", "*", new FetchConfig().query());
		users.fetch("most_recent.mountain", "*");
		users.fetch("most_recent.climber", "*");
		users.where().eq("u.id", id);
		
		return users.findUnique();
	}
	
	@Cached(key = "aspirants", duration = 3600)
	public static List<UserEntityAggregate> findAll() {
		RawSql sql = RawSqlBuilder.parse(query).columnMapping("a.climber_id", "user.id").columnMapping("total", "total").
				columnMapping("unique_successful", "unique_successful").columnMapping("l.ascent_id", "most_recent.id").create();
		
		Query<UserEntityAggregate> users = Ebean.find(UserEntityAggregate.class).setRawSql(sql);
		users.fetch("most_recent", "*", new FetchConfig().query());
		users.fetch("most_recent.mountain", "*");
		users.fetch("most_recent.climber", "*");
		
		return users.findList();
	}
	
	public Float percentComplete() {
		return (float) unique_successful / MountainEntity.listSize();
	}	
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
