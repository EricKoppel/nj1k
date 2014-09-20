package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("NEWS")
public class NewsImageEntity extends ImageEntity {

	private static final long serialVersionUID = 2752201643155156094L;
	public static Finder<Long, NewsImageEntity> find = new Finder<Long, NewsImageEntity>(Long.class, NewsImageEntity.class);
	
	@ManyToOne
	public NewsEntity news;
}
