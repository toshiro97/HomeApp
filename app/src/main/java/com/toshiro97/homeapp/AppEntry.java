package com.toshiro97.homeapp;

import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppEntry implements Serializable {

	 	private final AppLoader mLoader;
	    private final ApplicationInfo mInfo;
	    private final File mApkFile;
	    private String mLabel;
	    private Drawable mIcon;
	    private boolean mMounted;
	    
	public AppEntry(AppLoader appLoader, ApplicationInfo info) {
		mLoader=appLoader;
		mInfo = info;
        mApkFile = new File(info.sourceDir);
	}

	void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }
	
	public ApplicationInfo getApplicationInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }
    
    public Drawable getIcon() {
    	if (mIcon == null) {
    		if (mApkFile.exists()) {
    			mIcon = mInfo.loadIcon(mLoader.mPm);
    			return mIcon;
    			} else {
    				mMounted = false;
    				}
    		} else if (!mMounted) {
        if (mApkFile.exists()) {
            mMounted = true;
            mIcon = mInfo.loadIcon(mLoader.mPm);
            return mIcon;
        }
    } else {
        return mIcon;
    }
    return mLoader.getContext().getResources().getDrawable(
            android.R.drawable.sym_def_app_icon);
    }
    
    @Override 
    public String toString() {
        return mLabel;
    }
    
}
