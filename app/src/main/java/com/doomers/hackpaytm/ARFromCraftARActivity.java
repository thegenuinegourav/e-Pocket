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

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.craftar.CraftARActivity;
import com.craftar.CraftARCloudRecognition;
import com.craftar.CraftARError;
import com.craftar.CraftARItem;
import com.craftar.CraftARResult;
import com.craftar.CraftARSDK;
import com.craftar.CraftARSearchResponseHandler;
import com.craftar.CraftARTracking;
import com.craftar.ImageRecognition;

import java.util.ArrayList;


public class ARFromCraftARActivity extends CraftARActivity implements CraftARSearchResponseHandler, ImageRecognition.SetCollectionListener {

	private final String TAG = "ARFromCraftARActivity";

	private View mScanningLayout;

	CraftARSDK mCraftARSDK;
	CraftARTracking mTracking;
	CraftARCloudRecognition mCloudIR;

	@Override
	public void onPostCreate() {
        View mainLayout = getLayoutInflater().inflate(R.layout.activity_ar_programmatically_ar_from_craftar, null);
        setContentView(mainLayout);
        /**
         * Get the CraftAR SDK instance and initialize the capture
         * passing the current activity
         *
         * When the capture is ready onPreviewStarted() will be called.
         */
        mCraftARSDK = CraftARSDK.Instance();



        mCraftARSDK.init(getApplicationContext());
        mCraftARSDK.startCapture((CraftARActivity)this);
        /**
         * Get the Cloud Image Recognition instance and set this class
         * as the one to receive search responses.
         */
        mCloudIR = CraftARCloudRecognition.Instance();
        //mCloudIR.setRequestBoundingBoxes(true); //Optional, if you want to request bounding boxes
        //mCloudIR.setEmbedCustom(true); //Optional, if you want the custom data embedded in your CraftARItems
        mCraftARSDK.setSearchController(mCloudIR.getSearchController());
        /**
         * Set the Search controller from the Cloud IR class.
         * The Cloud IR class knows how to perform visual searches in the CraftAR CRS Service.
         * The searchController is a helper class that receives the camera frames and pictures from the
         * SDK and manages the Single shot and the Finder mode searches.
         */


        /**
         * Get the Tracking instance for the AR.
         */
        mCloudIR.setCollection("b9a268d8bbad4739", (ImageRecognition.SetCollectionListener)this);
    }



    @Override
    public void onPreviewStarted(int frameWidth, int frameHeight) {
        /**
         * Set the collection we want to search with the COLLECITON_TOKEN.
         * When the collection is ready, the collectionReady callback will be triggered.
         */
        mCloudIR.setCollection(Config.MY_COLLECTION_TOKEN, this);
    }

    @Override
    public void collectionReady() {
        /**
         * Start searching in finder mode. The searchResults() method of the
         * CraftARSearchResponseHandler previously set to the SDK will be triggered when some results
         * are found.
         */
       mySearchFunction();
    }

    @Override
    public void setCollectionFailed(CraftARError craftARError) {
        /**
         * This method is called when the setCollection method failed. This happens usually
         * when the token is wrong or there is no internet connection.
         */
        Log.d(TAG, "SetCollection failed " + craftARError.getErrorMessage());
    }


    @Override
    public void searchResults(ArrayList<CraftARResult> results, long searchTime, int requestCode) {
        //Call stopFinder() to stop sending queries
        if(results.size()==0){
            Log.d(TAG,"Nothing found");
        }else{
            mCraftARSDK.stopFinder();
            CraftARResult result = results.get(1); //We get only the first result
            CraftARItem item = result.getItem();
            Log.d(TAG,"Found item "+item.getItemName());
        }
    }

    @Override
    public void searchFailed(CraftARError craftARError, int requestCode) {
        /**
         * Called when a search fails. This happens usually when pointing the
         * device to a texture-less surface or when there are connectivity issues.
         */
        Log.d(TAG,"Search failed : "+craftARError.getErrorMessage());
    }

    @Override
    public void finish() {
        /**
         * Stop Tracking and clean the AR scene
         */
        mCraftARSDK.stopFinder();
        mTracking.stopTracking();
        mTracking.removeAllItems();
        super.finish();
    }

    void mySearchFunction() {
        //Trigger this function when clicking on a button, for example.
        mCraftARSDK.startFinder();
    }
	@Override
	public void onCameraOpenFailed() {
		Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_SHORT).show();				
	}
	
}
