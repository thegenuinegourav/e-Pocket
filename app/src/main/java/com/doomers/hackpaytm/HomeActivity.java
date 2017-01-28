package com.doomers.hackpaytm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences sharedPreferences;
    private String amount;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 54321;

    Button btn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            //To retrieve data from sharedPreference
            sharedPreferences = getSharedPreferences("PasswordPassColors", Context.MODE_PRIVATE); //First arg is filename and second arg is value i.e. private i.e. only app can access this file
            amount=sharedPreferences.getString("Blue", "Incorrect");
            SharedPreferences.Editor editor = sharedPreferences.edit();  //to edit the sharedPreference

            if(!amount.equals("Incorrect"))
            {
                notification = new NotificationCompat.Builder(this);
                notification.setAutoCancel(true);
                notification.setSmallIcon(R.drawable.paytm);
                notification.setTicker("Now you can add " + amount + " rupees");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Reminder");
                notification.setContentText("Click to add " + amount + " rupees");

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notification.setSound(alarmSound);

                                        Intent intent1 = new Intent(this,MoneyTransferActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueID,notification.build());
                //Adding values to sharedPreference
                editor.putString("Blue", "Incorrect");
                //Commiting the changes in the sharedPreference
                editor.commit();
            }
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Online_shopping) {
           startActivity(new Intent(HomeActivity.this,ProducerConsumerActivity.class));
        }else if(id == R.id.Analytics){
            startActivity(new Intent(HomeActivity.this,AnalyticsApi.class));
        }else if(id == R.id.scan){
            startActivity(new Intent(HomeActivity.this,ColorPickerActivity.class));
        }else if(id==R.id.reminder){
            startActivity(new Intent(HomeActivity.this,SetReminderActivity.class));
        }else if(id == R.id.addwithvoice){
            startActivity(new Intent(HomeActivity.this,AddWithVoiceRecognition.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
