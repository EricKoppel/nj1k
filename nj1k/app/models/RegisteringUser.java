package models;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import validators.NonMemberEmail;

public class RegisteringUser {
	
	@Required
	@Email
	@NonMemberEmail
	private String email;
	
	@Required
	private String password;
	
	@Required
	private String confirmPassword;
	
	@Required
	private String name;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String validate() {
		System.out.println("passwords equal?!?");
		return password.equals(confirmPassword) ? null : Messages.get("validation.passwords.equal");
	}
	
	@Override
	public String toString() {
		return "RegisteringUser [email=" + email + ", name=" + name + "]";
	}
}
