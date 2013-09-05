package models;

import java.util.Arrays;

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
	private char[] password;
	
	@Required
	private char[] confirmPassword;
	
	@Required
	private String name;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public char[] getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(char[] confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String validate() {
		return Arrays.equals(password, confirmPassword) ? null : Messages.get("validation.passwords.equal");
	}
	
	@Override
	public String toString() {
		return "RegisteringUser [email=" + email + ", name=" + name + "]";
	}
}
