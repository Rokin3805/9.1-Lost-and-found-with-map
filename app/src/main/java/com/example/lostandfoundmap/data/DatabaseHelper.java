package com.example.lostandfoundmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lostandfoundmap.model.Item;
import com.example.lostandfoundmap.util.Util;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context)
    {
        //factory parameter nullified to resolve error
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    //Done using constants from util page so all appearances of a column name, table name etc can be changes by changing util class
    //updated with lat and long for location
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.POST_TYPE + " TEXT, "
                + Util.POSTER + " STRING, "
                + Util.PHONE_NO + " STRING, "
                + Util.DESCRIPTION + " STRING, "
                + Util.LOCATION + " STRING, "
                + Util.DATE + " DATE, "
                + Util.LONGITUDE + " DOUBLE, "
                + Util.LATITUDE + " DOUBLE"
                + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " +Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE);

        onCreate(sqLiteDatabase);
    }

    //return type is long since we set our primary key as autoincrement integer
    public Long insertItem(Item item)
    {
        //using constant definitions for fields from Util class, and getters from Item class (updated with lat and long for map)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.POST_TYPE, item.getPostType());
        contentValues.put(Util.POSTER, item.getPoster());
        contentValues.put(Util.PHONE_NO, item.getPhoneNo());
        contentValues.put(Util.DESCRIPTION, item.getDescription());
        contentValues.put(Util.LOCATION, item.getLocation());
        contentValues.put(Util.DATE, item.getDate());
        contentValues.put(Util.LONGITUDE, item.getLongitude());
        contentValues.put(Util.LATITUDE, item.getLatitude());
        //null columnhack prevents null values being stored
        long newItemId = db.insert(Util.TABLE_NAME, null, contentValues);
        //close database to prevent memory leaks
        db.close();
        return newItemId;
    }

    //return type is a boolean as searched items may not exist in table
    public boolean fetchItem(String searchItem, String postType) {
        SQLiteDatabase db = this.getReadableDatabase();
        // using LOWER function to make search non case-sensitive, as well as LIKE and wildcards to check description
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.POST_ID}, "LOWER(" + Util.DESCRIPTION +
                        ") LIKE ? AND " + Util.POST_TYPE + " =?",
                new String[]{"%" + searchItem.toLowerCase() + "%", postType}, null, null, null);
        int numberResults = cursor.getCount();
        db.close();
        //if results were found, return true;
        if (numberResults > 0)
            return true;
        else
            return false;
    }

    //reference for get methods: https://www.geeksforgeeks.org/how-to-read-data-from-sqlite-database-in-android/
    public ArrayList<Item> getItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //selects all * from cursor (row)
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setPostType(cursor.getString(1));
                item.setPoster(cursor.getString(2));
                item.setPhoneNo(cursor.getString(3));
                item.setDescription(cursor.getString(4));
                item.setLocation(cursor.getString(5));
                item.setDate(cursor.getString(6));
                item.setLongitude(cursor.getDouble(7));
                item.setLatitude(cursor.getDouble(8));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }
//updated with lat and long
    public ArrayList<Item> getItemsLike(String searchQuery) {
        ArrayList<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //also selects all from row, but only where the description contains the searchQuery
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME +
                " WHERE " + Util.DESCRIPTION + " LIKE '%" + searchQuery + "%' " +
                " OR " + Util.LOCATION + " LIKE '%" + searchQuery + "%'", null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setPostType(cursor.getString(1));
                item.setPoster(cursor.getString(2));
                item.setPhoneNo(cursor.getString(3));
                item.setDescription(cursor.getString(4));
                item.setLocation(cursor.getString(5));
                item.setDate(cursor.getString(6));
                item.setLongitude(cursor.getDouble(7));
                item.setLatitude(cursor.getDouble(8));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }
    //method to delete item
    public void deleteItem(Item item) {
        //writeable database because we intend to make changes
        SQLiteDatabase db = this.getWritableDatabase();
        //where all item properties match all column entries, delete that entry (?'s are placeholders replaced by the strings in array)
        db.delete(Util.TABLE_NAME,
                Util.POST_TYPE + "=? AND "
                        + Util.POSTER + "=? AND "
                        + Util.PHONE_NO + "=? AND "
                        + Util.DESCRIPTION + "=? AND "
                        + Util.LOCATION + "=? AND "
                        + Util.DATE + "=?",
                new String[] { item.getPostType(), item.getPoster(), item.getPhoneNo(),
                        item.getDescription(), item.getLocation(), item.getDate() });
        db.close();
    }





}
