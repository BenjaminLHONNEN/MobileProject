package com.ynov.mobileproject.ui.map;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.ynov.mobileproject.R;
import com.ynov.mobileproject.models.velov.Fields;
import com.ynov.mobileproject.models.velov.Record;
import com.ynov.mobileproject.models.velov.Velov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoogleMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMap extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    com.google.android.gms.maps.GoogleMap mMap;

    RequestQueue queue;

    public GoogleMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoogleMap.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMap newInstance(String param1, String param2) {
        GoogleMap fragment = new GoogleMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    MapView mMapView;
    private GoogleMap googleMap;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());
        rootView = inflater.inflate(R.layout.fragment_google_map, container, false);
        // Construct a GeoDataClient.
// Initialize Places.
        Places.initialize(rootView.getContext(), "AIzaSyB00x4CkkS32qMytRpDiGvey2EMivgUr9A");

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(rootView.getContext());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                mMap = googleMap;

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                LatLng ynov = new LatLng(45.745678, 4.837594);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ynov, 15));
            }
        });

        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            final LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                                    mMap = googleMap;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                                    getHttp(current);
                                }
                            });
                        }
                    }
                }
        );

        return rootView;
    }

    public void getHttp(LatLng latLng) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://public.opendatasoft.com/api/records/1.0/search/?dataset=station-velov-grand-lyon&facet=name&facet=commune&facet=bonus&facet=status&facet=available&facet=availabl_1&facet=availabili&facet=availabi_1&facet=last_upd_1&geofilter.distance=" + latLng.latitude + "%2C+" + latLng.longitude + "%2C1000",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Gson gson = new Gson();
                            Velov velovs = gson.fromJson(response, Velov.class);

                            final List<Record> records = velovs.getRecords();
                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                                    for (int y = 0; y < records.size(); y++) {
                                        Fields fields = records.get(y).getFields();
                                        List<Double> coord = records.get(y).getGeometry().getCoordinates();
                                        LatLng pos = new LatLng(coord.get(1), coord.get(0));
                                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.velo_50);
                                        Marker marquer = googleMap.addMarker(new MarkerOptions()
                                                .position(pos)
                                                .title(fields.getName())
                                                .snippet(fields.getAvailable() + " / " + fields.getBikeStand())
                                                .icon(bitmap));
                                    }
                                }
                            });
                            //list.addAll(characterApiRequestResult.results);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("REQUEST_ERROR", "erreur lors de la requete", error);
                    Toast.makeText(getContext(),
                            "That didn't work!", Toast.LENGTH_LONG)
                            .show();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                });
            } else {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(false);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(rootView.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Executor executor = new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        command.run();
                    }
                };
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(executor, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();

                            final LatLng test = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 15));
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                                }
                            });
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            // "45.745678", "4.837594"
                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                                    LatLng ynov = new LatLng(45.745678, 4.837594);
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ynov, 15));
                                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                                }
                            });
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
