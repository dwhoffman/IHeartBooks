package com.iheartbooks;

import java.util.List;

public class SearchResults {
	///Kind property
	private String kind;
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getKind() {
		return this.kind;
	}
	
	///Items property
	private List<Volume> items;
	public void setItems(List<Volume> items) {
		this.items = items;
	}
	public List<Volume> getItems() {
		return this.items;
	}
	
	public int getNumberOfItems() {
		if(this.items == null) 
			return 0;
		return this.items.size();
	}
	
	///Constructors
	public SearchResults(){
		// no-args constructor required for GSON parsing
	}
	public SearchResults(String kind, List<Volume> items) {
		this.kind = kind;
		this.items = items;
	}
}
