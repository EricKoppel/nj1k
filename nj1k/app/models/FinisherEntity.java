package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class FinisherEntity {

	private static String finishers = "SELECT u.id,u.name,finish_date FROM user_entity u JOIN " +
			"(SELECT a.climber_id,COUNT(DISTINCT(`mountain_id`)) AS ascents " +
			"FROM ascent_entity a GROUP BY a.climber_id HAVING ascents >= " +
			"(SELECT COUNT(*) FROM mountain_entity m where m.club_list=1)) f ON f.climber_id = u.id " +
			"JOIN " +
			"(SELECT climber_id,MAX(first_time) AS finish_date FROM (SELECT climber_id, mountain_id, MIN(ascent_date) AS first_time " +
			"FROM ascent_entity " +
			"GROUP BY climber_id,mountain_id) m " +
			"GROUP BY climber_id) n " +
			"ON u.id=n.climber_id";
	
	@OneToOne
	public UserEntity user;
	
	public Timestamp finish_date;
	
	public static List<FinisherEntity> findFinishers() {
		RawSql sql = RawSqlBuilder.parse(finishers).columnMapping("u.id", "user.id").columnMapping("u.name", "user.name").columnMapping("finish_date", "finish_date").create();
		Query<FinisherEntity> finishers = Ebean.find(FinisherEntity.class).setRawSql(sql);
		
		return finishers.findList();
	}
}
