package com.example.anthony.parsetodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anthony.parsetodo.AppController;
import com.example.anthony.parsetodo.R;
import com.example.anthony.parsetodo.models.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by Anthony on 8/13/2015.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private List<Task> mTasks;

    public TaskAdapter(Context context, List<Task> objects) {
        super(context, R.layout.task_row_item, objects);
        mContext = context;
        mTasks = objects;
        AppController.bus.register(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.task_row_item, null);
        }

        Task t = mTasks.get(position);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.task_description);
        descriptionView.setText(t.getDescription());

        if (t.isCompleted()) {
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (t.getDueDate() != null) {
            if (t.getDueDate().before(new Date())) {
                descriptionView.setBackgroundColor(Color.argb(128, 255, 0, 0));
            }
            else
                descriptionView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
        else
            descriptionView.setBackgroundColor(Color.argb(0, 0, 0, 0));

        return convertView;
    }
}
