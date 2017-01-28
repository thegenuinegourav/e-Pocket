package com.doomers.hackpaytm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDisplayActivity extends Activity {

    private int position;
    private ImageView imageView;
    private TextView name,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);
        imageView = (ImageView)findViewById(R.id.imageview);
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);

        position = getIntent().getExtras().getInt("Position");
        switch (position)
        {
            case 0: imageView.setImageResource(R.drawable.bed); break;
            case 1: imageView.setImageResource(R.drawable.table);  name.setText("Rahul Tuteja"); price.setText("$5");break;
            case 2: imageView.setImageResource(R.drawable.sofa); name.setText("Sourabh Suri"); price.setText("$8");break;
                default: imageView.setImageResource(R.drawable.sofa); break;
        }
    }
}
