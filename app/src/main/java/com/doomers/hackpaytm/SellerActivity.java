package com.doomers.hackpaytm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SellerActivity extends Activity implements View.OnClickListener {

    Button seller_sign_in,seller_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        seller_sign_in = (Button) findViewById(R.id.seller_sign_in);
        seller_sign_up = (Button) findViewById(R.id.seller_sign_up);

        seller_sign_in.setOnClickListener(this);
        seller_sign_up.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.seller_sign_in:
                System.out.println("Activity Switched");
                Intent i1 = new Intent(this,SellerActivity.class);
                startActivity(i1);
                break;


            case R.id.seller_sign_up:

              // Intent i2 = new Intent(this,SellerSignUpActivity.class);
                //startActivity(i2);
                break;





        }
    }

}
