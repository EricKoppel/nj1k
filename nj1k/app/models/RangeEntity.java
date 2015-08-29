package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RangeEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	
	public String name;
	
	@ManyToOne
	public RangeEntity parentRange;
	
	@OneToMany(mappedBy = "parentRange")
	public List<RangeEntity> subRanges;
	
	@OneToMany(mappedBy = "range")
	public List<MountainEntity> mountains;
	
	@Lob
	public String description;
}
