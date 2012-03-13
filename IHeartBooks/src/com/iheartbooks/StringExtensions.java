package com.iheartbooks;

public class StringExtensions {

	public static Boolean isNullOrEmpty(String value) {
		return (value == null || value.trim().isEmpty());
	}
	
	public static Boolean isWhitespace(String value) {
		boolean whitespaceOnly = true;
		if(!isNullOrEmpty(value)) {
			char[] chars = value.trim().toCharArray();
			for(char c:chars) {
				if(!Character.isWhitespace(c)) {
					whitespaceOnly = false;
				}
			}
		}
		return whitespaceOnly;
	}
}
