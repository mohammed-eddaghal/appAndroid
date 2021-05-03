package com.ensias.prjappliation;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener {

    static final String[] tolsName={"Bolt","Nail","Screwdriver","Bradawl","Handsaw","Nut","Screw","Wrench","Hammer","Hacksaw"};

    List<Item> itemList;
    List<Item> listItemMap = new ArrayList<>();
    //List<Tool> tools;

    Marker marker;

    private GoogleMap mMap;
    String userPath="users";

    double lang,lat;

    String searchQuery;



    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    //instance of user classe
    User user;

    SearchView editsearch;

    String key;
    private MarkerOptions mapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //setItemsList();

        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        lang = getIntent().getDoubleExtra("langitud",1);
        lat = getIntent().getDoubleExtra("latitude",1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference(userPath);
        key = databaseReference.push().getKey();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lang = getIntent().getDoubleExtra("langitud",1);
        lat = getIntent().getDoubleExtra("latitude",1);
        Log.d("testLocation",lang+" / "+lat);
/*
        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
            User user1=dataSnapshot.getValue(User.class);
            if(user1.getName().equals("med")){
                Toast.makeText(MainActivity.this, "user already exist", Toast.LENGTH_SHORT).show();
                return;
            }
        }*/
        /*for(Item item:itemList){
            //Log.d("tst",query.toLowerCase()+"|"+item.getTool().getToolName().toLowerCase());
                drawMarker(new LatLng(item.getUser().getLat(),item.getUser().getLang()),
                        BitmapDescriptorFactory.HUE_GREEN);

                /*mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(item.getUser().getLat(),item.getUser().getLang())));
                //.title(item.getUser().getName()+"|"+item.getTool().getToolName()));
            }*/




        // Add a marker in Sydney and move the camera
        LatLng mark = new LatLng(lat, lang);



        mMap.addMarker(new MarkerOptions().position(mark).title("my location"));



        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 16));

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery=query;
        setItemsList();
        int x=0;
        //Toast.makeText(MapsActivity.this,searchQuery,Toast.LENGTH_LONG).show();
        Toast.makeText(MapsActivity.this,
                lat+" || "+lang,
                Toast.LENGTH_LONG).show();
        for(Item item:itemList){

            if((item.getTool().getToolName().toLowerCase()).compareTo(query.toLowerCase())==0){
            drawMarker(new LatLng(item.getUser().getLat(),item.getUser().getLang()),
                    BitmapDescriptorFactory.HUE_BLUE)
                    .title("|"+item.getTool().getToolName());
            }
        }


        drawMarker(new LatLng(34.0063, -6.7204),
                BitmapDescriptorFactory.HUE_GREEN);
        /*for(Item item:itemList){
            Log.d("tst",query.toLowerCase()+"|"+item.getTool().getToolName().toLowerCase());

            if((item.getTool().getToolName().toLowerCase()).compareTo(query.toLowerCase())==0){
                Toast.makeText(MapsActivity.this,
                        query.toLowerCase()+"|"+item.getTool().getToolName().toLowerCase()+"//"+x++,
                        Toast.LENGTH_LONG).show();
                listItemMap.add(item);
                drawMarker(new LatLng(item.getUser().getLat(),item.getUser().getLang()),
                        BitmapDescriptorFactory.HUE_GREEN);

                /*mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(item.getUser().getLat(),item.getUser().getLang())));
                        //.title(item.getUser().getName()+"|"+item.getTool().getToolName()));
            }
        }*/
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    public void setItemsList(){
        //setToolsList();

        /*tools = new ArrayList<>();
        for(String tool:tolsName){
            Log.v("tool",tool);
            tools.add(new Tool(tool));
        }*/
        int z=0;
        itemList = new ArrayList<>();
        for(int i=0;i<50;i++){
            double x = Math.random()/100;
            double y = Math.random()/100;
            User user= new User(key,lang+x,lat+y);
            itemList.add(new Item(user,new Tool(tolsName[z++])));
            if(z==10)z=0;

        }
    }

    private MarkerOptions drawMarker(LatLng point,float id) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(id));

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions);

        mapMarker=markerOptions;
        return markerOptions;
    }

    /*public void setToolsList(){
        tools = new ArrayList<>();
        for(String tool:tolsName){
            tools.add(new Tool(tool));
        }
    }*/
}