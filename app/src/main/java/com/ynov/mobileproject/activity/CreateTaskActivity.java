package com.ynov.mobileproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ynov.mobileproject.R;
import com.ynov.mobileproject.models.todolist.ToDoTask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CreateTaskActivity extends AppCompatActivity {

    EditText titleEditor;
    CalendarView calendarView;
    Button buttonCreate;
    Spinner categorySpinner;
    DatabaseReference taskRef;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ToDoTask toDoTask;
    Date date;
    private final static String default_notification_channel_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskRef = database.child("todoList");
        toDoTask = (ToDoTask) getIntent().getSerializableExtra("toDo");

        titleEditor = findViewById(R.id.titleEditor);
        calendarView = findViewById(R.id.deadlineEditor);
        buttonCreate = findViewById(R.id.createButton);
        categorySpinner = findViewById(R.id.categorieSpinner);

        if (toDoTask != null) {
            date = toDoTask.date;
            titleEditor.setText(toDoTask.title);
            calendarView.setDate(toDoTask.date.getTime());
            categorySpinner.setSelection(((ArrayAdapter) categorySpinner.getAdapter()).getPosition(toDoTask.category));
            buttonCreate.setText(R.string.update);
        } else {
            date = new Date();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                date = new GregorianCalendar(year, month, day).getTime();
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toDoTask != null && toDoTask.uid != null) {
                    toDoTask.title = titleEditor.getText().toString();
                    toDoTask.date = date;
                    toDoTask.category = categorySpinner.getSelectedItem().toString();
                    Update(toDoTask);
                } else {
                    ToDoTask toDoTaskToCreate = new ToDoTask();
                    toDoTaskToCreate.title = titleEditor.getText().toString();
                    toDoTaskToCreate.date = date;
                    toDoTaskToCreate.category = categorySpinner.getSelectedItem().toString();

                    Create(toDoTaskToCreate);
                }

                finish();
            }
        });
    }

    private void scheduleNotification(Notification notification, Date deadline, ToDoTask toDoTask) {
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra("uid", toDoTask.uid);
        notificationIntent.putExtra("notification", notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        long futureInMillis = SystemClock. elapsedRealtime () + 2000 ;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, deadline.getTime() /*futureInMillis*/, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
        builder.setContentTitle("Deadline Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_assignment_black_24dp);
        builder.setChannelId(default_notification_channel_id);
        return builder.build();
    }

    private void Create(ToDoTask toDo) {
        String key = taskRef.push().getKey();

        scheduleNotification(getNotification( "Deadline de la tache : " + toDo.title ) , toDo.date, toDo ) ;

        toDo.uid = key;
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/todoList/" + key, toDo);

        database.updateChildren(childUpdates);
    }

    private void Update(ToDoTask toDo) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/todoList/" + toDo.uid, toDo);

        database.updateChildren(childUpdates);
    }

    private void Delete(String key) {
        database.child(key).removeValue();
    }
}
