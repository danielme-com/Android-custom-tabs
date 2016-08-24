package com.danielme.android.customtabs;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

  private static final String URL = "https://danielme.com";
  private static final String CHROME_PACKAGE = "com.android.chrome";

  private CustomTabsServiceConnection ctConnection;
  private CustomTabsSession customTabsSession;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ctConnection = new CustomTabsServiceConnection() {
      @Override
      public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient
          customTabsClient) {
        customTabsClient.warmup(0);
        customTabsSession = getSession(customTabsClient);
        customTabsSession.mayLaunchUrl(Uri.parse(URL), null, null);
      }
      @Override
      public void onServiceDisconnected(ComponentName componentName) {
        //nothing here
      }
    };

    CustomTabsClient.bindCustomTabsService(this, CHROME_PACKAGE, ctConnection);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (ctConnection != null) {
      unbindService(ctConnection);
    }
  }

  private CustomTabsSession getSession(CustomTabsClient customTabsClient) {
    if (customTabsClient != null) {
      return customTabsClient.newSession(new CustomTabsCallback() {
        /*@Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
          super.onNavigationEvent(navigationEvent, extras);
        }*/
      });
    }
    return null;
  }

  public void openTab(View view) {
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(customTabsSession);
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary));
    builder.setShowTitle(true);
    builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(),
        R.drawable.ic_arrow_back));

    builder.addMenuItem(getString(R.string.menu1), createIntent(R.string.menu1, 1));
    builder.addMenuItem(getString(R.string.menu2), createIntent(R.string.menu2, 2));

    Bitmap icon = BitmapFactory.decodeResource(getResources(),
        android.R.drawable.ic_menu_add);
    builder.setActionButton(icon, getString(R.string.action), createIntent(R.string.action, 3),
        true);

    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(this, Uri.parse(URL));
  }

  private PendingIntent createIntent(int text, int number) {
    Intent intent = new Intent(this, CustomTabsBroadcastReceiver.class);
    intent.putExtra("text", getString(text));
    return PendingIntent.getBroadcast(this, number, intent, 0);
  }

}