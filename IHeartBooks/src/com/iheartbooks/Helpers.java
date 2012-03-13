package com.iheartbooks;

public class Helpers {

	public static String appendApiKey(String requestUrl, String apiKey) {
		requestUrl = requestUrl.concat("&key=");
		requestUrl = requestUrl.concat(apiKey);
		return requestUrl;
	}
	
}
