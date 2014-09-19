package models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@DiscriminatorColumn(name="image_type", discriminatorType=DiscriminatorType.STRING)
public abstract class ImageEntity extends BaseEntity {

	private static final long serialVersionUID = -5895581088150071181L;
	
	@Id
	public Long id;
	
	@Lob
	public byte[] image;
	
	@Lob
	public byte[] thumbnail;
	
	@Lob
	public String caption;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
