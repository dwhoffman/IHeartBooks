package com.iheartbooks;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class IHeartBooksActivity extends TabActivity {

	private TabHost _tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		_tabHost = getTabHost();
		_tabHost.getTabWidget().setDividerDrawable(R.drawable.divider_vertical_dark);
		
		setupActivityTab(new Intent().setClass(this, SearchActivity.class), "Search");
		setupActivityTab(new Intent().setClass(this, AccountActivity.class), "Account");
		setupActivityTab(new Intent().setClass(this, ReadActivity.class), "Read");	
		
		_tabHost.getTabWidget().setStripEnabled(true);
	}
    
	private void setupActivityTab(final Intent activityIntent, final String tag) {
		View tabView = createTabView(_tabHost.getContext(), tag);
		_tabHost.addTab(_tabHost.newTabSpec(tag)
				.setIndicator(tabView)
				.setContent(activityIntent));
	}
	
	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView textView = (TextView)view.findViewById(R.id.tabsText);
		textView.setText(text);
		return view;
	}
}