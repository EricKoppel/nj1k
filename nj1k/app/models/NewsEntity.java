package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class NewsEntity extends Model {

	private static final long serialVersionUID = -3217670302314792192L;
	public static Finder<Long, NewsEntity> find = new Finder<Long, NewsEntity>(Long.class, NewsEntity.class);
	
	@Id
	public Long id;
	
	public Timestamp news_date = new Timestamp(new Date().getTime());
	
	@Required
	public String title;
	
	@Lob
	@Required
	public String entry;

	@OneToMany(mappedBy="news")
	public List<NewsImageEntity> pictures;
	
	public static List<NewsEntity> findAll() {
		return find.all();
	}
	
	public static Integer findNumberOfPages(Integer pageSize) {
		return find.findPagingList(pageSize).getTotalPageCount();
	}
	public static List<NewsEntity> findByPage(Integer page, Integer pageSize) {
		return find.select("id,news_date,title").orderBy().desc("news_date").findPagingList(pageSize).getPage(page).getList();
	}
	
	public static NewsEntity find(Long id) {
		return find.byId(id);
	}
	
	public static NewsEntity showArticle(Long id) {
		return find.select("*").where().eq("id", id).findUnique();
	}
}
