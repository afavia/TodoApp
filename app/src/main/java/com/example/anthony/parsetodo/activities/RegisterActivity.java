package com.example.anthony.parsetodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.events.RegisterEvent;
import com.example.anthony.parsetodo.events.RegisterResultEvent;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.otto.Subscribe;

public class RegisterActivity extends AppCompatActivity {

    EditText mUserName;
    EditText mPassword;
    TextView mErrorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = (EditText) findViewById(R.id.register_username);
        mPassword = (EditText) findViewById(R.id.register_password);
        mErrorField = (TextView) findViewById(R.id.error_messages);

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

    public void register(final View v) {
        if (mUserName.getText().length() == 0 || mPassword.getText().length() == 0) {
            return;
        }
        mErrorField.setText("");

        AppController.bus.post(new RegisterEvent(mUserName.getText().toString(), mPassword.getText().toString()));
    }

    @Subscribe
    public void onRegisterEventResult(RegisterResultEvent resultEvent) {
        if (resultEvent.isSuccessful) {
            Intent i = new Intent(RegisterActivity.this, TodoActivity.class);
            startActivity(i);
            finish();
        } else {
            mErrorField.setText(resultEvent.msg);
            Button b = (Button) findViewById(R.id.signInButton);
            b.setEnabled(true);
        }
    }

    public void showLogin(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
