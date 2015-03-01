package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.cache.Cached;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.annotation.Sql;

@Entity
@CacheStrategy(useBeanCache = true)
@Sql
public class FinisherEntity extends UserAggregate {

	private static String finishers = "SELECT MAX(a.id) AS final_ascent_id,u.id,u.name,m.id,m.name,a.ascent_date FROM user_entity u JOIN " +
	"(SELECT a.climber_id,COUNT(DISTINCT(`mountain_id`)) AS ascents " +
	"FROM ascent_entity a WHERE a.successful='1' GROUP BY a.climber_id HAVING ascents >= " +
	"(SELECT COUNT(*) FROM mountain_entity m where m.club_list=1)) f ON f.climber_id = u.id " +
	"JOIN " +
	"(SELECT climber_id,MAX(first_time) AS finish_date FROM (SELECT climber_id, mountain_id, MIN(ascent_date) AS first_time FROM ascent_entity GROUP BY climber_id,mountain_id) m GROUP BY climber_id) n " +
	"ON u.id=n.climber_id " +
	"JOIN ascent_entity a ON a.climber_id=u.id AND a.ascent_date=finish_date " +
	"JOIN mountain_entity m ON a.mountain_id=m.id " + 
	"GROUP BY u.id";
	
	@OneToOne
	public AscentEntity final_ascent;
	
	@Cached(key = "finishers")
	public static List<FinisherEntity> findFinishers() {
		RawSql sql = RawSqlBuilder.parse(finishers)
				.columnMapping("u.id", "user.id")
				.columnMapping("u.name", "user.name")
				.columnMapping("m.id", "final_ascent.mountain.id")
				.columnMapping("m.name", "final_ascent.mountain.name")
				.columnMapping("a.ascent_date", "final_ascent.ascent_date")
				.columnMapping("MAX(a.id)", "final_ascent.id").create();
		Query<FinisherEntity> finishers = Ebean.find(FinisherEntity.class).setRawSql(sql);
		finishers.orderBy("ascent_date");
		
		return finishers.findList();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
