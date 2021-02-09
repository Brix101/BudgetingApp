package com.brix.budgetingapplication.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "incomeCat")
public class IncomeCategory implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "CategoryName")
    private String CategoryName;

    public IncomeCategory() {
    }

    public IncomeCategory(String categoryName) {
        CategoryName = categoryName;
    }

    protected IncomeCategory(Parcel in) {
        Id = in.readInt();
        CategoryName = in.readString();
    }

    public static final Creator<IncomeCategory> CREATOR = new Creator<IncomeCategory>() {
        @Override
        public IncomeCategory createFromParcel(Parcel in) {
            return new IncomeCategory(in);
        }

        @Override
        public IncomeCategory[] newArray(int size) {
            return new IncomeCategory[size];
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
        return "IncomeCategory{" +
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
        dest.writeInt(Id);
        dest.writeString(CategoryName);
    }
}
