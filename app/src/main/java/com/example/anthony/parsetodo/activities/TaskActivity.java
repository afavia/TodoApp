package com.example.anthony.parsetodo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.models.Task;
import com.example.anthony.parsetodo.utils.LogHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mController = (AppController) getApplicationContext();

        AppController.bus.register(this);

        Intent i = getIntent();

        setTitle(i.getStringExtra(TASK_TITLE));
        taskPos = i.getIntExtra(TASK_NUMBER, 0);

        mTask = mController.getTask(taskPos);

        setupUI();

        updateData();
    }

    private void setupUI() {
        mNotes = (EditText) findViewById(R.id.notes_edit_text);
        mDescription = (EditText) findViewById(R.id.description_text_view);
        mDueDate = (EditText) findViewById(R.id.due_date_edit_text);
        mDone = (CheckBox) findViewById(R.id.completed_check_box);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat();
                date.set(year, monthOfYear, dayOfMonth);
                mDueDate.setText(formatter.format(date.getTime()));
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        Button doneBtn = (Button) findViewById(R.id.done_button);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClick();
            }
        });

        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDueDateClick();
            }
        });

        mDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onDueDateClick();
            }
        });
    }

    private void onDueDateClick() {
        datePickerDialog.show();
        mDueDate.clearFocus();
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
        setTaskData();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(TASK_NUMBER,taskPos);
        setResult(RESULT_CANCELED,returnIntent);
        finish();
    }

    private void setTaskData() {
        mTask.setNotes(mNotes.getText().toString());
        mTask.setDescription(mDescription.getText().toString());
        mTask.setCompleted(mDone.isChecked());

        SimpleDateFormat format = new SimpleDateFormat();
        try {
            Date date = format.parse(mDueDate.getText().toString());
            mTask.setDueDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTask.saveEventually();
        mController.updateTask(mTask, taskPos);
    }

    private void onDoneButtonClick() {
        setTaskData();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TASK_NUMBER,taskPos);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
