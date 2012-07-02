package com.yash.mnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
				} finally {
					finish();
					startActivity(new Intent(getApplicationContext(),
							MNotes.class));
					stop();
				}
			}
		};
		splashTread.start();

	}
}
