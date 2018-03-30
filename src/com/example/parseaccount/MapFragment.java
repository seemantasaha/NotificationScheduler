package com.example.parseaccount;

import com.google.android.gms.maps.SupportMapFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {
  public View mOriginalContentView;
  public TouchableWrapper mTouchView;   

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	  //super.onCreate(savedInstanceState);
    mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState); 
    	mTouchView = new TouchableWrapper(getActivity());
    	mTouchView.addView(mOriginalContentView);
        return mTouchView;
  }

  @Override
  public View getView() {
    return mOriginalContentView;
  }
}