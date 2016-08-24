package com.danielme.android.customtabs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomTabsBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    Toast.makeText(context, intent.getStringExtra("text"), Toast.LENGTH_SHORT).show();

  }
}
