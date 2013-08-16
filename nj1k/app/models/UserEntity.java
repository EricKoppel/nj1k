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
import play.db.ebean.Model;
import validators.MemberEmail;

@Entity
public class UserEntity extends Model {

	private static final long serialVersionUID = 1L;

	public static Finder<Long, UserEntity> find = new Finder<Long, UserEntity>(Long.class, UserEntity.class);
			
	@Id
	public Long id;
	@Column(unique=true)
	
	@Required
	@Email
	@MemberEmail
	public String email;
	public String password;
	public String salt;
	
	public String name;
	@ManyToMany
	@JoinTable(name="users_roles",
        joinColumns=
            @JoinColumn(name="user_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="role_id", referencedColumnName="id")
        )
	
	public List<RoleEntity> roles;
	
	@Lob 
	public String aboutme;
	@Lob 
	public byte[] pic;
	@OneToMany(mappedBy="climber")
	public List<AscentEntity> ascents;
	
	public static List<UserEntity> findAll() {
		return find.all();
	}
	
	public static UserEntity find(Long id) {
		return find.byId(id);
	}
	
	public static UserEntity findCompleteUser(Long id) {
		return find.select("name,aboutme").where().eq("id", id).findUnique();
	}
	
	public static UserEntity findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}

	@Override
	public String toString() {
		return name;
	}
}
