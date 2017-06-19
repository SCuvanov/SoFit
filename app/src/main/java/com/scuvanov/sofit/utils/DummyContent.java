package com.scuvanov.sofit.utils;

import com.scuvanov.sofit.model.DummyModel;

import java.util.ArrayList;


public class DummyContent {
	
	/* This method gives us just a dummy content - array list
	 * of ImageGalleryCategoryModels. Every model has id that is
	 * need for some classes (e.g. DefaultAdapter.java).
	 * Favourites are randomly chosen to be true or false.
	 * Last category is randomly added to the list so you could
	 * see when there are even or odd numbers of categories in
	 * ImageGalleryActivity.
	 */

	public static ArrayList<DummyModel> getDummyModelList() {
		ArrayList<DummyModel> list = new ArrayList<DummyModel>();

		list.add(new DummyModel(0, "http://pengaja.com/uiapptemplate/avatars/0.jpg", "Isaac Reid"));
		list.add(new DummyModel(1, "http://pengaja.com/uiapptemplate/avatars/1.jpg", "Jason Graham"));
		list.add(new DummyModel(2, "http://pengaja.com/uiapptemplate/avatars/2.jpg", "Abigail Ross"));
		list.add(new DummyModel(3, "http://pengaja.com/uiapptemplate/avatars/3.jpg", "Justin Rutherford"));
		list.add(new DummyModel(4, "http://pengaja.com/uiapptemplate/avatars/4.jpg", "Nicholas Henderson"));
		list.add(new DummyModel(5, "http://pengaja.com/uiapptemplate/avatars/5.jpg", "Elizabeth Mackenzie"));
		list.add(new DummyModel(6, "http://pengaja.com/uiapptemplate/avatars/6.jpg", "Melanie Ferguson"));
		list.add(new DummyModel(7, "http://pengaja.com/uiapptemplate/avatars/7.jpg", "Fiona Kelly"));
		list.add(new DummyModel(8, "http://pengaja.com/uiapptemplate/avatars/8.jpg", "Nicholas King"));
		list.add(new DummyModel(9, "http://pengaja.com/uiapptemplate/avatars/9.jpg", "Victoria Mitchell"));
		list.add(new DummyModel(10, "http://pengaja.com/uiapptemplate/avatars/10.jpg", "Sophie Lyman"));
		list.add(new DummyModel(11, "http://pengaja.com/uiapptemplate/avatars/11.jpg", "Carl Ince"));
		list.add(new DummyModel(12, "http://pengaja.com/uiapptemplate/avatars/12.jpg", "Michelle Slater"));
		list.add(new DummyModel(13, "http://pengaja.com/uiapptemplate/avatars/13.jpg", "Ryan Mathis"));
		list.add(new DummyModel(14, "http://pengaja.com/uiapptemplate/avatars/14.jpg", "Julia Grant"));
		list.add(new DummyModel(15, "http://pengaja.com/uiapptemplate/avatars/15.jpg", "Hannah Martin"));
		
		return list;
	}
}
