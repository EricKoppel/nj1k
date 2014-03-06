package utils;

public class MailRunnable implements Runnable {

	private String to;
	private String subject;
	private String message;

	public MailRunnable(String to, String subject, String message) {
		this.to = to;
		this.subject = subject;
		this.message = message;
	}

	@Override
	public void run() {
		MailUtil.sendMail(to, subject, message);
	}
}
