package com.example.tsurumi.auto_sleep;

import android.preference.PreferenceActivity;
import android.os.Bundle;
public class MyPreferences extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}