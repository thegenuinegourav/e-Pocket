// com.craftar.craftarexamples is free software. You may use it under the MIT license, which is copied
// below and available at http://opensource.org/licenses/MIT
//
// Copyright (c) 2014 Catchoom Technologies S.L.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of
// this software and associated documentation files (the "Software"), to deal in
// the Software without restriction, including without limitation the rights to use,
// copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
// Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
// DEALINGS IN THE SOFTWARE.

package com.doomers.hackpaytm;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.craftar.CraftARActivity;
import com.craftar.CraftARError;
import com.craftar.CraftARItem;
import com.craftar.CraftARItemAR;
import com.craftar.CraftAROnDeviceCollection;
import com.craftar.CraftAROnDeviceCollectionManager;
import com.craftar.CraftARSDK;
import com.craftar.CraftARTracking;


public class OnDeviceARActivity extends CraftARActivity {

	private final String TAG = "OnDeviceARActivity";

	CraftARTracking mTracking;
	CraftARSDK mCraftARSDK;
    CraftAROnDeviceCollectionManager mCollectionManager;
    CraftAROnDeviceCollection mCollection;
    private Toast mToast;
    private View mScanningLayout;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CraftAROnDeviceCollectionManager collectionManager =
//                CraftAROnDeviceCollectionManager.Instance();
//        CraftAROnDeviceCollection collection = collectionManager.get("bf6e85d7656030e570b67e5f362e52148f2c3448");
//        if (collection != null) {
//            // Collection is already added to the device
//        } else {
//            // Add the collection to the local database
//            collectionManager.addCollection("arbundle.zip", (CraftAROnDeviceCollectionManager.AddCollectionListener) this);
//        }
//        collection.sync((CraftAROnDeviceCollectionManager.SyncCollectionListener)this);

    }
		
	@Override
	public void onPostCreate() {
		
		View mainLayout= getLayoutInflater().inflate(R.layout.activity_ar_programmatically_ar_from_craftar, null);
		setContentView(mainLayout);

        mScanningLayout = findViewById(R.id.layout_scanning);
        mScanningLayout.setVisibility(View.GONE);

        /**
         * Get the CraftAR SDK instance and start capturing
         */
		mCraftARSDK = CraftARSDK.Instance();
		mCraftARSDK.startCapture(this);
        /**
         * Get the instance of the Tracking
         */
		mTracking = CraftARTracking.Instance();
		
	}


    @Override
    public void onPreviewStarted(int width,int height){

        mCollection =  CraftAROnDeviceCollectionManager.Instance().get(Config.MY_COLLECTION_TOKEN);
        /**
         * As the on-device collection is already in the device (we did it in the Splash Screen), we will add the collection items
         * to the tracking and start the AR experience.
         */
        loadCollection();
 
    }

    private void loadCollection() {
        // Get all item UUIDs in the collection
        for(String itemUUID: mCollection.listItems()){
            // Get the item and check that it is an AR item
            CraftARItem item = mCollection.getItem(itemUUID);
            if(item.isAR()){
                CraftARItemAR itemAR = (CraftARItemAR)item;
                CraftARError error = mTracking.addItem(itemAR);
                if (error != null) {
                    showToast(error.getErrorMessage(), Toast.LENGTH_SHORT);
                }
            }
        }
        // Start tracking this collection.
        mTracking.startTracking();
    }

    private void showToast(String toastText, int toastDuration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getApplicationContext(), toastText,	toastDuration);
        mToast.show();

    }

    @Override
    public void finish() {
        /**
         * Stop Tracking and clean the AR scene
         */
        mTracking.stopTracking();
        mTracking.removeAllItems();
        super.finish();
    }

	@Override
	public void onCameraOpenFailed() {
		Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_SHORT).show();				
		
	}
}
