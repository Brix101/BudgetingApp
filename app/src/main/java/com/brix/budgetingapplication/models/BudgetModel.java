package com.brix.budgetingapplication.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moneyBudget")
public class BudgetModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "Category")
    private String Category;

    @ColumnInfo(name = "BudgetAmount")
    private double BudgetAmount;

    @ColumnInfo(name = "ExpenseAmount")
    private double ExpenseAmount;

    public BudgetModel() {
    }

    protected BudgetModel(Parcel in) {
        Id = in.readInt();
        Category = in.readString();
        BudgetAmount = in.readDouble();
        ExpenseAmount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Category);
        dest.writeDouble(BudgetAmount);
        dest.writeDouble(ExpenseAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BudgetModel> CREATOR = new Creator<BudgetModel>() {
        @Override
        public BudgetModel createFromParcel(Parcel in) {
            return new BudgetModel(in);
        }

        @Override
        public BudgetModel[] newArray(int size) {
            return new BudgetModel[size];
        }
    };

    @Override
    public String toString() {
        return "BudgetModel{" +
                "Id=" + Id +
                ", Category='" + Category + '\'' +
                ", BudgetAmount=" + BudgetAmount +
                ", ExpenseAmount=" + ExpenseAmount +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public double getBudgetAmount() {
        return BudgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        BudgetAmount = budgetAmount;
    }

    public double getExpenseAmount() {
        return ExpenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        ExpenseAmount = expenseAmount;
    }

    public BudgetModel(int id, String category, double budgetAmount, double expenseAmount) {
        Id = id;
        Category = category;
        BudgetAmount = budgetAmount;
        ExpenseAmount = expenseAmount;
    }
}