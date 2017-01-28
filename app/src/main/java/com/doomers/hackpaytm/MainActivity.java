package com.doomers.hackpaytm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cuboid.cuboidcirclebutton.CuboidButton;

public class MainActivity extends AppCompatActivity {

    private CuboidButton addMoney,addReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMoney = (CuboidButton)findViewById(R.id.offlinedeposit);
        addReminder = (CuboidButton)findViewById(R.id.addareminder);
        if(isNetworkAvailable()){
            Intent i = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
        }
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ColorPickerActivity.class));
            }
        });
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SetReminderActivity.class));
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onPause() {
       finish();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Constants.app_back=true;
        super.onStop();
    }

    @Override
    protected void onResume() {
        Constants.app_back=false;
        super.onResume();
    }

}
