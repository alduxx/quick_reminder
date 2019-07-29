package com.example.quickreminder.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.joda.time.DateTime;

import com.example.quickreminder.models.Reminder;

import static android.content.Context.MODE_PRIVATE;


public final class Util {

    public static void log(String label, String value) {
        System.out.println("qrlog | " + label + " -> " + value);
    }

    public static void log(String value) {
        System.out.println(value);
    }

    public static Reminder parseReminder(String reminderAsText){
        Reminder reminder = new Reminder();

        int index = reminderAsText.lastIndexOf(".");

        char lastChar = reminderAsText.charAt(reminderAsText.length() - 1);

        if(index <= 0 || lastChar == '.'){
            reminder.setLabel(reminderAsText);
            reminder.setSchedulingInfo(null);
        } else {
            reminder.setLabel(reminderAsText.substring(0, index));
            reminder.setSchedulingInfo(reminderAsText.substring(index+1));
        }

        return reminder;
    }


    public static List getReminderParams(Reminder reminder){
        List<String> params = new ArrayList<String>();

        if(reminder.getSchedulingInfo() != null){
            String[] items = reminder.getSchedulingInfo().trim().split(" ");
            for (String item : items) {
                params.add(item);
            }
        }

        return params;
    }

    public static DateTime setNewTimeToDateTime(int hours, DateTime scheduleTime){
        int currentHour = new DateTime().getHourOfDay();

        if(currentHour > hours){
            hours = hours + 24; //if hour is in the past, set to next day
        }
        scheduleTime = scheduleTime.withHourOfDay(0) // add hours from 0
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .plusHours(hours);

        return scheduleTime;
    }

    public static DateTime setNewTimeToDateTime(int hours, int minutes, DateTime scheduleTime){
        scheduleTime = setNewTimeToDateTime(hours, scheduleTime);
        return scheduleTime.plusMinutes(minutes);
    }

    public static DateTime addMinutesToCurrentDateTime(int minutes, DateTime scheduleTime){
        return scheduleTime.plusMinutes(minutes);
    }

    private static String scheduleReminder(String label, Context context) {
        return scheduleReminder(label, null, context);
    }


    public static String scheduleReminder(String label, String timeAsText, Context context) {
        DateTime scheduleTime = new DateTime();

        if (timeAsText != null) {
            int unitOfTime = Integer.parseInt(timeAsText);
            int hour, minutes;

            if (unitOfTime < 0) return null;

            switch (String.valueOf(unitOfTime).length()) {
                case 1:
                    scheduleTime = setNewTimeToDateTime(unitOfTime, scheduleTime);
                    break;
                case 2:
                    if (unitOfTime <= 23) {
                        scheduleTime = setNewTimeToDateTime(unitOfTime, scheduleTime);
                    } else {
                        scheduleTime = addMinutesToCurrentDateTime(unitOfTime, scheduleTime);
                    }
                    break;
                case 3:
                    hour = Integer.parseInt(unitOfTime / 100 + ""); // only the hour digit
                    minutes = unitOfTime - (hour * 100); // only the minutes digits

                    if (hour >= 0 && hour <= 23 && minutes >= 0 && minutes <= 59) {
                        scheduleTime = setNewTimeToDateTime(hour, minutes, scheduleTime);
                    }
                    break;
                case 4:
                    hour = Integer.parseInt(unitOfTime / 100 + ""); // only the hours digit
                    minutes = unitOfTime - (hour * 100); // only the minutes digits
                    if (hour >= 0 && hour <= 23 && minutes >= 0 && minutes <= 59) {
                        scheduleTime = setNewTimeToDateTime(hour, minutes, scheduleTime);
                    }
                    break;
                default:
                    break;
            }
        }

        log("\nscheduleTime", scheduleTime.toString());

        SharedPreferences pref = context.getSharedPreferences("ReminderPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(scheduleTime.toString(), label);
        editor.commit();

        return scheduleTime.toString();
    }

    public static List<Reminder> getSavedReminders(Context context) {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        SharedPreferences prefs = context.getSharedPreferences("ReminderPrefs", MODE_PRIVATE);

        Map<String,?> keys = prefs.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            reminderList.add(new Reminder(entry.getValue().toString(), entry.getKey()));
        }

        return reminderList;
    }

    public static Reminder processReminder(String reminderAsText, Context context){
        Reminder reminder = parseReminder(reminderAsText);

        List<String> params = getReminderParams(reminder);

        log("label", reminder.getLabel());
        log("schedulingInfo", reminder.getSchedulingInfo());


        switch(params.size()){
            case 0: // no scheduling info supplied
                reminder.setSchedulingInfo( scheduleReminder(reminder.getLabel(), context) );
                break;
            case 1: // time only
                reminder.setSchedulingInfo( scheduleReminder(reminder.getLabel(), params.get(0), context) );
                break;
            case 2: // time and date
                break;
            default:
                break;
        }

        return reminder;
    }



}

