package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.annotation.CreatedTimestamp;

@Entity
public class NewsEntity extends BaseEntity {

	private static final long serialVersionUID = -3217670302314792192L;
	public static Finder<Long, NewsEntity> find = new Finder<Long, NewsEntity>(Long.class, NewsEntity.class);
	
	@Id
	public Long id;
	
	@CreatedTimestamp
	public Timestamp news_date;
	
	@Required
	public String title;
	
	@Lob
	@Required
	public String entry;

	@OneToMany(mappedBy="news")
	public List<NewsImageEntity> pictures;
	
	public static List<NewsEntity> findAll() {
		return find.orderBy().desc("news_date").findList();
	}
	
	public static List<NewsEntity> findRecent(int i) {
		return find.setMaxRows(i).orderBy().desc("news_date").findList();
	}
	
	public static Integer findNumberOfPages(Integer pageSize) {
		return find.findPagingList(pageSize).getTotalPageCount();
	}
	
	public static NewsEntity find(Long id) {
		return find.byId(id);
	}
	
	public static NewsEntity showArticle(Long id) {
		return find.select("*").where().eq("id", id).findUnique();
	}
}
