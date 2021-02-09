package com.brix.budgetingapp.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.brix.budgetingapp.AsyncTask.DeleteExpense;
import com.brix.budgetingapp.AsyncTask.DeleteIncome;
import com.brix.budgetingapp.AsyncTask.MainInsertTask;
import com.brix.budgetingapp.AsyncTask.UpdateExpense;
import com.brix.budgetingapp.AsyncTask.UpdateIncome;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;

import java.util.List;

public class Repository {
    private BudgetDatabase mBudgetDatabase;

    public Repository(Context context) {
        mBudgetDatabase = BudgetDatabase.getInstance(context);
    }

    public LiveData<List<FinancialStatement>> getFinStateData() {
        return mBudgetDatabase.financialStatementDao().getData();
    }
    public LiveData<List<ExpenseCategory>> getExpenseData() {
        return mBudgetDatabase.expenseCatDao().getData();
    }
    public LiveData<List<IncomeCategory>> getIncomeData() {
        return mBudgetDatabase.incomeCatDao().getData();
    }


    public void deleteIncomeTask(IncomeCategory incomeCategory){
        new DeleteIncome(mBudgetDatabase.incomeCatDao()).execute(incomeCategory);
    }
    public void deleteExpenseTask(ExpenseCategory expenseCategory){
        new DeleteExpense(mBudgetDatabase.expenseCatDao()).execute(expenseCategory);
    }

    public void updateIncomeTask(IncomeCategory incomeCategory){
        new UpdateIncome(mBudgetDatabase.incomeCatDao()).execute(incomeCategory);
    }
    public void updateExpenseTask(ExpenseCategory expenseCategory){
        new UpdateExpense(mBudgetDatabase.expenseCatDao()).execute(expenseCategory);
    }

    public void insertMainTask(FinancialStatement financialStatement){
        new MainInsertTask(mBudgetDatabase.financialStatementDao()).execute(financialStatement);
    }

}
