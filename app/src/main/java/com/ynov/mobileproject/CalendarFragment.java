package com.ynov.mobileproject;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ynov.mobileproject.models.agenda.AgendaModel;
import com.ynov.mobileproject.models.todolist.ToDoTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment {

    private CalendarViewModel mViewModel;
    FloatingActionButton floatingActionButton;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseListAdapter<AgendaModel> agendaModelFirebaseListAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference agendaRef = database.child("agenda");
    AgendaModel agendaModel;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.calendar_fragment, container, false);
        floatingActionButton = view.findViewById(R.id.fab);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        FirebaseListOptions<AgendaModel> options = new FirebaseListOptions.Builder<AgendaModel>()
                .setQuery(agendaRef, AgendaModel.class)
                .setLayout(R.layout.agenda_list_layout)
                .build();
        agendaModelFirebaseListAdapter = new FirebaseListAdapter<AgendaModel>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull AgendaModel model, int position) {
                ImageView imageView = v.findViewById(R.id.image_list_layout);
                TextView locationTextView = v.findViewById(R.id.location_list_layout);
                TextView dateTextView = v.findViewById(R.id.date_list_layout);

                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

                Glide.with(v).load(model.Image).into(imageView);
                locationTextView.setText(model.Location);
                dateTextView.setText(dateFormat.format(model.date));
            }
        };

        agendaModelFirebaseListAdapter.startListening();
        final ListView lv = (ListView) view.findViewById(R.id.agenda_list);
        lv.setAdapter(agendaModelFirebaseListAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        // TODO: Use the ViewModel
    }

    private void dispatchTakePictureIntent() {
        agendaModel = new AgendaModel();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        agendaModel.Image = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                agendaModel.Latitude = location.getLatitude();
                                agendaModel.Longitude = location.getLongitude();
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                ArrayList<Address> addresses = null;
                                try {
                                    addresses = (ArrayList<Address>) geocoder.getFromLocation(agendaModel.Latitude, agendaModel.Longitude, 1);
                                    String cityName = addresses.get(0).getAddressLine(0);
                                    agendaModel.Location = cityName;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Create(agendaModel);
                            }
                        }
                    }
            );
        }
    }

    private void Create(AgendaModel agendaModel) {
        String key = agendaRef.push().getKey();
        agendaModel.date = new Date();
        agendaModel.uid = key;
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/agenda/" + key, agendaModel);

        database.updateChildren(childUpdates);
    }

    private void Update(AgendaModel agendaModel) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/agenda/" + agendaModel.uid, agendaModel);

        database.updateChildren(childUpdates);
    }
}
