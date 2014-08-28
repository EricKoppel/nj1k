package utils;

import java.util.List;

public class NYNJTCNewsArticles {

	private String etag;
	private String lastModified;
	private List<NYNJTCNewsArticle> articles;
	
	
	public List<NYNJTCNewsArticle> getArticles() {
		return articles;
	}
	public void setArticles(List<NYNJTCNewsArticle> articles) {
		this.articles = articles;
	}
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	@Override
	public String toString() {
		return "NYNJTCNewsArticles [etag=" + etag + ", articles=" + articles + "]";
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}
