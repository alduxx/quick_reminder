package com.example.quickreminder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.quickreminder.custom.ReminderListViewAdapter;
import com.example.quickreminder.models.Reminder;
import com.example.quickreminder.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ReminderListViewAdapter customAdapter;

    List<Reminder> reminderList = new ArrayList<Reminder>();


    final EditText.OnKeyListener newReminderListener = new EditText.OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
            if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                EditText editTextNewReminder = findViewById(R.id.textNewReminder);
                Reminder r = Util.processReminder(editTextNewReminder.getText().toString(), getApplicationContext());
                reminderList.add(0, r); //adds at the top of queue

                updateReminderList(true);

                editTextNewReminder.setText("");
                editTextNewReminder.clearFocus();

                // hides keyboard
                hideKeyboard();

                return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        hideActionBarTitle();

        EditText textNewReminder = findViewById(R.id.textNewReminder);
        textNewReminder.setOnKeyListener(newReminderListener);

        updateReminderList();
    }


    public void updateReminderList(){
        updateReminderList(false);
    }

    public void updateReminderList(boolean dataChanged){
        if(dataChanged){
            customAdapter.notifyDataSetChanged();
        }

        /* DEBUG: Limpa Shared Preferences */
        // getApplicationContext().getSharedPreferences("ReminderPrefs", 0).edit().clear().commit();

        reminderList = Util.getSavedReminders(getApplicationContext());

        TextView textNumberOfReminders =  findViewById(R.id.textNumberOfReminders);
        switch (reminderList.size()) {
            case 0:
                textNumberOfReminders.setText(getResources().getString(R.string.no_reminders_found));
                break;
            case 1:
                textNumberOfReminders.setText("1 " + getResources().getString(R.string.reminder_singular));
                break;
            default:
                textNumberOfReminders.setText(reminderList.size() + " " + getResources().getString(R.string.reminder_plural));
        }

        ListView reminderList = (ListView)findViewById(R.id.listViewReminders);
        customAdapter = new ReminderListViewAdapter(getApplicationContext(), this.reminderList);
        reminderList.setAdapter(customAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hideActionBarTitle() {
        try{
            getActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe){}

        try{
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe){}
    }


    void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(
                                                                      Context.INPUT_METHOD_SERVICE);
        View focusedView = MainActivity.this.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
