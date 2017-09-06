package com.example.tom.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

/**
 * Created by tom on 9/4/17.
 */

public class ItemProvider extends ContentProvider {

    /*Tag for log message*/
    public static final String LOG_TAG=ItemProvider.class.getSimpleName();
   /**Here is the role of the uriMatcher*/

   /*uri code that returned when shooting specific uri [content://com.example.tom.inventoryshop]*/
   private static final int ITEM=100;
   /*uri code that returned when shooting specific uri [content://com.example.tom.inventoryshop/pet_id]*/
    private static final int ITEM_ID=101;
    /*object from the ItemDbHelper*/

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
   static {
       /**
        *@1 content_authority
        * @2 Path
        * @3 return code for this uri
        * */
       // present all info about the the whole table
       sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH_ITEM,ITEM);
       //with id for present all info about the current selected item
       sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH_ITEM+"/#",ITEM_ID);
   }

   /*Database helper to play with the database*/
   private ItemDbHelper mDbhelper;


    @Override
    public boolean onCreate() {
        mDbhelper=new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match=sUriMatcher.match(uri);
        SQLiteDatabase db=mDbhelper.getReadableDatabase();
        Cursor cursor;
        switch (match){
            case ITEM:
                cursor=db.query(ItemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case ITEM_ID:
                selection=ItemEntry._ID+"=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(ItemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown uri: "+uri);
        }
        //set spy [listener] to the contentResolver so we can know if there is change or not to update
        //changes at the same time
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEM:
                return ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match =sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                return InsertItem(uri,values);
            default:
                throw new IllegalArgumentException("invalid URI: "+uri);
        }
    }
    private Uri InsertItem(Uri uri,ContentValues values){

        SQLiteDatabase db=mDbhelper.getWritableDatabase();
        //return id of the inserted item
        long id=db.insert(ItemEntry.TABLE_NAME,null,values);
        if(id ==-1){
            Log.e(LOG_TAG,"can't insert this item");
            return null;
        }
        //notify all listener that there is change
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match =sUriMatcher.match(uri);
        SQLiteDatabase db=mDbhelper.getWritableDatabase();
        int rowDelete;
        switch (match){
            //Delete all the table
            case ITEM:
                 rowDelete= db.delete(ItemEntry.TABLE_NAME,selection,selectionArgs);
                break;
                //Delete specific item in the table
            case ITEM_ID:
                selection=ItemEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDelete=db.delete(ItemEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not valid for this uri: "+uri);
        }
        if(rowDelete != 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
       int match=sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                //doesn't make sense to update all the table but i implement it in case something happen in the app
                return updateItem(uri,values,selection,selectionArgs);
            case ITEM_ID:
                /*extract the id from the uri */
                selection=ItemEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("invalid URI: "+uri);
        }
    }



    private int updateItem(Uri uri,ContentValues values,String selection,String[] selectionArgs) {

        SQLiteDatabase db=mDbhelper.getWritableDatabase();
        /**Make some validation for the updated data*/
        if(values.containsKey( ItemEntry.Column_Item_name) ){
            String name=values.getAsString(ItemEntry.Column_Item_name);
            if(name == null){
                throw new IllegalArgumentException("Item need name");
            }
        }
        if(values.containsKey( ItemEntry.Column_Item_price) ){

            Integer price=values.getAsInteger(ItemEntry.Column_Item_name);
            if(price !=null && price<0){

                throw new IllegalArgumentException("Item need price and not negative value");
            }
        }
        if(values.containsKey( ItemEntry.Column_Item_quantity) ){

            Integer quantity=values.getAsInteger(ItemEntry.Column_Item_quantity);
            if(quantity !=null && quantity<0){

                throw new IllegalArgumentException("Item need price and not negative value");
            }

        }
        if(values.containsKey( ItemEntry.Column_Item_supplier) ){
            String supplier=values.getAsString(ItemEntry.Column_Item_supplier);
            if(supplier == null){
                throw new IllegalArgumentException("Item need name");
            }
        }
          int rowUpdated=db.update(ItemEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowUpdated !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdated;
    }

}
