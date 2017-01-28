package com.doomers.hackpaytm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class BrowseImageActivity extends Activity {

    private ListView listView;
    private String[] HeadingNames={"Dog1","Dog2","Dog3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);

        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(new CustomAdapter(this,HeadingNames));
    }
}
