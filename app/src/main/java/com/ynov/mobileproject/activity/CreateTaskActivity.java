package com.ynov.mobileproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private void Create(ToDoTask toDo) {
        String key = taskRef.push().getKey();
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
