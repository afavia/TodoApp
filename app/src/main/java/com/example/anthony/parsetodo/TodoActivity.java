package com.example.anthony.parsetodo;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends Activity {

    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        ParseCrashReporting.enable(this);
        Parse.initialize(this, getString(R.string.app_id), getString(R.string.client_id));
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseObject.registerSubclass(Task.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        mAdapter = new TaskAdapter(this, new ArrayList<Task>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task t = mAdapter.getItem(position);
                TextView taskDescription = (TextView) view.findViewById(R.id.task_description);
                t.setCompleted(!t.isCompleted());

                if (t.isCompleted()) {
                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                t.saveEventually();
            }
        });

        updateData();
    }

    public void updateData() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (tasks != null) {
                    mAdapter.clear();
                    mAdapter.addAll(tasks);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
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

    public void createTask(View v) {
        if (mTaskInput.getText().length() > 0) {
            Task t = new Task();
            t.setCompleted(false);
            t.setDescription(mTaskInput.getText().toString());
            t.saveEventually();
            mTaskInput.setText("");
            mAdapter.insert(t, 0);
        }
    }
}
