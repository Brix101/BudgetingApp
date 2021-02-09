package com.brix.budgetingapplication.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "expenseCat")
public class ExpenseCategory implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "CategoryName")
    private String CategoryName;

    public ExpenseCategory() {
    }

    public ExpenseCategory(String categoryName) {
        CategoryName = categoryName;
    }

    protected ExpenseCategory(Parcel in) {
        Id = in.readInt();
        CategoryName = in.readString();
    }

    public static final Creator<ExpenseCategory> CREATOR = new Creator<ExpenseCategory>() {
        @Override
        public ExpenseCategory createFromParcel(Parcel in) {
            return new ExpenseCategory(in);
        }

        @Override
        public ExpenseCategory[] newArray(int size) {
            return new ExpenseCategory[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    @Override
    public String toString() {
        return "ExpenseCategory{" +
                "Id=" + Id +
                ", CategoryName='" + CategoryName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static Comparator<ExpenseCategory> Sorter = new Comparator<ExpenseCategory>() {
        @Override
        public int compare(ExpenseCategory E1, ExpenseCategory E2) {
            return E1.getCategoryName().compareTo(E2.getCategoryName());
        }
    };
}
