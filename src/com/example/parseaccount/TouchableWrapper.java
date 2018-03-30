package com.example.parseaccount;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

  public TouchableWrapper(Context context) {
    super(context);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
            SetNotificationActivity.myScroll.requestDisallowInterceptTouchEvent(true);
            break;
      case MotionEvent.ACTION_UP:
    	  	SetNotificationActivity.myScroll.requestDisallowInterceptTouchEvent(false);
            break;
    }
    return super.dispatchTouchEvent(event);
  }
}