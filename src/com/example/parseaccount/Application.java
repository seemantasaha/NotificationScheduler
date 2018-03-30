package com.example.parseaccount;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;

public class Application extends android.app.Application {

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

    String app_id = "tHsAybkTTG0MsxtqweOyKzCYzY1NzguoJ179wQEW";
    String client_key = "SUYEyhd6a319pmocl8u6JtBmYWe3rkUdBtjLmlaK";

    // Add your initialization code here
    Parse.initialize(this, app_id, client_key);
    
    // Specify an Activity to handle all pushes by default.
    PushService.setDefaultPushCallback(this, MainActivity.class);
  }
}