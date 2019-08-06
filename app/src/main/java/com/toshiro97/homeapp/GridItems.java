package com.toshiro97.homeapp;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class GridItems {

	 public int id;
	 public String title;
	 public Drawable drawable;
	 public ApplicationInfo info;
	 
	 public GridItems(int id, String address, Drawable drawable, ApplicationInfo info) {
	  this.id = id;
	  this.title = address;
	  this.drawable=drawable;
	  this.info=info;
	 }
}
