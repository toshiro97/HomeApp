package com.toshiro97.homeapp;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	private List<GridFragment> gridFragments;
	
	public MyPagerAdapter(FragmentManager fm, List<GridFragment> gridFragments) {
		super(fm);
		this.gridFragments = gridFragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.gridFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return this.gridFragments.size();
	}

}
