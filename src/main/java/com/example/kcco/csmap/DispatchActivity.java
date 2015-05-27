package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by gaojieli on 5/14/15.
 */
public class DispatchActivity extends Activity {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if already logged in
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, MapMainActivity.class));
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
