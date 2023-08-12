package com.example.aggrocery.Models;

public class StockModel {
    private  int itemCode;
    private String itemName;

    private int qtyInStock;
    private double price;
    private boolean isReturnable;

    public StockModel(String itemName, int qtyInStock, double price, boolean isReturnable) {
        this.itemName = itemName;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.isReturnable = isReturnable;
    }
    public StockModel(int itemCode,String itemName, int qtyInStock, double price, boolean isReturnable) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.isReturnable = isReturnable;
    }
    public int getItemCode() {
        return itemCode;
    }
    public String getItemName() {
        return itemName;
    }

    public int getQtyInStock() {
        return qtyInStock;
    }

    public double getPrice() {
        return price;
    }

    public boolean isReturnable() {
        return isReturnable;
    }
}
