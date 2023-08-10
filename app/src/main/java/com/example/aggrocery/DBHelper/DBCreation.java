package com.example.aggrocery.DBHelper;

public class DBCreation {
    static final String DBNAME = "AGGrocery.db";
    static final int VERSION =1;
    // User Table
    static public final String TABLE_USER = "User";
    static public final String COL_USERNAME = "username";
    static  public final String COL_EMAIL_ID = "emailId";
    static  public final String COL_PASSWORD = "password";

    // Stock Table
    static public final String TABLE_STOCK = "Stock";
    static public  final String COL_ITEM_CODE = "itemCode";
    static  public  final String COL_ITEM_NAME = "itemName";
    static  public final String COL_QTY_STOCK = "qtyStock";
    static public  final String COL_PRICE = "price";
    static public  final String COL_RETURNABLE = "returnable";

    // Sales Table
    static public  final String TABLE_SALES = "Sales";
    static public  final String COL_ORDER_NUMBER = "orderNumber";
    static  public final String COL_CUSTOMER_NAME = "customerName";
    static  public final String COL_CUSTOMER_EMAIL = "customerEmail";
    static  public final String COL_QTY_SOLD = "qtySold";
    static  public final String COL_DATE_OF_SALES = "dateOfSales";

    // Purchase Table
    static public final String TABLE_PURCHASE = "Purchase";
    static  public final String COL_INVOICE_NUMBER = "invoiceNumber";
    static  public final String COL_QTY_PURCHASED = "qtyPurchased";
    static public final String COL_DATE_OF_PURCHASE = "dateOfPurchase";

    // SQL Queries to create tables
    static  public final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COL_USERNAME + " TEXT, " +
            COL_EMAIL_ID + " TEXT, " +
            COL_PASSWORD + " TEXT, " +
            "PRIMARY KEY(" + COL_USERNAME + ")" +
            ");";

    static public  final String CREATE_TABLE_STOCK = "CREATE TABLE " + TABLE_STOCK + " (" +
            COL_ITEM_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_NAME + " TEXT, " +
            COL_QTY_STOCK + " INTEGER, " +
            COL_PRICE + " REAL, " +
            COL_RETURNABLE + " INTEGER" +
            ");";

    static public final String CREATE_TABLE_SALES = "CREATE TABLE " + TABLE_SALES + " (" +
            COL_ORDER_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_CODE + " INTEGER, " +
            COL_CUSTOMER_NAME + " TEXT, " +
            COL_CUSTOMER_EMAIL + " TEXT, " +
            COL_QTY_SOLD + " INTEGER, " +
            COL_DATE_OF_SALES + " DATE" +
            ");";

    static public  final String CREATE_TABLE_PURCHASE = "CREATE TABLE " + TABLE_PURCHASE + " (" +
            COL_INVOICE_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_CODE + " INTEGER, " +
            COL_QTY_PURCHASED + " INTEGER, " +
            COL_DATE_OF_PURCHASE + " DATE" +
            ");";

    // SQL Queries to drop tables
    static  public final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER + ";";
    static  public final String DROP_TABLE_STOCK = "DROP TABLE IF EXISTS " + TABLE_STOCK + ";";
    static  public final String DROP_TABLE_SALES = "DROP TABLE IF EXISTS " + TABLE_SALES + ";";
    static  public final String DROP_TABLE_PURCHASE = "DROP TABLE IF EXISTS " + TABLE_PURCHASE + ";";
}
