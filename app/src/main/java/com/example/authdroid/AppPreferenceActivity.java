package com.example.authdroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceActivity  extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(RR.xml.preferences);
    }


}
