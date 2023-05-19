package com.example.lostandfoundmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lostandfoundmap.data.DatabaseHelper;
import com.example.lostandfoundmap.model.Item;



public class ItemDetails extends AppCompatActivity {
    String postType;
    String poster;
    String phoneNo;
    String description;
    String location;
    String date;

    double latitude;

    double longitude;

    TextView postTypeTextView;
    TextView posterTextView;
    TextView phoneNoTextView;
    TextView descriptionTextView;
    TextView locationTextView;

    TextView dateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        //get the intent that started this activity (and has the relevant strings)
        Intent intent = getIntent();

        // Unpack the string extras from the intent
        postType = intent.getStringExtra("postType");
        poster = intent.getStringExtra("poster");
        phoneNo = intent.getStringExtra("phoneNo");
        description = intent.getStringExtra("description");
        location = intent.getStringExtra("location");
        date = intent.getStringExtra("date");

        //now retrieves lat and long from intent to use item constructor
        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);


        // Do something with the unpacked strings
        // For example, you could set the text of TextViews in the layout to display the values
        postTypeTextView = findViewById(R.id.postTypeTextView);
        postTypeTextView.setText(postType);

        posterTextView = findViewById(R.id.posterTextView);
        posterTextView.setText(poster);

        phoneNoTextView = findViewById(R.id.phoneNoTextView);
        phoneNoTextView.setText(phoneNo);

        descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);

        locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(location);

        dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(date);
    }

    public void deleteItem(View view) {

        //create Item object with the relevant info
        Item thisPost = new Item(postType, poster, phoneNo, description, location, date, latitude, longitude);
        DatabaseHelper db = new DatabaseHelper(this);
        //call the deleteItem method from DatabaseHelper class on the item created with the post data
        db.deleteItem(thisPost);
        //go to main activity and close this (removes from backstack)
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        finish();
    }
}