package com.ynov.mobileproject.ui.totolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ynov.mobileproject.R;
import com.ynov.mobileproject.activity.CreateTaskActivity;
import com.ynov.mobileproject.models.todolist.ToDoTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FloatingActionButton floatingActionButton;
    FirebaseListAdapter<ToDoTask> toDoTaskFirebaseListAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("todoList");

    public ToDoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoListFragment newInstance(String param1, String param2) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list2, container, false);

        FirebaseListOptions<ToDoTask> options = new FirebaseListOptions.Builder<ToDoTask>()
                .setQuery(database, ToDoTask.class)
                .setLayout(R.layout.to_do_list_layout)
                .build();

        toDoTaskFirebaseListAdapter = new FirebaseListAdapter<ToDoTask>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ToDoTask model, int position) {
                TextView titleTextView = v.findViewById(R.id.title_list_layout);
                TextView categoryTextView = v.findViewById(R.id.category_list_layout2);
                TextView dateTextView = v.findViewById(R.id.date_list_layout);

                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

                titleTextView.setText(model.title);
                categoryTextView.setText(model.category);
                dateTextView.setText(dateFormat.format(model.date));
            }
        };

        toDoTaskFirebaseListAdapter.startListening();
        final ListView lv = (ListView) view.findViewById(R.id.to_do_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoTask todo = (ToDoTask) lv.getItemAtPosition(position);
                OpenUpdateTask(todo);
            }
        });
        lv.setAdapter(toDoTaskFirebaseListAdapter);

        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCreateTask();
            }
        });

        return view;
    }

    private void OpenCreateTask() {
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        startActivity(intent);
    }
    private void OpenUpdateTask(ToDoTask todo) {
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra("toDo", todo);
        startActivity(intent);
    }
}
