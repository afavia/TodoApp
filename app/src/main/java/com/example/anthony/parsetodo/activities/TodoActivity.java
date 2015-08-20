package com.example.anthony.parsetodo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.adapters.TaskAdapter;
import com.example.anthony.parsetodo.models.Task;
import com.example.anthony.parsetodo.utils.LogHelper;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TodoActivity extends AppCompatActivity{

    private static final int TASK_ACTIVITY_REQUEST = 1;
    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;

    private AppController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        mController = (AppController) getApplicationContext();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LogHelper.configure(getResources().getString(R.string.app_name));

        Intent intent = getIntent();
        if (intent.getAction() != null ){
            if (intent.getAction().equals("android.intent.action.MAIN")) {
                initParse();
            }
        }

        ParseUser current = ParseUser.getCurrentUser();
        if (current == null) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        setupUI();

        updateData();
    }

    private void setupUI() {
        mTaskInput = (EditText) findViewById(R.id.task_input);
        mTaskInput.clearFocus();
        mListView = (ListView) findViewById(R.id.task_list);
        mAdapter = new TaskAdapter(this, mController.getTasks());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task t = mAdapter.getItem(position);
//                TextView taskDescription = (TextView) view.findViewById(R.id.task_description);
//                t.setCompleted(!t.isCompleted());
//
//                if (t.isCompleted()) {
//                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                } else {
//                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                }
//                t.saveEventually();

                Intent i = new Intent(TodoActivity.this, TaskActivity.class);
                i.putExtra(TaskActivity.TASK_TITLE, t.getDescription());
                i.putExtra(TaskActivity.TASK_NUMBER, position);
                startActivityForResult(i, TASK_ACTIVITY_REQUEST);
            }
        });

        Button addTaskBtn = (Button) findViewById(R.id.submit_button);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TASK_ACTIVITY_REQUEST) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void initParse() {
        //        ParseCrashReporting.enable(this);
        Parse.initialize(this, getString(R.string.app_id), getString(R.string.client_id));
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseObject.registerSubclass(Task.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void updateData() {
        mAdapter.clear();
        mAdapter.addAll(mController.getTasks());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                List<Task> matches = new LinkedList<Task>();
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (mAdapter.getItem(i).getDescription().contains(query)) {
                        matches.add(mAdapter.getItem(i));
                    }
                }

                if (matches.size() > 0) {
                    mAdapter.clear();
                    mAdapter.addAll(matches);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    updateData();
                }
                List<Task> matches = new LinkedList<>();
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (mAdapter.getItem(i).getDescription().contains(newText)) {
                        matches.add(mAdapter.getItem(i));
                    }
                }

                if (matches.size() > 0) {
                    mAdapter.clear();
                    mAdapter.addAll(matches);
                }

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateData();
                return false;
            }
        });

        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void onClickSettings(MenuItem item) {
        LogHelper.logThreadId("Settings option pressed.");
    }

    public void createTask(View v) {
        try {
            new AsyncTask<String, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(String... params) {
                    String task = params[0];
                    if (task.length() > 0) {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                        mController.addTask(task, false, cal.getTime());
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    if (result) {
                        mTaskInput.setText("");
                        mTaskInput.clearFocus();
                        updateData();
                    }
                }
            }.execute(mTaskInput.getText().toString());
        } catch (Exception e) {
            Log.d("TODO APP", e.getLocalizedMessage());
        }
    }
}
