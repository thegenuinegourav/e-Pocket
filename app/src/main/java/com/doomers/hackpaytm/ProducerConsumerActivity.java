package com.doomers.hackpaytm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class ProducerConsumerActivity extends Activity {

    private ToggleButton t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_consumer);
        t = (ToggleButton)findViewById(R.id.toggle);
    }

    public void clickedToggle(View v)  //When toggleButton clicked
    {

    }

    public void pressed(View v)
    {
        if(t.getText().toString().equals("Buyer")){
            //startActivity(new Intent(ProducerConsumerActivity.this,BrowseImageActivity.class));
//            PackageManager manager = this.getPackageManager();
//            //manager.resolveActivity(i,0);
//            Intent i = manager.getLaunchIntentForPackage("com.catchoom.craftarsdkexamples");
//            i.setAction(Intent.ACTION_MAIN);
//            i.addCategory(Intent.CATEGORY_LAUNCHER);
//            ComponentName componentName = new ComponentName("com.catchoom.craftarsdkexamples","BrowseImageActivity");
//            i.setComponent(componentName);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(i);
            //startActivity(new Intent(ProducerConsumerActivity.this,BrowseImageActivity.class));
            PackageManager manager = this.getPackageManager();




            Intent i = manager.getLaunchIntentForPackage("com.catchoom.craftarsdkexamples");

            if (i == null) {

                //throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            this.startActivity(i);


        }
        else {
            startActivity(new Intent(ProducerConsumerActivity.this, SellerActivity.class));
        }
    }
}

