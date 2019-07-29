package com.example.quickreminder.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quickreminder.R;
import com.example.quickreminder.models.Reminder;

import java.util.List;

public class ReminderListViewAdapter  extends BaseAdapter {
    private List<Reminder> reminderList;
    private LayoutInflater inflater;

    public ReminderListViewAdapter(Context applicationContext, List<Reminder> reminderList) {
        this.reminderList = reminderList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Reminder reminder = reminderList.get(i);
        view = inflater.inflate(R.layout.list_view_main, null);
        TextView item = view.findViewById(R.id.mainListViewLayoutReminderText);
        TextView subitem = view.findViewById(R.id.mainListViewLayoutReminderDateTime);

        item.setText(reminder.getLabel());
        subitem.setText(reminder.getPrettyDate());

        if(reminder.pastDue()){
            subitem.setTextColor(Color.parseColor("#FF0000"));
        }

        return view;
    }


}
