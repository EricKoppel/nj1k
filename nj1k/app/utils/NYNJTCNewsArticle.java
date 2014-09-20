package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Element;

import play.Configuration;


public class NYNJTCNewsArticle {
	
	private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			return new SimpleDateFormat(Configuration.root().getString("render.date.format"));
		}
	}; 
	
	private String title;
	private Date date;
	private String link;
	
	public NYNJTCNewsArticle(String title, Date date, String link) {
		this.title = title;
		this.date = date;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "NYNJTCNewsArticle [title=" + title + ", date=" + date + ", link=" + link + "]";
	}
	
	public static Date extractDate(Element row) {
		try {
			return dateFormat.get().parse(row.child(0).getElementsByTag("span").get(0).text());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String extractTitle(Element row) {
		return row.child(2).getElementsByTag("a").get(0).text();
	}
	
	public static String extractURL(Element row) {
		return row.child(2).getElementsByTag("a").attr("abs:href");
	}
}
