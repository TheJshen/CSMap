package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by gaojieli on 5/14/15.
 */
public class SignUporLogin extends Activity {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_or_login);

        // if click login
        (findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUporLogin.this.startActivity(new Intent(SignUporLogin.this, LoginActivity.class));
            }
        });

        // if click signup
        (findViewById(R.id.signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUporLogin.this.startActivity(new Intent(SignUporLogin.this, SignUpActivity.class));
            }
        });
    }
}

