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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.craftar.CLog;
import com.craftar.CraftARError;
import com.craftar.CraftAROnDeviceCollection;
import com.craftar.CraftAROnDeviceCollectionManager;
import com.craftar.CraftAROnDeviceCollectionManager.AddCollectionListener;
import com.craftar.CraftARSDK;

public class SplashScreenActivity extends Activity implements AddCollectionListener{

	private final static String TAG = "SplashScreenActivity";
	private static final long SPLASH_SCREEN_DELAY = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		CraftARSDK.Instance().init(getApplicationContext());
		
		/** We will load the collection for On Device AR ( 4th example). 
		 * If you use Cloud AR, you don't need to add the collection*/

		CraftAROnDeviceCollectionManager collectionManager = CraftAROnDeviceCollectionManager.Instance();

        /**
         * The on-device collection may already be added to the device (we just add it once)
         * we can use the token to retrieve it.
         */
		CraftAROnDeviceCollection collection = collectionManager.get(Config.MY_COLLECTION_TOKEN);
		if(collection != null){
			Log.d(this.getClass().getSimpleName(), "Collection already added, starting launchers activity!");
			startLaunchersActivity();

		}else{
			/**
           * If not, we get the path for the bundle and add the collection to the device first.
           * The addCollection  method receives an AddCollectionListener instance that will receive
           * the callbacks when the collection is ready.
           */
			Log.d(this.getClass().getSimpleName(), "Collection NOT added, adding collection");
			collectionManager.addCollection("arbundle.zip", this);
			
			//Alternatively, you can also download the collection from CraftAR using the token, instead of embedding it into the app resources.
			//collectionManager.addCollectionWithToken(TOKEN, this); 
		}
	}

	@Override
	public void collectionAdded(CraftAROnDeviceCollection collection) {
		
		/**
         * The collection is on the device and ready to use!
         */
		startLaunchersActivity();
	}

	@Override
	public void addCollectionFailed(CraftARError error) {
		Toast.makeText(getApplicationContext(), "AddCollection failed: "+error.getErrorMessage(), Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void addCollectionProgress(float progress) {
		Log.d(TAG, "addCollectionProgress "+progress);

	}
	
	private void startLaunchersActivity(){
		TimerTask task = new TimerTask() {
			public void run() {
				Intent launchersActivity = new Intent( SplashScreenActivity.this, ProducerConsumerActivity.class);
				startActivity(launchersActivity);
				finish();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}

}
