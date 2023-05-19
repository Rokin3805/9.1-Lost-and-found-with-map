package com.example.lostandfoundmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostandfoundmap.data.DatabaseHelper;
import com.example.lostandfoundmap.model.Item;


public class AdPost extends AppCompatActivity {

    DatabaseHelper db;

    RadioGroup radioGroup;
    RadioButton isLost;
    RadioButton isFound;

    Intent goToMain;

    EditText name;
    EditText phoneNum;
    EditText description;
    EditText date;

    Button addLocation;
    double longitude;
    double latitude;

    //variables to take value from listener on button click, as well as boolean to monitor
    double addedLat;

    double addedLong;

    boolean hasAdded;
    LocationManager locationManager;
    LocationListener locationListener;


    Button submit;

    //overridden onRequestPermission results required
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_post);

        db = new DatabaseHelper(this);
        radioGroup = findViewById(R.id.radioButtons);
        isLost = radioGroup.findViewById(R.id.radioLost);
        isFound = radioGroup.findViewById(R.id.radioFound);
        //default state for is lost is true
        isLost.setChecked(true);
        name = findViewById(R.id.nameEntry);
        phoneNum = findViewById(R.id.phoneEntry);
        description = findViewById(R.id.descriptionEntry);
        date = findViewById(R.id.dateEntry);
        submit = findViewById(R.id.submitButton);

        addLocation = findViewById(R.id.locationEntry);

        //default, will toggle on adding location
        hasAdded = false;

        //location manager and listener for obtaining lat and long
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //update latitude and longitude variables using listener
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }




        //intent to go back to MainActivity
        goToMain = new Intent(this, MainActivity.class);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedLat = latitude;
                addedLong = longitude;
                hasAdded = true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postType;
                String descriptionStr;
                //if isLost is checked, assign value "Lost", else assign "Found". Concat post type to start of description for display purposes
                if (isLost.isChecked()) {
                    postType = "Lost";
                    descriptionStr = postType + " " + description.getText().toString().trim();
                }
                else
                {
                    postType = "Found";
                    descriptionStr = postType + " " + description.getText().toString().trim();
                }
                //all other strings received in standard fashion
                String poster = name.getText().toString().trim();
                String phoneNo = phoneNum.getText().toString().trim();
                String dateStr = date.getText().toString().trim();
                String locationStr = "NOT RELEVANT";

                //most basic input validation to ensure empty fields are not passed
                if (poster.equals("") || phoneNo.equals("") || descriptionStr.equals("") || dateStr.equals("") || locationStr.equals("") ||!hasAdded) {
                    Toast.makeText(AdPost.this, "Fill all input fields", Toast.LENGTH_LONG).show();
                } else
                {
                    //create a new Item object with input values
                    Item item = new Item(postType, poster, phoneNo, descriptionStr, dateStr, locationStr, addedLat, addedLong);

                    //insert the new item into the database
                    long result = db.insertItem(item);

                    //check if the item was successfully inserted into the database, toast user with result, return to main if added
                    if (result != -1) {
                        Toast.makeText(AdPost.this, "Item successfully added", Toast.LENGTH_SHORT).show();
                        startActivity(goToMain);
                        //remove current activity from backStack
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AdPost.this, "Failed to post item", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
}