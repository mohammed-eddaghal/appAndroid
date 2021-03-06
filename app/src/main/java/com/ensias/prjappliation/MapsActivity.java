package com.ensias.prjappliation;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener {

    static final String[] tolsName={"Bolt","Nail","Screwdriver","Bradawl","Handsaw","Nut","Screw","Wrench","Hammer","Hacksaw"};

    SupportMapFragment mapFragment;
    List<Item> itemList;
    List<Item> listItemMap = new ArrayList<>();
    List<Tool> tools=new ArrayList<>();

    List<Marker> markers = new ArrayList<>();

    Marker marker;

    private GoogleMap mMap;
    String userPath="users", toolPath="types";
    final static String keyUser = "-M_gOmulg-2mzNWN2d554";

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
        user = new User();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //FireBaseTraitement.getListOfTools();

        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);



        lang = getIntent().getDoubleExtra("langitud",1);
        lat = getIntent().getDoubleExtra("latitude",1);

        new LoadData().execute();



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void mapContent(){
        if(user.getName()!=null){
            Log.d("tagTestxx","xxx ||| "+user.getName()+"/ "+user.getLat()+"/ "+user.getLang());
            lang = user.getLang();
            lat = user.getLat();
        }else{
            Log.d("tagTestxx","xxx |||");
            lang = getIntent().getDoubleExtra("langitud",1);
            lat = getIntent().getDoubleExtra("latitude",1);
        }

        // Add a marker in Sydney and move the camera

        LatLng mark = new LatLng(lat, lang);


        mMap.addMarker(new MarkerOptions().position(mark).title(user.getName()));




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
        for (Marker marker:markers){
            marker.remove();
        }
        markers.clear();

        int x=0;
        //Toast.makeText(MapsActivity.this,searchQuery,Toast.LENGTH_LONG).show();
        Toast.makeText(MapsActivity.this,
                lat+" || "+lang,
                Toast.LENGTH_LONG).show();
        for(Item item:itemList){

            if((item.getTool().getToolName().toLowerCase()).compareTo(query.toLowerCase())==0){
                drawMarker(new LatLng(item.getUser().getLat(),item.getUser().getLang()),
                        BitmapDescriptorFactory.HUE_BLUE, item);
            }
        }

        if(tools.size()==0)Log.d("testOne","tools list is umpty");
        for(Tool tool:tools){
            Log.d("testZero",tool.getToolName());
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    public void setItemsList(){

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

    private MarkerOptions drawMarker(LatLng point,float id,Item item) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(id));

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions.title(item.getUser().getName()+"--"+item.getTool().getToolName()));
        markers.add(marker);

        mMap.setOnMarkerClickListener(this);

        mapMarker=markerOptions;
        return markerOptions;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getPosition().longitude!=lang&&marker.getPosition().latitude!=lat){
            Toast.makeText(MapsActivity.this,"latitude"+marker.getPosition().latitude+" | longitude"
                    +marker.getPosition().longitude,Toast.LENGTH_LONG).show();
        }
        return false;
    }



    public void getUser(@NonNull DataSnapshot snapshot) throws InterruptedException {
        //User userx = FireBaseTraitement.findUserByID(keyUser,snapshot,MapsActivity.this);

        User userx= FireBaseTraitement.getUserByID(keyUser,databaseReference);
        Log.d("tagTest","firebase traitment"+userx.getName()+"/ "+userx.getLat()+"/ "+userx.getLang());

        user.setId(userx.getId());
        user.setName(userx.getName());
        user.setLang(userx.getLang());
        user.setLat(userx.getLat());
        Log.d("tagTestDDD","firebase traitment"+user.getName()+"/ "+user.getLat()+"/ "+user.getLang());

        mapContent();
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            firebaseDatabase = FirebaseDatabase.getInstance();
            // below line is used to get reference for our database.
            databaseReference = firebaseDatabase.getReference(userPath);
            Log.d("test","log test 0");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // below line is used to get the
            // instance of our FIrebase database.

            try {
                user.cloneUser(FireBaseTraitement.getUserByID(keyUser,databaseReference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //databaseReference.addValueEventListener(MapsActivity.this);

            Log.d("test","log test 1"+user.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //key = databaseReference.push().getKey();
            //FireBaseTraitement.getUserByID(keyUser,databaseReference);
            mapContent();
            Log.d("test","log test 2"+user.toString());
            //mapContent();
        }
    }
    /*public void x(){
        final User user3=new User();
        databaseReference.child(keyUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // user3.setId(((User)((HashMap)task.getResult().getValue()).entrySet().iterator().next()).getId());


                    Log.d("firebase", task.getResult().getValue(User.class).toString());

                }
            }
        });
    }*/
    /*public void setToolsList(){
        databaseReference = firebaseDatabase.getReference(toolPath);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //toolList.add(dataSnapshot.getValue(Tool.class));
                    tools.add(dataSnapshot.getValue(Tool.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}