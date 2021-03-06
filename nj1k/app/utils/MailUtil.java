package utils;

import play.Play;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

public class MailUtil {

	public static void sendMail(String to, String subject, String message) {
		MailerAPI mailer = Play.application().plugin(MailerPlugin.class).email();
		mailer.setFrom("nobody@nj1k.org");
		mailer.setRecipient(to);
		mailer.setSubject(subject);
		mailer.send(message);
	}
}
