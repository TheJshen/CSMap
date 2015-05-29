package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kcco.csmap.DAO.Messenger;

/**
 * Created by gaojieli on 5/24/15.
 */
public class RetrievePasswordActivity extends Activity {
    private EditText emailView;
    //private static final String TAG = "tag";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_activity);

        emailView = (EditText) findViewById(R.id.email);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(emailView)) {
                    com.example.kcco.csmap.DAO.Messenger.toast("Please enter an email address.", RetrievePasswordActivity.this);
                }
                else {
                    UserDAO.resetPassword(emailView.getText().toString(), RetrievePasswordActivity.this);
                }
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrievePasswordActivity.this.startActivity(new Intent(RetrievePasswordActivity.this, LoginActivity.class));
            }
        });
    }

    private boolean isEmpty (EditText edText) {
        return edText.getText().toString().trim().length() == 0;
    }

}



