package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

@Entity
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static Finder<Long, UserEntity> find = new Finder<Long, UserEntity>(Long.class, UserEntity.class);

	@Id
	public Long id;

	@Column(unique = true)
	@Required
	@Email
	public String email;
	public String password;
	public String salt;

	public String name;
	@ManyToMany
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	public List<RoleEntity> roles;

	@Lob
	public String aboutme;
	@Lob
	public byte[] pic;

	@Lob
	public byte[] thumbnail;

	@OneToMany(mappedBy = "climber")
	public List<AscentEntity> ascents;

	public static List<UserEntity> findAll() {
		return find.all();
	}

	public static UserEntity find(Long id) {
		return find.byId(id);
	}

	public static UserEntity findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}

	public static boolean userExists(String email) {
		return find.where().eq("email", email).findRowCount() == 1;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static boolean hasRole(String email, String role) {
		return find.where().eq("email", email).where().eq("roles.roleName", role).findRowCount() > 0;
	}

	public static UserEntity findImage(Long id) {
		return find.select("pic").where().eq("id", id).findUnique();
	}
	
	public static UserEntity findThumbnail(Long id) {
		return find.select("thumbnail,pic").where().eq("id", id).findUnique();
	}
}
