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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class RecognitionOnlyActivity extends CraftARActivity implements CraftARSearchResponseHandler, ImageRecognition.SetCollectionListener, View.OnClickListener {

	private final String TAG = "RecognitionOnlyActivity";

	private View mScanningLayout;
	private View mTapToScanLayout;


	CraftARTracking mTracking;
	CraftARSDK mCraftARSDK;
	CraftARCloudRecognition mCloudIR;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate() {
		View mainLayout = getLayoutInflater().inflate(R.layout.activity_recognition_only, null);
		setContentView(mainLayout);

		mScanningLayout = findViewById(R.id.layout_scanning);
		mTapToScanLayout = findViewById(R.id.tap_to_scan);
		mTapToScanLayout.setOnClickListener(this);

		mTracking = CraftARTracking.Instance();

		mCraftARSDK = CraftARSDK.Instance();
		mCraftARSDK.startCapture(this);
	}


	@Override
	public void onPreviewStarted(int frameWidth, int frameHeight) {
		mCloudIR = CraftARCloudRecognition.Instance();
		mCloudIR.setCraftARSearchResponseHandler(this);
		mCloudIR.setCollection(Config.MY_COLLECTION_TOKEN, this);
		mCraftARSDK.setSearchController(mCloudIR.getSearchController());
	}


	@Override
	public void collectionReady() {
		mTapToScanLayout.setClickable(true);

	}

	@Override
	public void setCollectionFailed(CraftARError craftARError) {
		Log.d(TAG, "search failed! " + craftARError.getErrorMessage());
	}

	@Override
	public void searchResults(ArrayList<CraftARResult> results, long searchTime, int requestCode) {
		mCraftARSDK.getCamera().restartCapture();
		mScanningLayout.setVisibility(View.GONE);
		mTapToScanLayout.setVisibility(View.VISIBLE);

		if(results.size()==0){
			Log.d(TAG,"Nothing found");
			Toast.makeText(getBaseContext(),getString(R.string.recognition_only_toast_nothing_found), Toast.LENGTH_SHORT).show();
		}else{
			CraftARResult result = results.get(0);
			CraftARItem item = result.getItem();
			if (!item.isAR()) {
				String url = item.getUrl();
				if((url!= null)&&(! url.isEmpty())){
					System.out.println("url"+url);
					Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(launchBrowser);
				}
			}
			Log.d(TAG,"Found item "+item.getItemName());
		}
		
	}

	@Override
	public void searchFailed(CraftARError craftARError, int requestCode) {
		Log.d(TAG,"search failed!");
		Toast.makeText(getBaseContext(), getString(R.string.recognition_only_toast_nothing_found), Toast.LENGTH_SHORT).show();
		mScanningLayout.setVisibility(View.GONE);
		mTapToScanLayout.setVisibility(View.VISIBLE);
		mCraftARSDK.getCamera().restartCapture();
	}

	@Override
	public void onClick(View view) {
		if (view == mTapToScanLayout) {
			mTapToScanLayout.setVisibility(View.GONE);
			mScanningLayout.setVisibility(View.VISIBLE);
			mCraftARSDK.singleShotSearch();
		}
	}

	@Override
	public void onCameraOpenFailed() {
		Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_SHORT).show();
	}
}
