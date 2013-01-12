package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class FinisherEntity {

	private static String finishers = "SELECT MAX(a.id) AS final_ascent_id,u.id,u.name FROM user_entity u JOIN " +
	"(SELECT a.climber_id,COUNT(DISTINCT(`mountain_id`)) AS ascents " +
	"FROM ascent_entity a WHERE a.successful='1' GROUP BY a.climber_id HAVING ascents >= " +
	"(SELECT COUNT(*) FROM mountain_entity m where m.club_list=1)) f ON f.climber_id = u.id " +
	"JOIN " +
	"(SELECT climber_id,MAX(first_time) AS finish_date FROM (SELECT climber_id, mountain_id, MIN(ascent_date) AS first_time FROM ascent_entity GROUP BY climber_id,mountain_id) m GROUP BY climber_id) n " +
	"ON u.id=n.climber_id " +
	"JOIN ascent_entity a ON a.climber_id=u.id AND a.ascent_date=finish_date " +
	"GROUP BY u.id";
	
	@OneToOne
	public UserEntity user;
	
	@OneToOne
	public AscentEntity final_ascent;
	
	public static List<FinisherEntity> findFinishers() {
		RawSql sql = RawSqlBuilder.parse(finishers).columnMapping("u.id", "user.id").columnMapping("u.name", "user.name").columnMapping("MAX(a.id)", "final_ascent.id").create();
		Query<FinisherEntity> finishers = Ebean.find(FinisherEntity.class).setRawSql(sql);
		finishers.fetch("final_ascent", "*", new FetchConfig().query());
		finishers.fetch("final_ascent.mountain", "name");
		
		return finishers.findList();
	}
}
