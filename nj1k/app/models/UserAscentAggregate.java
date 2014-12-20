package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class UserAscentAggregate {

	private static String query = "SELECT u.id, climber_id, ascent_date FROM (SELECT climber_id,ascent_date FROM ascent_entity group by ascent_date,climber_id) a JOIN user_entity u ON a.climber_id = u.id";

	@EmbeddedId
	public UserAscentPK key;

	@OneToOne
	public UserEntity climber;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(referencedColumnName = "ascent_date", name = "ascent_date"), @JoinColumn(referencedColumnName = "climber_id", name = "climber_id") })
	public List<AscentEntity> ascents;
	
	public static List<UserAscentAggregate> findAll(int page, int num) {
		RawSql sql = RawSqlBuilder.parse(query).columnMapping("u.id", "climber.id").columnMapping("ascent_date", "key.ascent_date").columnMapping("climber_id", "key.climber_id").create();
		return Ebean.find(UserAscentAggregate.class).orderBy().desc("ascent_date").setRawSql(sql).findPagingList(num).getPage(page).getList();
	}
}
