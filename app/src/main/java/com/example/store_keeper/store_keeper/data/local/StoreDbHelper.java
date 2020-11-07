/*
 * Created By Rajat Gupta And Harshita Joshi 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.store_keeper.store_keeper.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.util.Log;

import com.example.store_keeper.store_keeper.utils.AppConstants;
import com.example.store_keeper.store_keeper.utils.AppExecutors;
import com.example.store_keeper.store_keeper.data.local.contracts.ProductContract;
import com.example.store_keeper.store_keeper.data.local.contracts.SalesContract;
import com.example.store_keeper.store_keeper.data.local.contracts.SupplierContract;
import com.example.store_keeper.store_keeper.data.local.utils.SqliteUtility;

/**
 * Database Helper class that manages Database creation and Version management.
 *
 * @author Rajat Gupta And Harshita Joshi 
 */
public class StoreDbHelper extends SQLiteOpenHelper {
    //Constant used for logs
    private static final String LOG_TAG = StoreDbHelper.class.getSimpleName();

    //Constant for the Database Version
    private static final int DATABASE_VERSION = 1;
    //Constant for the Database Name
    private static final String DATABASE_NAME = "inventory.db";
    //Query that creates the Item Table
    private static final String CREATE_TABLE_ITEM
            = SqliteUtility.CREATE_TABLE + ProductContract.Product.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + ProductContract.Product._ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.SPACE + SqliteUtility.PRIMARY_KEY_AUTOINCREMENT + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.Product.COLUMN_ITEM_NAME + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.Product.COLUMN_ITEM_SKU + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.Product.COLUMN_ITEM_DESCRIPTION + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.Product.COLUMN_ITEM_CATEGORY_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_item_sku" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + ProductContract.Product.COLUMN_ITEM_SKU + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_FAIL + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_category_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + ProductContract.Product.COLUMN_ITEM_CATEGORY_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + ProductContract.ProductCategory.TABLE_NAME + SqliteUtility.OPEN_BRACE + ProductContract.ProductCategory._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Item Category Table
    private static final String CREATE_TABLE_ITEM_CATEGORY
            = SqliteUtility.CREATE_TABLE + ProductContract.ProductCategory.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + ProductContract.ProductCategory._ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.SPACE + SqliteUtility.PRIMARY_KEY_AUTOINCREMENT + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.ProductCategory.COLUMN_ITEM_CATEGORY_NAME + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_category_name" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + ProductContract.ProductCategory.COLUMN_ITEM_CATEGORY_NAME + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_FAIL
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Item Image Table
    private static final String CREATE_TABLE_ITEM_IMAGE
            = SqliteUtility.CREATE_TABLE + ProductContract.ProductImage.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + ProductContract.ProductImage.COLUMN_ITEM_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.ProductImage.COLUMN_ITEM_IMAGE_URI + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.ProductImage.COLUMN_ITEM_IMAGE_DEFAULT + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.DEFAULT + ProductContract.ProductImage.ITEM_IMAGE_NON_DEFAULT + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_image_uri" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + ProductContract.ProductImage.COLUMN_ITEM_ID + SqliteUtility.COMMA + SqliteUtility.SPACE + ProductContract.ProductImage.COLUMN_ITEM_IMAGE_URI + SqliteUtility.CLOSE_BRACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_item_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + ProductContract.ProductImage.COLUMN_ITEM_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + ProductContract.Product.TABLE_NAME + SqliteUtility.OPEN_BRACE + ProductContract.Product._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Item Attribute Table
    private static final String CREATE_TABLE_ITEM_ATTR
            = SqliteUtility.CREATE_TABLE + ProductContract.ProductAttribute.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + ProductContract.ProductAttribute.COLUMN_ITEM_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.ProductAttribute.COLUMN_ITEM_ATTR_NAME + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + ProductContract.ProductAttribute.COLUMN_ITEM_ATTR_VALUE + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_attr_name" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + ProductContract.ProductImage.COLUMN_ITEM_ID + SqliteUtility.COMMA + SqliteUtility.SPACE + ProductContract.ProductAttribute.COLUMN_ITEM_ATTR_NAME + SqliteUtility.CLOSE_BRACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_item_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + ProductContract.ProductAttribute.COLUMN_ITEM_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + ProductContract.Product.TABLE_NAME + SqliteUtility.OPEN_BRACE + ProductContract.Product._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Supplier Table
    private static final String CREATE_TABLE_SUPPLIER
            = SqliteUtility.CREATE_TABLE + SupplierContract.Supplier.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + SupplierContract.Supplier._ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.SPACE + SqliteUtility.PRIMARY_KEY_AUTOINCREMENT + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.Supplier.COLUMN_SUPPLIER_NAME + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.Supplier.COLUMN_SUPPLIER_CODE + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_supplier_code" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + SupplierContract.Supplier.COLUMN_SUPPLIER_CODE + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_FAIL
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Contact Type Table of the Supplier
    private static final String CREATE_TABLE_SUPPLIER_CONTACT_TYPE
            = SqliteUtility.CREATE_TABLE + SupplierContract.SupplierContactType.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + SupplierContract.SupplierContactType._ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.SPACE + SqliteUtility.PRIMARY_KEY + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.SupplierContactType.COLUMN_CONTACT_TYPE_NAME + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_type_name" + SqliteUtility.UNIQUE + SqliteUtility.OPEN_BRACE + SupplierContract.SupplierContactType.COLUMN_CONTACT_TYPE_NAME + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Supplier Contact Table
    private static final String CREATE_TABLE_SUPPLIER_CONTACT
            = SqliteUtility.CREATE_TABLE + SupplierContract.SupplierContact.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + SupplierContract.SupplierContact.COLUMN_SUPPLIER_CONTACT_TYPE_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.SupplierContact.COLUMN_SUPPLIER_CONTACT_VALUE + SqliteUtility.SPACE + SqliteUtility.TEXT + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.SupplierContact.COLUMN_SUPPLIER_CONTACT_DEFAULT + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.DEFAULT + SupplierContract.SupplierContact.SUPPLIER_CONTACT_NON_DEFAULT + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.SupplierContact.COLUMN_SUPPLIER_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_record" + SqliteUtility.UNIQUE
            + SqliteUtility.OPEN_BRACE + SupplierContract.SupplierContact.COLUMN_SUPPLIER_ID + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SupplierContract.SupplierContact.COLUMN_SUPPLIER_CONTACT_VALUE + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_REPLACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_contact_type_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SupplierContract.SupplierContact.COLUMN_SUPPLIER_CONTACT_TYPE_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + SupplierContract.SupplierContactType.TABLE_NAME + SqliteUtility.OPEN_BRACE + SupplierContract.SupplierContactType._ID + SqliteUtility.CLOSE_BRACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_supplier_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SupplierContract.SupplierContact.COLUMN_SUPPLIER_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + SupplierContract.Supplier.TABLE_NAME + SqliteUtility.OPEN_BRACE + SupplierContract.Supplier._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Item Supplier Info Table
    private static final String CREATE_TABLE_ITEM_SUPPLIER_INFO
            = SqliteUtility.CREATE_TABLE + SalesContract.ProductSupplierInfo.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + SalesContract.ProductSupplierInfo.COLUMN_ITEM_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInfo.COLUMN_SUPPLIER_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInfo.COLUMN_ITEM_UNIT_PRICE + SqliteUtility.SPACE + SqliteUtility.REAL + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.DEFAULT + SalesContract.ProductSupplierInfo.DEFAULT_ITEM_UNIT_PRICE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_record" + SqliteUtility.UNIQUE
            + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInfo.COLUMN_ITEM_ID + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInfo.COLUMN_SUPPLIER_ID + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_REPLACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_item_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInfo.COLUMN_ITEM_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + ProductContract.Product.TABLE_NAME + SqliteUtility.OPEN_BRACE + ProductContract.Product._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_supplier_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInfo.COLUMN_SUPPLIER_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + SupplierContract.Supplier.TABLE_NAME + SqliteUtility.OPEN_BRACE + SupplierContract.Supplier._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates the Item Supplier Inventory Table
    private static final String CREATE_TABLE_ITEM_SUPPLIER_INVENTORY
            = SqliteUtility.CREATE_TABLE + SalesContract.ProductSupplierInventory.TABLE_NAME
            + SqliteUtility.OPEN_BRACE
            + SalesContract.ProductSupplierInventory.COLUMN_ITEM_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInventory.COLUMN_SUPPLIER_ID + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInventory.COLUMN_ITEM_AVAIL_QUANTITY + SqliteUtility.SPACE + SqliteUtility.INTEGER + SqliteUtility.NOT + SqliteUtility.NULL + SqliteUtility.DEFAULT + SalesContract.ProductSupplierInventory.DEFAULT_ITEM_AVAIL_QUANTITY + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "unique_record" + SqliteUtility.UNIQUE
            + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInventory.COLUMN_ITEM_ID + SqliteUtility.COMMA + SqliteUtility.SPACE
            + SalesContract.ProductSupplierInventory.COLUMN_SUPPLIER_ID + SqliteUtility.CLOSE_BRACE + SqliteUtility.ON + SqliteUtility.CONFLICT_REPLACE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_item_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInventory.COLUMN_ITEM_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + ProductContract.Product.TABLE_NAME + SqliteUtility.OPEN_BRACE + ProductContract.Product._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE + SqliteUtility.COMMA
            + SqliteUtility.CONSTRAINT + "fk_supplier_id"
            + SqliteUtility.FOREIGN_KEY + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInventory.COLUMN_SUPPLIER_ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.REFERENCES + SupplierContract.Supplier.TABLE_NAME + SqliteUtility.OPEN_BRACE + SupplierContract.Supplier._ID + SqliteUtility.CLOSE_BRACE
            + SqliteUtility.ON + SqliteUtility.DELETE_CASCADE
            + SqliteUtility.CLOSE_BRACE;
    //Query that creates an Index on the "available_quantity" column
    //of "item_supplier_inventory" table
    private static final String CREATE_INDEX_SUPPLIER_QUANTITY
            = SqliteUtility.CREATE_INDEX + "quantity_idx" + SqliteUtility.ON + SalesContract.ProductSupplierInventory.TABLE_NAME
            + SqliteUtility.SPACE + SqliteUtility.OPEN_BRACE + SalesContract.ProductSupplierInventory.COLUMN_ITEM_AVAIL_QUANTITY + SqliteUtility.CLOSE_BRACE;
    //Stores the singleton instance of this class
    private static volatile StoreDbHelper INSTANCE;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    private StoreDbHelper(Context context) {
        //Propagating the call to super, to initialize the database
        super(context,
                DATABASE_NAME,
                new AppCursorFactory(), //Custom CursorFactory to log the queries fired
                DATABASE_VERSION
        );
    }

