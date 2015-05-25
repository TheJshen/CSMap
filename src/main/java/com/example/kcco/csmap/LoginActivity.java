package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by jason on 5/16/15.
 */
public class LoginActivity extends Activity {
    private EditText usernameView;
    private EditText passwordView;
    public static final int MAIN = 1;
    public static final int RESET = 2;
    private static final String TAG = "tag";


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);

        findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valError = false;
                StringBuilder valErrorMessage = new StringBuilder("Please ");

                // Check username input
                if (LoginActivity.this.isEmpty(usernameView)) {
                    valError = true;
                    valErrorMessage.append("enter a username");
                }

                // Check password input
                if (LoginActivity.this.isEmpty(passwordView)) {
                    if (valError)
                        valErrorMessage.append(", and ");
                    valError = true;
                    valErrorMessage.append("enter a password");
                }
                valErrorMessage.append(".");

                // Show error message
                if (valError) {
                    Toast.makeText(LoginActivity.this, valErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();

                UserDAO user = new UserDAO(LoginActivity.this);
                user.logIn(username, password, LoginActivity.this);
            }
        });

        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RetrievePasswordActivity.class));
                //switchActivity(RESET);
                //Log.d(TAG,"check button");
            }
        });
    }

    // Method to check whether the EditText is empty or not.
    private boolean isEmpty (EditText edText) {
        return edText.getText().toString().trim().length() == 0;
    }

    public void switchActivity(int caseNumber) {
        if (caseNumber == MAIN) {
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(intent);
        }
        else if (caseNumber == RESET) {
            Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(intent);
        }
        else {
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(intent);
        }
    }

}
