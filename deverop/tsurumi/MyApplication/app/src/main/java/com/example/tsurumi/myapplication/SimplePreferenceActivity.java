package com.example.tsurumi.myapplication;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SimplePreferenceActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SimplePreferenceFragment()).commit();
    }
}