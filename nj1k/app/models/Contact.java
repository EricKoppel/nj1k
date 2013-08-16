package models;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class Contact {

	@Email
	@Required
	private String email;
	
	@Required
	private String subject;
	
	@Required
	private String message;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
