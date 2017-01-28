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

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.craftar.CraftARActivity;
import com.craftar.CraftARCloudRecognition;
import com.craftar.CraftARContent;
import com.craftar.CraftARContentImage;
import com.craftar.CraftARError;
import com.craftar.CraftARItem;
import com.craftar.CraftARItemAR;
import com.craftar.CraftARResult;
import com.craftar.CraftARSDK;
import com.craftar.CraftARSDKException;
import com.craftar.CraftARSearchResponseHandler;
import com.craftar.CraftARTracking;
import com.craftar.ImageRecognition;

public class ARProgrammaticallyActivity extends CraftARActivity implements CraftARSearchResponseHandler, ImageRecognition.SetCollectionListener {

	private final String TAG = "ARProgrammaticallyActivity";

	private View mScanningLayout;

	CraftARSDK mCraftARSDK;
	CraftARTracking mTracking;
	CraftARCloudRecognition mCloudIR;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate() {

		View mainLayout = getLayoutInflater().inflate(R.layout.activity_ar_programmatically_ar_from_craftar, null);
		setContentView(mainLayout);

		mScanningLayout = findViewById(R.id.layout_scanning);

		/**
		 * Get the CraftAR SDK instance and initialize the capture
		 * passing the current activity
		 *
		 * When the capture is ready onPreviewStarted() will be called.
		 */
		mCraftARSDK =  CraftARSDK.Instance();
		mCraftARSDK.startCapture(this);


		/**
		 * Get the Cloud Image Recognition instance and set this class
		 * as the one to receive search responses.
		 */
		mCloudIR = CraftARCloudRecognition.Instance();
		mCloudIR.setCraftARSearchResponseHandler(this);

		/**
		 * Set the Search controller from the Cloud IR class.
		 * The Cloud IR class knows how to perform visual searches in the CraftAR CRS Service.
		 * The searchController is a helper class that receives the camera frames and pictures from the
		 * SDK and manages the Single shot and the Finder mode searches.
		 */
		mCraftARSDK.setSearchController(mCloudIR.getSearchController());

		/**
		 * Get the Tracking instance for the AR.
		 */
		mTracking = CraftARTracking.Instance();

	}

	@Override
	public void onPreviewStarted(int width, int height) {
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
		mCraftARSDK.startFinder();
	}

	@Override
	public void setCollectionFailed(CraftARError craftARError) {
		/**
		 * This method is called when the setCollection method failed. This happens usually
		 * when the token is wrong or there is no internet connection.
		 */
		//Log.d(TAG, "search failed! " + craftARError.getErrorMessage());
	}

	@Override
	public void searchResults(ArrayList<CraftARResult> results, long l, int i) {
		/**
		 * This is called when a search is finalized. Check if we have any results...
		 */
		if(results.size() != 0){
			/**
			 * Each result contains information about the match:
			 *  - score
			 *  - matched image
			 *  - match bounding box
			 *  - item
			 */
			CraftARResult result = results.get(0);


			/**
			 * Get the item for this result and check if it is an AR item
			 */
			CraftARItem item = result.getItem();
			if (item.isAR()) {
				// Stop Finding
				mCraftARSDK.stopFinder();

				// Cast the found item to an AR item
				CraftARItemAR myARItem = (CraftARItemAR)item;
				// Add content to the tracking SDK and start AR experience

				// We create a CraftARContentImage programmatically from a resource stored in the SD card.
				String imageUrl = getApplicationContext().getExternalFilesDir(null) + "/ar_programmatically_content.png";
				CraftARContentImage myImage = new CraftARContentImage(imageUrl);

				// This will make the content to be scaled to match the edges of the reference image. This doesn't keep the aspect ratio of the content.
				myImage.setWrapMode(CraftARContent.ContentWrapMode.WRAP_MODE_SCALE_FILL);

				// Add the content to the AR item
				myARItem.addContent(myImage);

				CraftARError error = mTracking.addItem(myARItem);
				if (error == null) {
					mTracking.startTracking();
					mScanningLayout.setVisibility(View.GONE);
				} else {
				//	Log.e(TAG, error.getErrorMessage());
				}

			}

		}
	}

	@Override
	public void searchFailed(CraftARError craftARError, int i) {
		/**
		 * Called when a search fails. This happens usually when pointing the
		 * device to a textureless surface or when there is connectivity issues.
		 */
		//Log.d(TAG, "Search failed :"+craftARError.getErrorMessage());
	}

	@Override
	public void finish() {
		/**
		 * Stop Searching, Tracking and clean the AR scene
		 */
        mCraftARSDK.stopFinder();
		mTracking.stopTracking();
		mTracking.removeAllItems();
		super.finish();
	}

	@Override
	public void onCameraOpenFailed() {
		Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_SHORT).show();		
	}
	

}
