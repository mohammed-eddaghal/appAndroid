package com.ensias.prjappliation;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseTraitement {

    // creating a variable for our
    // Firebase Database.
    static FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();

    // creating a variable for our Database
    // Reference for Firebase.
    static DatabaseReference databaseReference;


    public static void getX(@NonNull String keyUser, DatabaseReference databaseReference){
        final User user3=new User();
        databaseReference.child(keyUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase static", "Error getting data", task.getException());
                }
                else {
                    // user3.setId(((User)((HashMap)task.getResult().getValue()).entrySet().iterator().next()).getId());


                    Log.d("firebase static", task.getResult().getValue(User.class).toString());

                }
            }
        });
    }


    public static boolean findeUser(String userName, DataSnapshot snapshot, Activity activity){
        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
            User user1=dataSnapshot.getValue(User.class);
            if((user1.getName().toLowerCase()).equals(userName.toLowerCase())){
                Toast.makeText(activity , "user already exist", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    public static void addUser(String userName, DataSnapshot snapshot, Activity activity, DatabaseReference databaseReference,String key, User user){
        if(!findeUser("med",snapshot,activity)){
            databaseReference.child(key).setValue(user).addOnCompleteListener((task)->{
                // after adding this data we are showing toast message.
                Toast.makeText(activity, "data added", Toast.LENGTH_SHORT).show();
            });}
    }

    public static User findUserByID(String id, DataSnapshot snapshot, Activity activity){
        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
            User user1=dataSnapshot.getValue(User.class);
            if(user1.getId().equals(id)){
                return user1;
            }
        }
        return null;
    }

    public static List<Tool> getListOfTools(){
        databaseReference = firebaseDatabase.getReference("types");
        List<Tool> tools=new ArrayList<>();
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
        if(tools.size()!=0) Log.d("testFive","list of tools not imptye");

        return tools;
    }


}
