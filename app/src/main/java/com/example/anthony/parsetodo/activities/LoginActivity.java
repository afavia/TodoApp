package com.example.anthony.parsetodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.events.LoginEvent;
import com.example.anthony.parsetodo.events.LoginResultEvent;
import com.google.android.gms.appindexing.AppIndexApi;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.otto.Subscribe;

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

        AppController.bus.register(this);
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
        AppController.bus.post(new LoginEvent(mUserName.getText().toString(), mPassword.getText().toString()));
    }

    @Subscribe
    public void onLoginResult(LoginResultEvent result) {
        if (result.isSuccessful) {
            Intent i = new Intent(LoginActivity.this, TodoActivity.class);
            startActivity(i);
            finish();
        }
        else {
            mErrorField.setText(result.msg);
        }
    }

    public void showRegistration(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }
}

