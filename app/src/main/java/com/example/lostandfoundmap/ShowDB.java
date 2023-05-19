package com.example.lostandfoundmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.lostandfoundmap.data.DatabaseHelper;
import com.example.lostandfoundmap.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ShowDB extends AppCompatActivity implements LostAndFoundAdapter.OnRowClickListener {

    RecyclerView itemRecycler;
    LostAndFoundAdapter adapter;
    List<Item> itemList = new ArrayList<>();
    EditText searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);
        itemRecycler = findViewById(R.id.itemRecycler);
        searchTerm = findViewById(R.id.searchEntry);

        //create the adapter and set it on the RecyclerView
        adapter = new LostAndFoundAdapter(this, itemList, this);
        itemRecycler.setAdapter(adapter);

        //set the layout manager with a vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemRecycler.setLayoutManager(layoutManager);

        //populate the list with items from the database
        DatabaseHelper db = new DatabaseHelper(this);
        itemList.addAll(db.getItems());
        adapter.notifyDataSetChanged();

        //add a text watcher to the search term EditText field
        searchTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //does nothing in my implementation but is required method
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //clear the current list (otherwise this would just duplicate elements matching string in the Recycler
                itemList.clear();
                //populate the list with items from the database matching the search term (cast as string)
                itemList.addAll(db.getItemsLike(charSequence.toString()));
                //notify adapter of updated parameter (default adapter method) to refresh display
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                //also doesnothing in my implementation
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //retrieves the clicked item from the itemList
        Item clickedItem = itemList.get(position);

        //intent to open item details activity
        Intent intent = new Intent(this, ItemDetails.class);

        //use the getters (from Item class) to putExtra the string properties of the item into the intent
        intent.putExtra("postType", clickedItem.getPostType());
        intent.putExtra("poster", clickedItem.getPoster());
        intent.putExtra("phoneNo", clickedItem.getPhoneNo());
        intent.putExtra("description", clickedItem.getDescription());
        intent.putExtra("location", clickedItem.getLocation());
        intent.putExtra("date", clickedItem.getDate());
        intent.putExtra("long", clickedItem.getLongitude());
        intent.putExtra("lat", clickedItem.getLatitude());

        //start the activity with the intent
        startActivity(intent);
    }

}
