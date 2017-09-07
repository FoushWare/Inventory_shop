package com.example.tom.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

public class CatalogActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * item loader constant number
     */
    private static final int ItemLoader = 0;
    /**
     * make object of the cursorAdapter
     */
    private ItemCursorAdapter mCursorAdapter;

    /**
     * Decrease button in the list_item ui
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        //SETup  FAB to go to EditorActivity when press
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //find list view and assign empty view to it
        ListView ItemListView = (ListView) findViewById(R.id.list);
        //find the empty view
        View empty_view = findViewById(R.id.empty_view);
        ItemListView.setEmptyView(empty_view);


        /**assign the adapter to the listView**/

        //there is no data here until the loader finish so give it null
        mCursorAdapter = new ItemCursorAdapter(this, null);
        ItemListView.setAdapter(mCursorAdapter);

        /**when listView item clicked make listener for that */
        ItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //make intent to go to EditorActivity
                /**the id is the id of the current clicked item*/
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri CurrentUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                intent.setData(CurrentUri);
                startActivity(intent);
            }
        });


        /**kick off  the loader*/
        getLoaderManager().initLoader(ItemLoader, null, this);
    }


    /**
     * implement Cursor loader Methods
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Define the Quire i want to load to this activity
        String[] projection = {
                ItemEntry._ID, //Don't forget it or there will be exception
                ItemEntry.Column_Item_name,
                ItemEntry.Column_Item_quantity,
                ItemEntry.Column_Item_price,
                ItemEntry.Column_Item_supplier
        };
        return new CursorLoader(this,                 //parent activity
                ItemEntry.CONTENT_URI,                 //uri of the table
                projection,                             //column i want to select
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //update the cursorAdapter with the new value
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //when the data is deleted or not needed empty the cursor
        mCursorAdapter.swapCursor(null);
    }


}
