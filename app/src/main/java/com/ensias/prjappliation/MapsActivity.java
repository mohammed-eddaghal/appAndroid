package com.ensias.prjappliation;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String userPath="users";

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    //instance of user classe
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference(userPath);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Double lang = getIntent().getDoubleExtra("langitud",1);
        Double lat = getIntent().getDoubleExtra("latitude",1);
        Log.d("testLocation",lang+" / "+lat);
/*
        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
            User user1=dataSnapshot.getValue(User.class);
            if(user1.getName().equals("med")){
                Toast.makeText(MainActivity.this, "user already exist", Toast.LENGTH_SHORT).show();
                return;
            }
        }*/
        // Add a marker in Sydney and move the camera
        LatLng mark = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().position(mark).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 16));

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}