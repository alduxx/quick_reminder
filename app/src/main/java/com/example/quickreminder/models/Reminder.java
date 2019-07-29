package com.example.quickreminder.models;

import org.joda.time.DateTime;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Reminder {
    private String label;
    private String schedulingInfo;
    private boolean done;

    public Reminder(){}

    public Reminder(String label) {
        this.label = label;
        this.schedulingInfo = null;
        this.done = false;
    }
    public Reminder(String label, String schedulingInfo) {
        this.label = label;
        this.schedulingInfo = schedulingInfo;
        this.done = false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSchedulingInfo() {
        return schedulingInfo;
    }

    public void setSchedulingInfo(String schedulingInfo) {
        this.schedulingInfo = schedulingInfo;
    }

    public void setDone(boolean done){
        this.done = done;
    }

    public boolean isDone(){
        return this.done;
    }

    public boolean hasSchedule(){
        return this.schedulingInfo != null;
    }

    public boolean pastDue(){
        try{
            DateTime dateTime = new DateTime(this.getSchedulingInfo());

            return dateTime.isAfterNow();
        } catch (Exception e){
            return false;
        }
    }

    public String getPrettyDate(){
        try{
            DateTime dateTime = new DateTime(this.getSchedulingInfo());
            DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d/M");
            String pretty = formatter.print(dateTime);

            formatter = DateTimeFormat.forPattern("HH:mm");
            if( (dateTime.toLocalDate()).equals(new LocalDate()) ){
                pretty = "Hoje, " + formatter.print(dateTime);
            } else if(LocalDate.now().plusDays(1).compareTo(new LocalDate(dateTime)) == 0) {
                pretty = "Amanhã, " + formatter.print(dateTime);
            } else if(LocalDate.now().minusDays(1).compareTo(new LocalDate(dateTime)) == 0) {
                pretty = "Ontem, " + formatter.print(dateTime);
            }

            return pretty;
        } catch (Exception e){
            return "#erro getPrettyDate";
        }
    }

}