    /**
     * Static Singleton Constructor that creates a single instance of {@link StoreDbHelper}.
     *
     * @param context is the {@link Context} of the Activity used to open/create the database
     * @return New or existing instance of {@link StoreDbHelper}
     */
    public static synchronized StoreDbHelper getInstance(Context context) {
        if (INSTANCE == null) {
            //When instance is not available
            synchronized (StoreDbHelper.class) {
                //Apply lock and check for the instance again
                if (INSTANCE == null) {
                    //When there is no instance, create a new one
                    //Using Application Context instead of Activity Context to prevent memory leaks
                    INSTANCE = new StoreDbHelper(context.getApplicationContext());
                }
            }
        }
        //Returning the instance of StoreDbHelper
        return INSTANCE;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Executing the statements to create the tables in the database: START
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_ITEM_CATEGORY);
        db.execSQL(CREATE_TABLE_ITEM_IMAGE);
        db.execSQL(CREATE_TABLE_ITEM_ATTR);
        db.execSQL(CREATE_TABLE_SUPPLIER);
        db.execSQL(CREATE_TABLE_SUPPLIER_CONTACT_TYPE);
        db.execSQL(CREATE_TABLE_SUPPLIER_CONTACT);
        db.execSQL(CREATE_TABLE_ITEM_SUPPLIER_INFO);
        db.execSQL(CREATE_TABLE_ITEM_SUPPLIER_INVENTORY);
        //Executing the statements to create the tables in the database: END

