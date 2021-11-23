package com.qasp.unibeat.firebase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Database {

    FirebaseFirestore firestore;

    public Database(){
        firestore = FirebaseFirestore.getInstance();
    }

    @SuppressLint("NewApi")
    public void getUserInfo(String email, Consumer<User> callback, Runnable error){
        firestore.collection("users").document(email)
                .get().addOnCompleteListener((task) -> {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.i("Database", doc.getString("Name"));
                        callback.accept(doc.toObject(User.class));
                    }else{
                        Log.i("Database", "Email [" + email + "] not found");
                        error.run();
                    }
                });

    }

    public void setUserInfo(User user, String email){
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put("Name", user.getName());
        docData.put("AboutMe", user.getAboutMe());
        docData.put("Location", user.getLocation());
        docData.put("LikedSongs", user.getLikedSongs());
        docData.put("Matches", user.getMatches());
        docData.put("ProfileImageURI", user.getImageUri());
        docData.put("ViewedSongs", user.getViewedSongs());
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        Task future = firestore.collection("users").document(email).set(docData);
        future.addOnCompleteListener((task) -> {
            Log.i("Database","Update time : " + future.getResult());
        });
        // future.get() blocks on response
    }

    public void getMessages(String ownerEmail, String otherEmail, Consumer<ChatRoom> callback, Runnable error) {
        firestore.collection("users").document(ownerEmail).collection("messages").document("Email")
                    .get().addOnCompleteListener((task) -> {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()) {
                    Log.i("Database", "Getting messages and exists");
                    Log.i("Database", "Type of data: " + doc.getData().get(otherEmail).getClass());
                    callback.accept(null);
                }else{
                    Log.i("Database", "No messages found");
                    error.run();
                }
            });
    }

}
