package com.brix.budgetingapplication.models;

import androidx.room.ColumnInfo;

public class GraphData {
    private String Category;

    private double Amount;

    public GraphData() {
    }

    public GraphData(String category, double amount) {
        Category = category;
        Amount = amount;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    @Override
    public String toString() {
        return "GraphData{" +
                "Category='" + Category + '\'' +
                ", Amount=" + Amount +
                '}';
    }
}
