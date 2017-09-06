package com.example.tom.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

public class EditorActivity extends AppCompatActivity {

   /** make variables to be reference for the UI component **/
   /*Edit text field to enter the item's name*/
   private EditText mNameEditText;
    /*Edit text field to enter the item's quantity*/
    private EditText mQuantityEditText;
    /*Edit text field to enter the item's supplier*/
    private EditText mSupplierEditText;
    /*Edit text field to enter the item's price*/
    private EditText mPriceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_);
        /**Examine the intent that launch this activity*/
        Intent intent=getIntent();
        Uri CurrentUri=intent.getData();

        if(CurrentUri==null){
            setTitle(getString(R.string.new_item));

        }else {

            setTitle(getString(R.string.edit_item));
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






}
