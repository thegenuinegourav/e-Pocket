package com.doomers.hackpaytm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeSellerActivity extends Activity {

    Button b1;
    EditText productname,price;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_seller);

        b1=(Button)findViewById(R.id.button);
        iv=(ImageView)findViewById(R.id.imageView);
        productname = (EditText)findViewById(R.id.editText);
        price = (EditText)findViewById(R.id.editText3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void Submit(View view) {
        if(productname.getText().length()!=0 && price.getText().length()!=0)
        Toast.makeText(getApplicationContext(),"Product added successfully!",Toast.LENGTH_LONG).show();
        else Toast.makeText(getApplicationContext(),"Please Complete the entries first",Toast.LENGTH_LONG).show();
    }
}