        //Creating an Index on the Available Quantity column of the table "item_supplier_inventory"
        db.execSQL(CREATE_INDEX_SUPPLIER_QUANTITY);

        //Inserting predefined set of categories into the 'item_category' table
        insertPredefinedCategories();

        //Inserting predefined set of contact types into the 'contact_type' table
        insertPredefinedContactTypes();
    }

    /**
     * Method that loads a predefined set of categories into the 'item_category' table
     */
    private void insertPredefinedCategories() {
        //Executing in the background thread
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            //Get the Categories to insert
            String[] preloadedCategories = ProductContract.ProductCategory.getPreloadedCategories();

            //Retrieving the database in write mode
            SQLiteDatabase writableDatabase = INSTANCE.getWritableDatabase();

            //Stores the count of records inserted
            int noOfRecordsInserted = 0;

            //Locking the database for insert
            writableDatabase.beginTransaction();
            try {
                //Iterate over the Categories, to insert them one by one
                for (String categoryName : preloadedCategories) {
                    //Preparing the ContentValue for inserting the category
                    ContentValues categoryContentValues = new ContentValues();
                    categoryContentValues.put(ProductContract.ProductCategory.COLUMN_ITEM_CATEGORY_NAME, categoryName);

                    //Executing insert
                    long recordId = writableDatabase.insert(
                            ProductContract.ProductCategory.TABLE_NAME,
                            null,
                            categoryContentValues
                    );

                    //Validating the operation
                    if (recordId == -1) {
                        //Bail out on failure
                        break;
                    } else {
                        //On success of inserting the record

                        //Increment the number of records inserted
                        noOfRecordsInserted++;
                    }
                }
            } finally {

                //Evaluating the number of records inserted to commit accordingly
                if (noOfRecordsInserted == preloadedCategories.length) {
                    //When all categories are inserted successfully, mark the transaction as successful
                    writableDatabase.setTransactionSuccessful();
                    Log.i(LOG_TAG, "insertPredefinedCategories: Predefined Categories inserted");
                } else {
                    //When NOT all categories are inserted, log the error
                    Log.e(LOG_TAG, "insertPredefinedCategories: Predefined Categories failed to insert.");
                }

                //Releasing the lock in the end
                writableDatabase.endTransaction();
            }

        });
    }

    /**
     * Method that loads a predefined set of contact types into the 'contact_type' table
     */
    private void insertPredefinedContactTypes() {
        //Executing in the background thread
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            //Get the Contact types to insert
            String[] preloadedContactTypes = SupplierContract.SupplierContactType.getPreloadedContactTypes();

            //Retrieving the database in write mode
            SQLiteDatabase writableDatabase = INSTANCE.getWritableDatabase();

            //Stores the count of records inserted
            int noOfRecordsInserted = 0;

            //Number of Contact types to preload
            int noOfContactTypes = preloadedContactTypes.length;

            //Locking the database for insert
            writableDatabase.beginTransaction();
            try {
                //Iterate over the contact types, to insert them one by one
                for (int index = 0; index < noOfContactTypes; index++) {
                    //Preparing the ContentValue for inserting the contact type
                    ContentValues contactTypeContentValues = new ContentValues();
                    contactTypeContentValues.put(SupplierContract.SupplierContactType._ID, String.valueOf(index));
                    contactTypeContentValues.put(SupplierContract.SupplierContactType.COLUMN_CONTACT_TYPE_NAME, preloadedContactTypes[index]);

                    //Executing insert
                    long recordId = writableDatabase.insert(
                            SupplierContract.SupplierContactType.TABLE_NAME,
                            null,
                            contactTypeContentValues
                    );

                    //Validating the operation
                    if (recordId == -1) {
                        //Bail out on failure
                        break;
                    } else {
                        //On success of inserting the record

                        //Increment the number of records inserted
                        noOfRecordsInserted++;
                    }
                }
            } finally {

                //Evaluating the number of records inserted to commit accordingly
                if (noOfRecordsInserted == noOfContactTypes) {
                    //When all contact types are inserted successfully, mark the transaction as successful
                    writableDatabase.setTransactionSuccessful();
                    Log.i(LOG_TAG, "insertPredefinedContactTypes: Predefined Contact types inserted");
                } else {
                    //When NOT all contact types are inserted, log the error
                    Log.e(LOG_TAG, "insertPredefinedContactTypes: Predefined Contact types failed to insert.");
                }

                //Releasing the lock in the end
                writableDatabase.endTransaction();
            }

        });
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Dropping the Database tables and recreating it on Version Upgrade

        //Dropping the tables
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductCategory.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductImage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductAttribute.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SupplierContract.Supplier.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SupplierContract.SupplierContactType.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SupplierContract.SupplierContact.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SalesContract.ProductSupplierInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SalesContract.ProductSupplierInventory.TABLE_NAME);

        //Dropping the Indexes manually created
        db.execSQL("DROP INDEX quantity_idx");

        //Recreating all the tables
        onCreate(db);
    }

    /**
     * Called when the database connection is being configured, to enable features such as
     * write-ahead logging or foreign key support.
     * <p>
     * This method is called before {@link #onCreate}, {@link #onUpgrade}, {@link #onDowngrade}, or
     * {@link #onOpen} are called. It should not modify the database except to configure the
     * database connection as required.
     * </p>
     * <p>
     * This method should only call methods that configure the parameters of the database
     * connection, such as {@link SQLiteDatabase#enableWriteAheadLogging}
     * {@link SQLiteDatabase#setForeignKeyConstraintsEnabled}, {@link SQLiteDatabase#setLocale},
     * {@link SQLiteDatabase#setMaximumSize}, or executing PRAGMA statements.
     * </p>
     *
     * @param db The database.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        //Enabling the Foreign Key Constraints
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //For API Level 16 and above
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            //For API Level below 16
            //Using the PRAGMA command to enable the Foreign Key Constraints
            String foreignKeyPragmaStr = "PRAGMA foreign_keys = ON";
            db.execSQL(foreignKeyPragmaStr);
        }
    }

    /**
     * Implementation of {@link SQLiteDatabase.CursorFactory} to log the queries fired
     * when executed in debug mode.
     */
    private static class AppCursorFactory implements SQLiteDatabase.CursorFactory {

        /**
         * Execute a query and provide access to its result set through a Cursor
         * interface.
         *
         * @param db          a reference to a Database object that is already constructed
         *                    and opened.
         * @param masterQuery A driver for SQLiteCursors that is used to create them and gets notified
         *                    by the cursors it creates on significant events in their lifetimes.
         * @param editTable   The name of the table used for this query
         * @param query       The {@link SQLiteQuery} object associated with this cursor object.
         * @return Returns a {@link SQLiteCursor} instance to the query.
         */
        @Override
        public Cursor newCursor(SQLiteDatabase db,
                                SQLiteCursorDriver masterQuery,
                                String editTable, SQLiteQuery query) {

            //Log the Query when logging is enabled
            if (AppConstants.LOG_CURSOR_QUERIES) {
                Log.i(LOG_TAG, "Table: " + editTable);
                Log.i(LOG_TAG, "newCursor: " + query.toString());
            }

            //Returning the SQLiteCursor instance for the query fired
            return new SQLiteCursor(masterQuery, editTable, query);
        }
    }
}