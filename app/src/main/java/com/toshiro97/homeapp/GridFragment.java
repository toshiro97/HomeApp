package com.toshiro97.homeapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.toshiro97.homeapp.GridAdapter.ViewHolder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GridFragment extends Fragment {

	 private GridView mGridView;
	 private GridView dockView;
	 private GridAdapter mGridAdapter;
	 GridItems[] gridItems = {};
	 private Activity mainActivity;

	public GridFragment() {
	}

	@SuppressLint("ValidFragment")
	public GridFragment(GridItems[] gridItems, Activity mainActivity) {
		this.gridItems = gridItems;
		this.mainActivity = mainActivity;
	}
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	  View view;
	  view = inflater.inflate(R.layout.grid_page, container, false);
	  mGridView = (GridView) view.findViewById(R.id.gridView);
	  dockView = (GridView) view.findViewById(R.id.dock);
	  
	  float scaleFactor = getResources().getDisplayMetrics().density * 100;
	  int number = mainActivity.getWindowManager().getDefaultDisplay().getWidth();
	  int columns = (int) ((float) number / (float) scaleFactor);
	  mGridView.setNumColumns(4);
	  return view;
	 }
	
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	 
	  if (mainActivity != null) {
	 
	   mGridAdapter = new GridAdapter(mainActivity, gridItems);
	   
	   if (mGridView != null) {
	    mGridView.setAdapter(mGridAdapter);
	   }
	 
	   mGridView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView parent, View view,
	      int position, long id) {
	     onGridItemClick((GridView) parent, view, position, id);
	    }
	   });
	   
	   mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			dragANDdrop(parent,view,position,id);
			return false;
		}
	   });
	   }
	  }

	 protected void dragANDdrop(AdapterView<?> parent, View view,
				int position, long id) {

                 int count = parent.getChildCount();
                 for (int i = 0; i < count; i++) {
                     View curr = parent.getChildAt(i);
                     curr.setOnDragListener(new View.OnDragListener() {

						@Override
						public boolean onDrag(View v, DragEvent event) {
							boolean result = true;
                            int action = event.getAction();
                            switch (action)
                            {
                            case DragEvent.ACTION_DRAG_STARTED:
                                break;
                            case DragEvent.ACTION_DRAG_LOCATION:
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                break;
                            case DragEvent.ACTION_DROP:
                            	if(event.getLocalState()==v){result=false;}
                            	else{

                            		View droped=(View)event.getLocalState();
                            		GridItems dropItem = ((ViewHolder)droped.getTag()).item;

                            		GridView parent= (GridView)droped.getParent();
                            		GridAdapter gridAdapter = (GridAdapter)parent.getAdapter();
                            		GridItems[] gridItemss = gridAdapter.getItems();

                            		View target=v;

                            		GridItems targetItem = ((ViewHolder)target.getTag()).item;
                            		List<GridItems> list = new ArrayList<GridItems>(Arrays.asList(gridItemss));
                            		int index = list.indexOf(targetItem);
                            		list.remove(dropItem);
                            		list.add(index, dropItem);

                            		gridItemss = list.toArray(gridItemss);
                            		gridAdapter.notifyDataSetChanged();
                            	}
                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                break;
                            default:
                                result = false;
                                break;
                            }
							return result;
						}
					});
                 }
                 int relativePosition = position - parent.getFirstVisiblePosition();
                 View target=(View)parent.getChildAt(relativePosition);

                 ViewHolder holder= ((ViewHolder)target.getTag());
                 GridItems currItem=holder.item;
                 String path=currItem.title;
                 ClipData clipData=ClipData.newPlainText("DragData", path);
                 target.startDrag(clipData, new View.DragShadowBuilder(target), target, 0);

	}

	public void onGridItemClick(GridView g, View v, int position, long id) {
		ApplicationInfo info = gridItems[position].info;
		Intent intent= getActivity().getPackageManager().getLaunchIntentForPackage(info.packageName);
		startActivity(intent);
		}
	 
	 
}