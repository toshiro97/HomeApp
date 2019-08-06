package com.toshiro97.homeapp;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class AppLoader extends AsyncTaskLoader<List<AppEntry>> {

	final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
    final PackageManager mPm;
    List<AppEntry> mApps;
    PackageIntentReceiver mPackageObserver;

	public AppLoader(Context context) {
		super(context);
		mPm = getContext().getPackageManager();
	}
	@Override
	public List<AppEntry> loadInBackground() {
		@SuppressLint("WrongConstant") List<ApplicationInfo> apps = mPm.getInstalledApplications(PackageManager.GET_GIDS);
		if (apps == null) {
            apps = new ArrayList<ApplicationInfo>();
        }
		final Context context = getContext();
		List<AppEntry> entries = new ArrayList<AppEntry>();
        
        for (ApplicationInfo app : apps) {
            if(mPm.getLaunchIntentForPackage(app.packageName) != null) {
                // get all apps with launcher intent
                if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                    //get all updated system apps

                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    // system apps

                } else {
                    //get all user installed apps
                }
            	AppEntry entry = new AppEntry(this, app);
	            entry.loadLabel(context);
	            entries.add(entry);
            }

        }
        Collections.sort(entries, ALPHA_COMPARATOR);
        return entries;
	}
	
	public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
	    private final Collator sCollator = Collator.getInstance();
	    @Override
	    public int compare(AppEntry object1, AppEntry object2) {
	        return sCollator.compare(object1.getLabel(), object2.getLabel());
	    }
	};
	
	@Override
	public void deliverResult(List<AppEntry> apps) {
        if (isReset()) {
            if (apps != null) {
                onReleaseResources(apps);
            }
        }
        List<AppEntry> oldApps = mApps;
        mApps = apps;

        if (isStarted()) {
            super.deliverResult(apps);
        }
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }
    @Override 
    protected void onStartLoading() {
        if (mApps != null) {
            deliverResult(mApps);
        }
        
        if (mPackageObserver == null) {
            mPackageObserver = new PackageIntentReceiver(this);
        }

        boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

        if (takeContentChanged() || mApps == null || configChange) {
            forceLoad();
        }
    }
    @Override protected void onStopLoading() {
        cancelLoad();
    }
    @Override public void onCanceled(List<AppEntry> apps) {
        super.onCanceled(apps);
        onReleaseResources(apps);
    }
    @Override protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mApps != null) {
            onReleaseResources(mApps);
            mApps = null;
        }
        if (mPackageObserver != null) {
            getContext().unregisterReceiver(mPackageObserver);
            mPackageObserver = null;
        }
    }
    protected void onReleaseResources(List<AppEntry> apps) {
    }
}
