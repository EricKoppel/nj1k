package models;

import java.util.Arrays;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import validators.NonMemberEmail;

public class RegisteringUser {
	
	@Required
	@Email
	@NonMemberEmail
	private String email;
	
	@Required
	@Size(min = 6, max = 32, message = "password.length")
	private char[] password;
	
	@Required
	private char[] confirmPassword;
	
	@Required
	private String name;
	
	@Required
	@Pattern(value = "(?i)\\s*high\\s*point\\s*", message="validation.antispam")
	private String challenge;
	
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

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public String validate() {
		return Arrays.equals(password, confirmPassword) ? null : Messages.get("validation.passwords.equal");
	}
	
	@Override
	public String toString() {
		return "RegisteringUser [email=" + email + ", name=" + name + "]";
	}
}
