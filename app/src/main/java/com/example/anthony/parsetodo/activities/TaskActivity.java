package com.example.anthony.parsetodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.models.Task;
import com.example.anthony.parsetodo.utils.LogHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    public final static String TASK_TITLE = "TaskActivityTitle";
    public final static String TASK_NUMBER = "TaskPosition";
    private int taskPos;
    private AppController mController;
    private Task mTask;

    private EditText mNotes;
    private EditText mDescription;
    private EditText mDueDate;
    private CheckBox mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mController = (AppController) getApplicationContext();

        Intent i = getIntent();

        setTitle(i.getStringExtra(TASK_TITLE));
        taskPos = i.getIntExtra(TASK_NUMBER, 0);

        mTask = mController.getTask(taskPos);

        mNotes = (EditText) findViewById(R.id.notes_edit_text);
        mDescription = (EditText) findViewById(R.id.description_text_view);
        mDueDate = (EditText) findViewById(R.id.due_date_edit_text);
        mDone = (CheckBox) findViewById(R.id.completed_check_box);

        updateData();
    }

    private void updateData() {
        try {
            mNotes.setText(mTask.getNotes());
            mDescription.setText(mTask.getDescription());
            mDone.setChecked(mTask.isCompleted());

            DateFormat formatter = new SimpleDateFormat();
            mDueDate.setText(formatter.format(mTask.getDueDate()));
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.logThreadId(e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTask.setNotes(mNotes.getText().toString());
        mTask.setDescription(mDescription.getText().toString());
        mTask.setCompleted(mDone.isChecked());

        SimpleDateFormat  format = new SimpleDateFormat();
        try {
            Date date = format.parse(mDueDate.getText().toString());
            mTask.setDueDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTask.saveEventually();
        mController.updateTask(mTask, taskPos);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(TASK_NUMBER,taskPos);
        setResult(RESULT_CANCELED,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
}
