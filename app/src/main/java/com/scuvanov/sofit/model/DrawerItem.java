package com.scuvanov.sofit.model;

public class DrawerItem {

	public static final int DRAWER_ITEM_TAG_FEED = 10;
	public static final int DRAWER_ITEM_TAG_USER = 11;
	/*public static final int DRAWER_ITEM_TAG_SETTINGS = 12;*/
	public static final int DRAWER_ITEM_TAG_TIMERS = 12;
	public static final int DRAWER_ITEM_TAG_CALENDAR = 13;

	
	public DrawerItem(int icon, int title, int tag) {
		this.icon = icon;
		this.title = title;
		this.tag = tag;
	}

	private int icon;
	private int title;
	private int tag;

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}
