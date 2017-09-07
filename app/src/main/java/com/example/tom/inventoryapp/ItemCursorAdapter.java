package com.example.tom.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.tom.inventoryapp.data.InventoryContract.ItemEntry;

/**
 * Created by tom on 9/5/17.
 */

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /**bind values to the view it mean mapping every EditText with the corresponding value*/
        /*find the view of the list_item and fill it with the corresponding value*/
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price);
        /*find the columns that represent these fields */
        int ColumnName = cursor.getColumnIndex(ItemEntry.Column_Item_name);
        int ColumnQuanity = cursor.getColumnIndex(ItemEntry.Column_Item_quantity);
        int ColumnPrice = cursor.getColumnIndex(ItemEntry.Column_Item_price);
        /*Read the values from the cursor*/
        String getName = cursor.getString(ColumnName);
        int getQuantity = cursor.getInt(ColumnQuanity);
        int getPrice = cursor.getInt(ColumnPrice);
        /*set the corresponding view with there values*/
        name.setText(getName);
        quantity.setText(Integer.toString(getQuantity));
        price.setText(Integer.toString(getPrice) + " LE");


    }
}
