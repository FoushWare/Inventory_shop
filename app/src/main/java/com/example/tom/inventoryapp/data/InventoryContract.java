package com.example.tom.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tom on 9/4/17.
 */

public final class InventoryContract {
    //make it private to prevent someone from instantiating  it accidentally cause it's constant

    /**
     * content Authority like domain name
     */
    public static final String CONTENT_AUTHORITY = "com.example.tom.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEM = "items";


    private InventoryContract() {
    }

    public static final class ItemEntry implements BaseColumns {
        /**
         * Table name items
         */
        public static final String TABLE_NAME = "items";
        /**
         * Table Columns
         **/

                /*unique id for each row only used in the database*/
        public static final String ID = BaseColumns._ID;
        /*name of the item*/
        public static final String Column_Item_name = "name";
        /*quantity  of  current item */
        public static final String Column_Item_quantity = "quantity";
        /*supplier  of  current item */
        public static final String Column_Item_supplier = "supplier";
        /*price  of  current item */
        public static final String Column_Item_price = "price";
        /*picture of the item*/
        public static final String Column_Item_img = "image";

        /**
         * make the content uri
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEM);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;


    }


}
