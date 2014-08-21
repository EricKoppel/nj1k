package utils;

import java.util.Date;


public class NYNJTCNewsArticle implements Comparable<NYNJTCNewsArticle> {
	
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

	@Override
	public int compareTo(NYNJTCNewsArticle o) {
		return o.getDate().compareTo(this.getDate());
	}
}
