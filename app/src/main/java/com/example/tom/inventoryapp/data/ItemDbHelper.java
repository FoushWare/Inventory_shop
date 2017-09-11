package com.example.tom.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

/**
 * Created by tom on 9/4/17.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "Inventory.db";
    /**
     * version of the database
     */
    private static final int DATABASE_VERSION = 1;


    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**create the schema of the table*/
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ( "
                + ItemEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemEntry.Column_Item_name + " TEXT NOT NULL ," +
                ItemEntry.Column_Item_quantity + " INTEGER NOT NULL ," +
                ItemEntry.Column_Item_price + " INTEGER NOT NULL ," +
                ItemEntry.Column_Item_supplier + " TEXT NOT NULL ," +
                ItemEntry.Column_Item_img + " BLOB NOT NULL" + " )";
        /**Execute the table*/
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    //this is called when database need to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
