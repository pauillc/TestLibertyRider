package com.example.pauill_c.testlibertyrider;

import com.google.firebase.database.*;

/**
 * Created by pauill_c on 26/03/2017.
 */

public class Database {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Read a key from database
    public void getReference(String key) {
        DatabaseReference ref = database.getReference(key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                MainActivity.mTextMessage.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.err.println("Error in Database -> Event Listener: " + error.getMessage());
            }
        });;
    }

    // Write a value with his key in the database
    public void pushData(String key, String value){
        DatabaseReference myRef = database.getReference(key);

        myRef.setValue(value);
    }
}
