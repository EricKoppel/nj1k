package models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class UserAscentPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	public Timestamp ascent_date;
	public Long climber_id;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ascent_date == null) ? 0 : ascent_date.hashCode());
		result = prime * result + ((climber_id == null) ? 0 : climber_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAscentPK other = (UserAscentPK) obj;
		if (ascent_date == null) {
			if (other.ascent_date != null)
				return false;
		} else if (!ascent_date.equals(other.ascent_date))
			return false;
		if (climber_id == null) {
			if (other.climber_id != null)
				return false;
		} else if (!climber_id.equals(other.climber_id))
			return false;
		return true;
	}
	
	
}
