package com.example.lostandfoundmap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.lostandfoundmap.data.DatabaseHelper;
import com.example.lostandfoundmap.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostandfoundmap.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //generate item list using db helper
        DatabaseHelper db = new DatabaseHelper(this);
        itemList.addAll(db.getItems());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //iterate over item list
        for (int i = 0; i < itemList.size(); i++)
        {
            //for each item get the coords and description
            Item item = itemList.get(i);
            double latitude = item.getLatitude();
            double longitude = item.getLongitude();
            String description = item.getDescription();

            //only use first 10 characters of the description (or the full string if length < 10)
            String markerTitle = description.substring(0, Math.min(description.length(), 10));

            //create a LatLng object for the items location (as in the default sydney marker on creating this activity)
            LatLng itemLocation = new LatLng(latitude, longitude);

            //add a marker for the current item
            mMap.addMarker(new MarkerOptions().position(itemLocation).title(markerTitle));
        }

    }
}