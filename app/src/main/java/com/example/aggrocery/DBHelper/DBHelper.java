package com.example.aggrocery.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.Models.Usermodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {




    // User Table



    public DBHelper(Context context) {
        super(context, DBCreation.DBNAME, null, DBCreation.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBCreation.CREATE_TABLE_USER);
        db.execSQL(DBCreation.CREATE_TABLE_PURCHASE);
        db.execSQL(DBCreation.CREATE_TABLE_SALES);
        db.execSQL(DBCreation.CREATE_TABLE_STOCK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBCreation.TABLE_USER);
        onCreate(db);
    }

    public long insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBCreation.COL_USERNAME, username);
        values.put(DBCreation.COL_EMAIL_ID, email);
        values.put(DBCreation.COL_PASSWORD, password);
        long result = db.insert(DBCreation.TABLE_USER, null, values);
        db.close();
        return result;
    }

    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DBCreation.COL_USERNAME, DBCreation.COL_PASSWORD};
        String selection = DBCreation.COL_USERNAME + " = ? AND " + DBCreation.COL_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(DBCreation.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public Usermodel verifyCredentials(String username, String password) {
        Usermodel usermodel =  null;


        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns = {DBCreation.COL_USERNAME, DBCreation.COL_PASSWORD,DBCreation.COL_EMAIL_ID};
            String selection = DBCreation.COL_USERNAME + " = ? AND " + DBCreation.COL_PASSWORD + " = ?";
            String[] selectionArgs = {username, password};

            Cursor cursor = db.query(DBCreation.TABLE_USER, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(DBCreation.COL_USERNAME);
                int passwordIndex = cursor.getColumnIndex(DBCreation.COL_PASSWORD);
                int emailIndex = cursor.getColumnIndex(DBCreation.COL_EMAIL_ID);

                if (usernameIndex >= 0 && passwordIndex >= 0) {
                    String fetchedUsername = cursor.getString(usernameIndex);
                    String fetchedPassword = cursor.getString(passwordIndex);
                    String fetchedEmail = cursor.getString(emailIndex);

                    usermodel = new Usermodel(fetchedUsername,fetchedEmail,fetchedPassword);
                }
                }
            cursor.close();
            db.close();
        return usermodel;

        } catch (Exception e) {
            return  null;
        }
    }

    public long insertStockItem(StockModel stockItem) {
        SQLiteDatabase db = null;
        long newRowId = -1;

        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBCreation.COL_ITEM_NAME, stockItem.getItemName());
            values.put(DBCreation.COL_QTY_STOCK, stockItem.getQtyInStock());
            values.put(DBCreation.COL_PRICE, stockItem.getPrice());
            values.put(DBCreation.COL_RETURNABLE, stockItem.isReturnable() ? 1 : 0);

            newRowId = db.insert(DBCreation.TABLE_STOCK, null, values);
            Log.d("newROwID",String.valueOf(newRowId));
        } catch (Exception e) {
            Log.e("InsertError", "An error occurred while inserting item to stock: " + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return newRowId;
    }

    public List<StockModel> getAllStockItems() {
        List<StockModel> stockList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBCreation.TABLE_STOCK;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int itemCodeIndex = cursor.getColumnIndex(DBCreation.COL_ITEM_CODE);
                int itemNameIndex = cursor.getColumnIndex(DBCreation.COL_ITEM_NAME);
                int qtyInStockIndex = cursor.getColumnIndex(DBCreation.COL_QTY_STOCK);
                int priceIndex = cursor.getColumnIndex(DBCreation.COL_PRICE);
                int returnableIndex = cursor.getColumnIndex(DBCreation.COL_RETURNABLE);
                int itemCode = cursor.getInt(itemCodeIndex);
                String itemName = cursor.getString(itemNameIndex);
                int qtyInStock = cursor.getInt(qtyInStockIndex);
                double price = cursor.getDouble(priceIndex);
                int returnableInt = cursor.getInt(returnableIndex);
                boolean isReturnable = returnableInt == 1;

                StockModel stockItem = new StockModel(itemCode,itemName, qtyInStock, price, isReturnable);
                stockList.add(stockItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return stockList;
    }
    public boolean checkItemExistsInStock(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DBCreation.COL_ITEM_CODE};
        String selection = DBCreation.COL_ITEM_CODE + " = ?";
        String[] selectionArgs = {itemCode};
        Cursor cursor = db.query(DBCreation.TABLE_STOCK, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to get the available quantity of an item
    public int getAvailableQuantity(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DBCreation.COL_QTY_STOCK};
        String selection = DBCreation.COL_ITEM_CODE + " = ?";
        String[] selectionArgs = {itemCode};
        Cursor cursor = db.query(DBCreation.TABLE_STOCK, columns, selection, selectionArgs, null, null, null);

        int availableQuantity = 0;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DBCreation.COL_QTY_STOCK);

            Log.d("availableQuantity","asd"+String.valueOf(columnIndex));
            if (columnIndex != -1) {
                availableQuantity = cursor.getInt(columnIndex);
                if (availableQuantity < 0) {
                    availableQuantity = 0; // Ensure non-negative value
                }
            }
        }

        cursor.close();
        return availableQuantity;
    }



    // Method to update stock quantity
    public boolean updateStockQuantity(String itemCode, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBCreation.COL_QTY_STOCK, newQuantity);

        int numRowsUpdated = db.update(
                DBCreation.TABLE_STOCK,
                values,
                DBCreation.COL_ITEM_CODE + " = ?",
                new String[]{itemCode}
        );

        return numRowsUpdated > 0;
    }
    public boolean addSalesDetails(String itemCode, String customerName, String customerEmail,
                                   int qtySold, Date salesDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBCreation.COL_ITEM_CODE, itemCode);
        values.put(DBCreation.COL_CUSTOMER_NAME, customerName);
        values.put(DBCreation.COL_CUSTOMER_EMAIL, customerEmail);
        values.put(DBCreation.COL_QTY_SOLD, qtySold);

        // Convert the Date object to a suitable text format
        String formattedDate = DateFormat.format("yyyy-MM-dd HH:mm:ss", salesDate).toString();
        values.put(DBCreation.COL_DATE_OF_SALES, formattedDate);

        long newRowId = db.insert(DBCreation.TABLE_SALES, null, values);
        db.close();

        return newRowId != -1;
    }
    public boolean addPurchaseDetails(String itemCode, int qtyPurchased, Date purchaseDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBCreation.COL_ITEM_CODE, itemCode);
        values.put(DBCreation.COL_QTY_PURCHASED, qtyPurchased);

        // Convert the Date object to a suitable text format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(purchaseDate);
        values.put(DBCreation.COL_DATE_OF_PURCHASE, formattedDate);

        long newRowId = db.insert(DBCreation.TABLE_PURCHASE, null, values);
        db.close();

        return newRowId != -1;
    }
    public StockModel searchStockItem(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        StockModel stockItem = null;

        Cursor cursor = db.query(DBCreation.TABLE_STOCK,
                new String[] { DBCreation.COL_ITEM_CODE, DBCreation.COL_ITEM_NAME,
                        DBCreation.COL_QTY_STOCK, DBCreation.COL_PRICE, DBCreation.COL_RETURNABLE },
                DBCreation.COL_ITEM_CODE + "=?",
                new String[] { itemCode }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
             int itemCodeIndex = cursor.getColumnIndex(DBCreation.COL_ITEM_CODE);
            int itemNameIndex = cursor.getColumnIndex(DBCreation.COL_ITEM_NAME);
            int qtyInStockIndex = cursor.getColumnIndex(DBCreation.COL_QTY_STOCK);
            int priceIndex = cursor.getColumnIndex(DBCreation.COL_PRICE);
            int returnableIndex = cursor.getColumnIndex(DBCreation.COL_RETURNABLE);
            int itemCodeNew = cursor.getInt(itemCodeIndex);
            String itemName = cursor.getString(itemNameIndex);
            int qtyInStock = cursor.getInt(qtyInStockIndex);
            double price = cursor.getDouble(priceIndex);
            int returnableInt = cursor.getInt(returnableIndex);
            boolean isReturnable = returnableInt == 1;

             stockItem = new StockModel(itemCodeNew,itemName, qtyInStock, price, isReturnable);
            cursor.close();
        }

        db.close();
        return stockItem;
    }

}


