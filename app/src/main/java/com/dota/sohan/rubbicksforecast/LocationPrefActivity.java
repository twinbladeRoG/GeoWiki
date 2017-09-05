package com.dota.sohan.rubbicksforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

/**
 * Created by Sohan on 8/9/2016.
 */
public class LocationPrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.location);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(LocationPrefActivity.this,MainActivity.class);
        startActivity(i);
    }
}
