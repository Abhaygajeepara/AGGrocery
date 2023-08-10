package com.example.aggrocery.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.Models.Usermodel;

import java.util.ArrayList;
import java.util.List;

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
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(DBCreation.TABLE_STOCK, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String itemName = String.valueOf(cursor.getColumnIndex(DBCreation.COL_ITEM_NAME));
                    int qtyInStock = cursor.getColumnIndex(DBCreation.COL_QTY_STOCK);
                    double price = cursor.getColumnIndex(DBCreation.COL_PRICE);
                    int returnableInt = cursor.getColumnIndex(DBCreation.COL_RETURNABLE);
                    boolean isReturnable = returnableInt == 1;

                    StockModel stockModel = new StockModel(itemName, qtyInStock, price, isReturnable);
                    stockList.add(stockModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("QueryError", "Error retrieving data from stock table: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return stockList;
    }
}

