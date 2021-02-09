package com.brix.budgetingapplication.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "financialState")
public class FinancialStatement implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "CategoryType")
    private String CategoryType;

    @ColumnInfo(name = "SubCategory")
    private String SubCategory;

    @ColumnInfo(name = "Amount")
    private double Amount;

    @ColumnInfo(name = "Description")
    private String Description;

    @ColumnInfo(name = "Date")
    private String Date;

    public FinancialStatement() {
    }

    public FinancialStatement( String categoryType, String subCategory, double amount, String description, String date) {
        CategoryType = categoryType;
        SubCategory = subCategory;
        Amount = amount;
        Description = description;
        Date = date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(String categoryType) {
        CategoryType = categoryType;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public static Creator<FinancialStatement> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "FinancialStatement{" +
                "Id=" + Id +
                ", CategoryType='" + CategoryType + '\'' +
                ", SubCategory='" + SubCategory + '\'' +
                ", Amount=" + Amount +
                ", Description='" + Description + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }

    protected FinancialStatement(Parcel in) {
        Id = in.readInt();
        CategoryType = in.readString();
        SubCategory = in.readString();
        Amount = in.readDouble();
        Description = in.readString();
        Date = in.readString();
    }

    public static final Creator<FinancialStatement> CREATOR = new Creator<FinancialStatement>() {
        @Override
        public FinancialStatement createFromParcel(Parcel in) {
            return new FinancialStatement(in);
        }

        @Override
        public FinancialStatement[] newArray(int size) {
            return new FinancialStatement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(CategoryType);
        dest.writeString(SubCategory);
        dest.writeDouble(Amount);
        dest.writeString(Description);
        dest.writeString(Date);
    }
}
