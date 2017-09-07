package com.example.tom.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * make variables to be reference for the UI component
     **/
   /*Edit text field to enter the item's name*/
    private EditText mNameEditText;
    /*Edit text field to enter the item's quantity*/
    private EditText mQuantityEditText;
    /*Edit text field to enter the item's supplier*/
    private EditText mSupplierEditText;
    /*Edit text field to enter the item's price*/
    private EditText mPriceEditText;
    /*id number for loader*/
    private static final int Existing_Item_loader = 1;
    /*variable of the CurrentUri sent from the CatalogActivity*/
    private Uri mCurrentUri;
    /*variable to indict if there was change in the Editing the current clicked Item*/
    private boolean mItemHasChanged = false;

    /**
     * make listener for touch of the EditText to indict if there was any change
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mItemHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_);
        /**Examine the intent that launch this activity*/
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle(getString(R.string.new_item));

        } else {

            setTitle(getString(R.string.edit_item));
            //kick out the loader here to get the info of the current clicked listItem
            getLoaderManager().initLoader(Existing_Item_loader, null, this);

        }

        /**Select the UI component*/

        mNameEditText = (EditText) findViewById(R.id.item_name);
        mQuantityEditText = (EditText) findViewById(R.id.item_quantity);
        mPriceEditText = (EditText) findViewById(R.id.item_price);
        mSupplierEditText = (EditText) findViewById(R.id.item_supplier);
        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);


    }

    //inflate the menu of EditorActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //save the new item   OR save changes of the item
                saveItem();
                finish();
            case R.id.action_delete:
                //delete the current selected item
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:

                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                //when back icon clicked [from the up bar]

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    /*insert item function */
    private void saveItem() {
        /**get the text from the text field and convert them if needed */
        String mNameStringEditText = mNameEditText.getText().toString().trim();
        String mQuantityStringEditText = mQuantityEditText.getText().toString().trim();
        int Quantity = Integer.parseInt(mQuantityStringEditText);
        String mSupplierStringEditText = mSupplierEditText.getText().toString().trim();
        String mPriceStringEditText = mPriceEditText.getText().toString().trim();
        int price = Integer.parseInt(mPriceStringEditText);
        /**if every field is empty do nothing */
        if (mCurrentUri == null &&
                mQuantityStringEditText.isEmpty() &&
                mPriceStringEditText.isEmpty() && mSupplierStringEditText.isEmpty()) {
            return;
        }
        /** use the content values to map the column to the key value **/
        ContentValues values = new ContentValues();
        values.put(ItemEntry.Column_Item_name, mNameStringEditText);
        values.put(ItemEntry.Column_Item_quantity, Quantity);
        values.put(ItemEntry.Column_Item_price, price);
        values.put(ItemEntry.Column_Item_supplier, mSupplierStringEditText);

        if (mCurrentUri == null) {

            /**use the contentResolver to insert the item in the DB*/
            Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

            /**if the insert uri return is null give Toast of fail **/
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.Editor_fail), Toast.LENGTH_SHORT).show();
            } /**if the insert uri return is not  null give Toast of success **/
            else {

                Toast.makeText(this, getString(R.string.Editor_success), Toast.LENGTH_SHORT).show();
            }
        } else { //this part for update current Item

            int rowAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowAffected == 0) {
                Toast.makeText(this, getString(R.string.Editor_update_fail), Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, getString(R.string.Editor_update_success), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    /**
     * This method is called when the back button is pressed.
     */


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Request some data[for the current clicked listItem] from the database
        String[] projection = {
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
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        /** i will get one record so use moveToFirst*/
        if (cursor.moveToFirst()) {

            /**get the column index of the name,quantity,price,supplier*/
            int Column_Index_Name = cursor.getColumnIndex(ItemEntry.Column_Item_name);
            int Column_Index_Price = cursor.getColumnIndex(ItemEntry.Column_Item_price);
            int Column_Index_Supplier = cursor.getColumnIndex(ItemEntry.Column_Item_supplier);
            int Column_Index_Quantity = cursor.getColumnIndex(ItemEntry.Column_Item_quantity);

            /**Get the cursor content as key And values for the name,quantity,price,supplier*/
            String Column_Name_String = cursor.getString(Column_Index_Name);
            String Column_Supplier_String = cursor.getString(Column_Index_Supplier);
            int Column_price = cursor.getInt(Column_Index_Price);
            int Column_Quantity = cursor.getInt(Column_Index_Quantity);

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

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    /**
     * +     * This method is called after invalidateOptionsMenu(), so that the
     * +     * menu can be updated (some menu items can be hidden or made visible).
     * +
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database.
     */
    private void deleteItem() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }


}//End of Activity
