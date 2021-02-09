package com.brix.budgetingapplication.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.ExpenseCategory;
import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.models.IncomeCategory;


@Database(entities = {ExpenseCategory.class, FinancialStatement.class, IncomeCategory.class, BudgetModel.class},version = 5)
public abstract class BudgetDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "budgetDB";

    public abstract ExpenseCatDao expenseCatDao();
    public abstract FinancialStatementDao financialStatementDao();
    public abstract IncomeCatDao incomeCatDao();
    public abstract BudgetDao budgetDao();

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
