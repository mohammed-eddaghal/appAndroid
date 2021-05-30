package com.ensias.prjappliation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView lang,lat;
    FusedLocationProviderClient fusedLocationProviderClient;
    String key, userPath="users";

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
        setContentView(R.layout.activity_main);

        lang = findViewById(R.id.lang);
        lat = findViewById(R.id.lat);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference(userPath);

        key = databaseReference.push().getKey();

        // initializing our object
        // class variable.
        user= new User();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        //check permission
        if(ActivityCompat.checkSelfPermission(MainActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this
                , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            // if both permissions garanted => call methode
            getCurrentLocation();


        }else{
            //when permission is not garanted
            //request permission

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            },100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check condition
        if(requestCode == 100 && grantResults.length > 0 && (grantResults[0]+ grantResults[1]
                == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    //methode to add data of User in the firebase realTime database
    public void addUserDataToFireBase(String name,double lang,double lat){

        user.setName(name);user.setLang(lang);user.setLat(lat); user.setId(key);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                FireBaseTraitement.addUser("med",snapshot, MainActivity.this,databaseReference,key,user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //initialize LocationManger
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled
            //get Location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize Location
                    Location location = task.getResult();
                    //check condition
                    if (location != null){
                        //if location result is null
                        //set Latitude
                        lat.setText(String.valueOf(location.getLatitude()));
                        //set Longetude
                        lang.setText(String.valueOf(location.getLongitude()));

                        Intent intent= new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("langitud", location.getLongitude());
                        intent.putExtra("latitude", location.getLatitude());
                        intent.putExtra("userID", key);
                        startActivity(intent);

                        addUserDataToFireBase("med",location.getLongitude(),location.getLatitude());

                    }else{
                        //when location result is null
                        //initialization location request

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //initilize Location call back
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Inisilize location
                                Location location1 = locationResult.getLastLocation();
                                //set Latitude
                                lat.setText(String.valueOf(location1.getLatitude()));
                                //set Langitude
                                lang.setText(String.valueOf(location1.getLongitude()));


                                addUserDataToFireBase("med",location.getLongitude(),location.getLatitude());
                            }
                        };

                        //request location update
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            //when location service is not innabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }
}