package com.brix.budgetingapp.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;


@Database(entities = {ExpenseCategory.class, FinancialStatement.class, IncomeCategory.class},version = 3)
public abstract class BudgetDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "budgetDB";

    public abstract ExpenseCatDao expenseCatDao();
    public abstract FinancialStatementDao financialStatementDao();
    public abstract IncomeCatDao incomeCatDao();

    private static volatile BudgetDatabase Instance;

   public static BudgetDatabase getInstance(Context context){
        if(Instance == null){
            synchronized (BudgetDatabase.class){
                Instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BudgetDatabase.class,
                        DATABASE_NAME
                ).build();
            }
        }
        return Instance;
    }
}
