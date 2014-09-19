package models;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;

public class Contact {
	
	@Email
	@Required
	private String email;
	
	@Required
	private String subject;
	
	@Required
	private String message;
	
	@Required
	@Pattern(value = "(?i)\\s*high\\s*point\\s*", message="validation.antispam")
	private String answer;

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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
