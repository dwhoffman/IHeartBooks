package com.iheartbooks;

public class Author {
	private String lastName;
	public String getLastName() { 
		return this.lastName; 
	}
	
	private String firstName;
	public String getFirstName() { 
		return this.firstName; 
	}
	
	public Author(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Author(String fullName) 
			throws NullPointerException {
		String firstName;
		String lastName;
		String[] names;
		
		if(!StringExtensions.isNullOrEmpty(fullName)) {
			if(fullName.trim().contains(",")) {
				names = fullName.split(",");
				lastName = names[names.length-1].trim(); 
			} else {
				names = fullName.split(" ");
				lastName = names[names.length-1].trim();
			}
			firstName = fullName.replace(lastName, "").trim();
			firstName = firstName.replace(",", "");
			new Author(firstName, lastName);
		} else {
			throw new NullPointerException();
		}
	}
}
