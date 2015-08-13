package com.example.anthony.parsetodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anthony.parsetodo.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    EditText mUserName;
    EditText mPassword;
    TextView mErrorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (EditText) findViewById(R.id.sign_in_username);
        mPassword = (EditText) findViewById(R.id.sign_in_password);
        mErrorField = (TextView) findViewById(R.id.error_messages_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(final View v) {
        v.setEnabled(false);

        ParseUser.logInInBackground(mUserName.getText().toString(),
                mPassword.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Intent i = new Intent(LoginActivity.this, TodoActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            switch (e.getCode()) {
                                case ParseException.USERNAME_TAKEN:
                                    mErrorField.setText("Sorry this username is already taken");
                                    break;
                                case ParseException.USERNAME_MISSING:
                                    mErrorField.setText("Sorry you must supply a username to register");
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    mErrorField.setText("Sorry you must supply a password to register");
                                    break;
                                case ParseException.OBJECT_NOT_FOUND:
                                    mErrorField.setText("Sorry, those credentials were invalid.");
                                    break;
                                default:
                                    mErrorField.setText(e.getLocalizedMessage());
                                    break;
                            }
                            v.setEnabled(true);
                        }
                    }
                });
    }

    public void showRegistration(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }
}

