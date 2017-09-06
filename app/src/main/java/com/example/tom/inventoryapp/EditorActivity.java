package com.example.tom.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>
{


   /** make variables to be reference for the UI component **/
   /*Edit text field to enter the item's name*/
   private EditText mNameEditText;
    /*Edit text field to enter the item's quantity*/
    private EditText mQuantityEditText;
    /*Edit text field to enter the item's supplier*/
    private EditText mSupplierEditText;
    /*Edit text field to enter the item's price*/
    private EditText mPriceEditText;
    /*id number for loader*/
    private static final int Existing_Item_loader=1;
    /*variable of the CurrentUri sent from the CatalogActivity*/
    private  Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_);
        /**Examine the intent that launch this activity*/
        Intent intent=getIntent();
         mCurrentUri=intent.getData();

        if(mCurrentUri==null){
            setTitle(getString(R.string.new_item));

        }else {

            setTitle(getString(R.string.edit_item));
            //kick out the loader here to get the info of the current clicked listItem
            getLoaderManager().initLoader(Existing_Item_loader,null,this);

        }

    /**Select the UI component*/

    mNameEditText=(EditText)findViewById(R.id.item_name);
    mQuantityEditText=(EditText)findViewById(R.id.item_quantity);
    mPriceEditText =(EditText)findViewById(R.id.item_price);
    mSupplierEditText =(EditText)findViewById(R.id.item_supplier);

    }

   //inflate the menu of EditorActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                //save the new item   OR save changes of the item
                insertItem();
                finish();
            case R.id.action_delete:
                //delete the current selected item
                break;

        }
        return super.onOptionsItemSelected(item);
    }

   /*insert item function */
   private void insertItem(){
       /**get the text from the text field and convert them if needed */
      String mNameStringEditText=mNameEditText.getText().toString().trim();
      String mQuantityStringEditText=mQuantityEditText.getText().toString().trim();
      int Quantity=Integer.parseInt(mQuantityStringEditText);
      String mSupplierStringEditText=mSupplierEditText.getText().toString().trim();
      String mPriceStringEditText=mPriceEditText.getText().toString().trim();
      int price=Integer.parseInt(mPriceStringEditText);


       /** use the content values to map the column to the key value **/
       ContentValues values=new ContentValues();
       values.put(ItemEntry.Column_Item_name,mNameStringEditText);
       values.put(ItemEntry.Column_Item_quantity,Quantity);
       values.put(ItemEntry.Column_Item_price,price);
       values.put(ItemEntry.Column_Item_supplier,mSupplierStringEditText);

       /**use the contentResolver to insert the item in the DB*/
       Uri newUri=getContentResolver().insert(ItemEntry.CONTENT_URI,values);

      /**if the insert uri return is null give Toast of fail **/
      if(newUri==null){
          Toast.makeText(this,getString(R.string.Editor_fail),Toast.LENGTH_SHORT).show();
      } /**if the insert uri return is not  null give Toast of success **/else{

          Toast.makeText(this,getString(R.string.Editor_success),Toast.LENGTH_SHORT).show();
      }



   }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Request some data[for the current clicked listItem] from the database
        String[] projection={
                ItemEntry._ID,
                ItemEntry.Column_Item_name,
                ItemEntry.Column_Item_price,
                ItemEntry.Column_Item_quantity,
                ItemEntry.Column_Item_supplier,
        };
        return new CursorLoader(this,     //parent view
               mCurrentUri,
                projection,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        /*when the CursorLoader get the data from the DB i will handle it
        *by find the views and assign it with the values of the current clicked listItem
        *
        **/
        if(cursor ==null || cursor.getCount()<1){
            return;
        }
        /** i will get one record so use moveToFirst*/
        if(cursor.moveToFirst()){

           /**get the column index of the name,quantity,price,supplier*/
           int Column_Index_Name=cursor.getColumnIndex(ItemEntry.Column_Item_name);
           int Column_Index_Price=cursor.getColumnIndex(ItemEntry.Column_Item_price);
           int Column_Index_Supplier=cursor.getColumnIndex(ItemEntry.Column_Item_supplier);
           int Column_Index_Quantity=cursor.getColumnIndex(ItemEntry.Column_Item_quantity);

           /**Get the cursor content as key And values for the name,quantity,price,supplier*/
           String Column_Name_String=cursor.getString(Column_Index_Name);
           String Column_Supplier_String=cursor.getString(Column_Index_Supplier);
            int Column_price=cursor.getInt(Column_Index_Price);
            int Column_Quantity=cursor.getInt(Column_Index_Quantity);

            /**Set the fields with the corresponding view*/
            mNameEditText.setText(Column_Name_String);
            mPriceEditText.setText(Integer.toString(Column_price));
            mQuantityEditText.setText(Integer.toString(Column_Quantity));
            mSupplierEditText.setText(Column_Supplier_String);


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mNameEditText.setText("");
        mPriceEditText.setText("");

    }
}
