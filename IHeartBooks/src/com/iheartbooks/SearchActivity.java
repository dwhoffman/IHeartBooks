package com.iheartbooks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		((Button)findViewById(R.id.btnSearch)).setOnClickListener(_searchButtonClick);
	}
	
	private JSONObject _responseJsonData = null;
	private SearchResults _searchResults = null;	
	
	private OnClickListener _searchButtonClick = new OnClickListener() {
		public void onClick(View view) {
			//Want to get the soft keyboard out of the way,
			hideSoftKeyboard();
			String searchText = getSearchText().trim();
			if(searchText.length() == 0) {
				showToast("Please provide search criteria");
				return;
			} 
			new SearchTask().execute(
				getString(R.string.google_search_base_url), 
				getSearchText(), 
				"&key=".concat(getString(R.string.google_api_key)));
		}
	};
	
	private void hideSoftKeyboard() {
		InputMethodManager inputMethMgr = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
		inputMethMgr.hideSoftInputFromWindow(((EditText)findViewById(R.id.editTextSearch)).getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	private String getSearchText() {
		EditText editTextSearch = (EditText)findViewById(R.id.editTextSearch);
		String searchText = editTextSearch.getText().toString().replace(" ", "+");
		return searchText;
	}
	
	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
	
	private void writeException(Exception exception) {
		exception.printStackTrace();
	}
	
	private void processSearchResults() {
		JSONObject searchJson = _responseJsonData;
		SearchResults results = new SearchResults();
		List<Volume> volumes = new ArrayList<Volume>();
		
		try {
			results.setKind(searchJson.getString("kind"));
			JSONArray items = searchJson.getJSONArray("items");
			for(int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				Volume vol = getVolumeFromJson(item); 				
				volumes.add(vol);
			}
			results.setItems(volumes);
			setListAdapter(new VolumeListAdapter(this, R.layout.search_item, results.getItems()));
		} catch(JSONException jsonEx) {
			writeException(jsonEx);
			showToast("Exception during result JSON parsing!");
		} catch(JsonSyntaxException jsEx) {
			writeException(jsEx);
			showToast("Exception during result parsing!");
		}
		
		int resultCount = results.getNumberOfItems();
		showToast(results.getKind() + " " + Integer.toString(resultCount));
		TextView result = (TextView)findViewById(R.id.textViewSearchResults);
		result.setText(Integer.toString(resultCount));
		_searchResults = results;
	}

	private Volume getVolumeFromJson(JSONObject volumeJson) throws JSONException {
		Volume volume = new Volume();
		
		volume.setId(volumeJson.getString("id")); //Guaranteed to get 'id', no null check needed
		volume.setSelfLink(!volumeJson.isNull("selfLink") ? volumeJson.getString("selfLink") : "");
		
		if(!volumeJson.isNull("volumeInfo")) {
			JSONObject volInfoJson = volumeJson.getJSONObject("volumeInfo");
			
			volume.setTitle(volInfoJson.getString("title"));
			volume.setPublishedDate(volInfoJson.getString("publishedDate")); 
			//vol.setDescription(volInfo.getString("description"));
			
			setVolumeImages(volume, volInfoJson);
			setVolumeAuthors(volume, volInfoJson);
		}
		
		return volume;
	}
	
	private void setVolumeImages(Volume volume, JSONObject volumeInfoJson) throws JSONException {
		if(volumeInfoJson.isNull("imageLinks")) return; 
		JSONObject imgLinks = volumeInfoJson.getJSONObject("imageLinks");
		volume.setThumbnail(imgLinks.getString("thumbnail"));
		volume.setSmallThumbnail(imgLinks.getString("smallThumbnail"));
	}
	
	private void setVolumeAuthors(Volume volume, JSONObject volumeInfoJson) throws JSONException {
		if(volumeInfoJson.isNull("authors")) return;
		List<Author> authors = new ArrayList<Author>();
		JSONArray authInfo = volumeInfoJson.getJSONArray("authors");
		for(int a = 0; a < authInfo.length(); a++) {
			Author author = new Author(authInfo.optString(a));
			authors.add(author);
		}
		volume.setAuthors(authors);
	}
	
	private class SearchTask extends AsyncTask<String, Void, JSONObject> {
		private Exception _asyncException = null;
		
		protected JSONObject doInBackground(String... params) {
			HttpParams httpParms = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParms, 5000);
			HttpConnectionParams.setSoTimeout(httpParms, 5000);
			DefaultHttpClient client = new DefaultHttpClient(httpParms);
			
			String baseUrl = params[0];
			String queryString = params[1];
			String apiKey = params[2];
			
			String requestUrl = baseUrl.concat(queryString).concat(apiKey);
			HttpGet get = new HttpGet(requestUrl);
			get.setHeader("content-type", "application/json");
			
			HttpResponse response = null;
			String responseString = null;
			JSONObject responseJson = null;
			
			try {
				response = client.execute(get);
				responseString = EntityUtils.toString(response.getEntity());
				responseJson = new JSONObject(responseString);
			} catch (Exception e) {
				this._asyncException = e;
			}

			return responseJson;
		}
		
		protected void onPostExecute(JSONObject result) {
			if(this._asyncException != null) {
				// An exception was encountered during the async task, handle it here
				Class<? extends Exception> exClass = this._asyncException.getClass();
				showToast(exClass.getName().toString() + " Exception occurred.");
			} else {
				_responseJsonData = result;
			}
			
			processSearchResults();
		}
	}

	private class VolumeListAdapter extends ArrayAdapter<Volume> {
		
		private List<Volume> _items;
		
		public VolumeListAdapter(Context context, int textViewResourceId, List<Volume> objects) {
			super(context, textViewResourceId, objects);
			_items = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = vi.inflate(R.layout.search_item, null);
	        }
	        Volume volume = _items.get(position);
	        if (volume != null) {
		        ImageView thumbnailImage = (ImageView)view.findViewById(R.id.searchItemImage);
		        InputStream inputStream = null;
				try {
					inputStream = (InputStream)new URL(volume.getSmallThumbnail()).getContent();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(inputStream != null) {
					Drawable thumbImg = Drawable.createFromStream(inputStream, "thumb");
		        	thumbnailImage.setImageDrawable(thumbImg);
				}
				
		        TextView titleText = (TextView)view.findViewById(R.id.searchItemTitle);
		        titleText.setText(volume.getTitle());
		        
		        TextView descriptText = (TextView)view.findViewById(R.id.searchItemDescription);
		        descriptText.setText(volume.getDescription());
	        }
	     
	        return view;
		}
	}

}
