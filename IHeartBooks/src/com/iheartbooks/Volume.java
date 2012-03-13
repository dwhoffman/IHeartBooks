package com.iheartbooks;

import java.util.List;

public class Volume {

	///Id property
	private String id;
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	///Selflink property
	private String selfLink;
	public String getSelfLink() { return this.selfLink; }
	public void setSelfLink(String selfLink) { this.selfLink = selfLink; }
	
	///Title property
	private String title;
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }
	
	///Authors property
	private List<Author> authors;
	public List<Author> getAuthors() { return this.authors; }
	public void setAuthors(List<Author> authors) { this.authors = authors; }
	
	///Description property
	private String description;
	public String getDescription() { return this.description; }
	public void setDescription(String description) { this.description = description; }
	
	///PublishedDate property
	private String publishedDate;
	public String getPublishedDate() { return this.publishedDate; }
	public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
	
	///Thumbnail property
	private String thumbnail;
	public String getThumbnail() { return this.thumbnail; }
	public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
	
	///SmallThumbnail property
	private String smallThumbnail;
	public String getSmallThumbnail() { return this.smallThumbnail; }
	public void setSmallThumbnail(String smallThumbnail) { this.smallThumbnail = smallThumbnail; }
	
	///Constructors
	public Volume(){
		
	}
	public Volume(String id, String title, List<Author> authors, String selfLink, String publishedDate, String description, String thumbnail, String smallThumbnail) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.selfLink = selfLink;
		this.publishedDate = publishedDate;
		this.description = description;
		this.thumbnail = thumbnail;
		this.smallThumbnail = smallThumbnail;
	}
}
