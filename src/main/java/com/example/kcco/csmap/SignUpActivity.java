package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by gaojieli on 5/14/15.
 */
public class SignUpActivity extends Activity {
    private EditText usernameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private static final String TAG = "tag";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // set up sign up form
        usernameView = (EditText) findViewById(R.id.username);
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);

        //set up the submit button click handler
        findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the sign up data
                boolean valError = false;
                StringBuilder valErrorMessage = new StringBuilder("Please ");

                if (SignUpActivity.this.isEmpty(usernameView) || SignUpActivity.this.isEmpty(emailView) ||
                        SignUpActivity.this.isEmpty(passwordView) || SignUpActivity.this.isEmpty(passwordAgainView)) {
                    valError = true;
                    valErrorMessage.append("fill in all of the fields.");
                } else if (!SignUpActivity.this.isMatching(passwordView, passwordAgainView)) {
                    valError = true;
                    valErrorMessage.append("enter the same password twice.");
                } else {

                    String email = emailView.getText().toString();

                    for (int i = 0; i < email.length(); i++) {
                        if (!(email.substring(i, i + 1).equals("@") ||
                                email.substring(email.length() - 4, email.length()).equals(".com"))) {
                            valError = true;
                            valErrorMessage.append("enter a valid email address.");
                            break;
                        }

                    }
                }

                if (valError) {
                    Toast.makeText(SignUpActivity.this, valErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                String username = usernameView.getText().toString();
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();

                // signup with Ka's UserDAO
                UserDAO user = new UserDAO(SignUpActivity.this);
                user.createUser(username, email, password);
                user.userSignUp();


                /* original signup with our database
                // Initialize a new parse user in the database
                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());

                // User the default signUp method to sign the user up
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            SignUpActivity.this.startActivity(intent);
                        }
                    }
                });
                */
            }
        });
    }

    // Method to switch activity
    public void switchActivity(int caseNumber) {
        if (caseNumber == 1) {
            Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SignUpActivity.this.startActivity(intent);
        }
        else {
            Intent intent = new Intent(SignUpActivity.this, SignUporLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SignUpActivity.this.startActivity(intent);
        }
    }

    // Method to check whether the EditText is empty or not.
    private boolean isEmpty (EditText edText) {
        return edText.getText().toString().trim().length() == 0;
    }

    // Method to check two EditText field have the same content
    private boolean isMatching (EditText et1, EditText et2) {
        return et1.getText().toString().equals(et2.getText().toString());
    }
}

