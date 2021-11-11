package com.qasp.unibeat.firebase;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Database {

    FirebaseFirestore firestore;

    public Database(){
        firestore = FirebaseFirestore.getInstance();
        getUserInfo(); // get empty data
        setUserInfo(); // set data
        getUserInfo(); // get full data
    }

    public void getUserInfo(){
        firestore.collection("users").document("Jose")
                .get().addOnCompleteListener((task) -> {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.i("Database", doc.getString("Name"));
                    }else{
                        Log.i("Database", "Doc not found");
                    }
                });

    }

    public void setUserInfo(){
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put("Name", "Jose");
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        Task future = firestore.collection("users").document("Jose").set(docData);
        future.addOnCompleteListener((task) -> {
            Log.i("Database","Update time : " + future.getResult());
            getUserInfo();
        });
        // future.get() blocks on response

    }

}
