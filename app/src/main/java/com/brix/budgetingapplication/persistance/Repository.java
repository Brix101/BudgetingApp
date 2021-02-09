package com.brix.budgetingapplication.persistance;

import android.content.Context;


import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.brix.budgetingapplication.AsyncTask.DeleteBudget;
import com.brix.budgetingapplication.AsyncTask.DeleteExpense;
import com.brix.budgetingapplication.AsyncTask.DeleteFinState;
import com.brix.budgetingapplication.AsyncTask.DeleteIncome;
import com.brix.budgetingapplication.AsyncTask.InsertBudget;
import com.brix.budgetingapplication.AsyncTask.InsertMain;
import com.brix.budgetingapplication.AsyncTask.UpdateBudget;
import com.brix.budgetingapplication.AsyncTask.UpdateExpense;
import com.brix.budgetingapplication.AsyncTask.UpdateFinState;
import com.brix.budgetingapplication.AsyncTask.UpdateIncome;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.ExpenseCategory;
import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.models.GraphData;
import com.brix.budgetingapplication.models.IncomeCategory;

import java.util.List;

public class Repository {
    private BudgetDatabase mBudgetDatabase;

    public Repository(Context context) {
        mBudgetDatabase = BudgetDatabase.getInstance(context);
    }

    public FinancialStatement findById(int id) {
        return mBudgetDatabase.financialStatementDao().findById(id);
    }
    public LiveData<List<ExpenseCategory>> getExpenseData() {
        return mBudgetDatabase.expenseCatDao().getData();
    }
    public LiveData<List<IncomeCategory>> getIncomeData() {
        return mBudgetDatabase.incomeCatDao().getData();
    }
    public LiveData<List<BudgetModel>> getBudgetData() {
        return mBudgetDatabase.budgetDao().getBudgetData();
    }
    public LiveData<BudgetModel> updateTotalExpense(String category) {
        return mBudgetDatabase.budgetDao().updateTotalExpense(category);
    }
    public  LiveData<List<GraphData>> getExpenseTotal(String date1, String date2){
        return mBudgetDatabase.financialStatementDao().getExpenseTotal(date1,date2);
    }
    public LiveData<List<FinancialStatement>> getDateSelector(String date) {
        return mBudgetDatabase.financialStatementDao().getDateSelector(date);
    }
    public LiveData<List<FinancialStatement>> getWeekSelector(String date1,String date2) {
        return mBudgetDatabase.financialStatementDao().getWeekSelector(date1,date2);
    }
    public LiveData<Double> getExpenseTotal(String date){
        return mBudgetDatabase.financialStatementDao().getExpenseTotal(date);
    }
    public LiveData<Double> getIncomeTotal(String date){
        return mBudgetDatabase.financialStatementDao().getIncomeTotal(date);
    }
    public LiveData<Double> getExpenseWeekTotal(String date1,String date2){
        return mBudgetDatabase.financialStatementDao().getExpenseWeekTotal(date1,date2);
    }
    public LiveData<Double> getIncomeWeekTotal(String date1,String date2){
        return mBudgetDatabase.financialStatementDao().getIncomeWeekTotal(date1,date2);
    }
    public LiveData<Double> geTotalBudget(){
        return mBudgetDatabase.budgetDao().geTotalBudget();
    }


    public BudgetModel findBudgetById(int Id){
        return mBudgetDatabase.budgetDao().findBudgetById(Id);
    }
    public  LiveData<Double> getExpenseBudgetTotal(String date1,String date2,String sub){
        return mBudgetDatabase.financialStatementDao().getExpenseBudgetTotal(date1,date2,sub);
    }

    public void deleteIncomeTask(IncomeCategory incomeCategory){
        new DeleteIncome(mBudgetDatabase.incomeCatDao()).execute(incomeCategory);
    }
    public void deleteExpenseTask(ExpenseCategory expenseCategory){
        new DeleteExpense(mBudgetDatabase.expenseCatDao()).execute(expenseCategory);
    }
    public void deleteFinState(FinancialStatement financialStatement){
        new DeleteFinState(mBudgetDatabase.financialStatementDao()).execute(financialStatement);
    }

    public void updateIncomeTask(IncomeCategory incomeCategory){
        new UpdateIncome(mBudgetDatabase.incomeCatDao()).execute(incomeCategory);
    }
    public void updateExpenseTask(ExpenseCategory expenseCategory){
        new UpdateExpense(mBudgetDatabase.expenseCatDao()).execute(expenseCategory);
    }
    public void updateFinState(FinancialStatement financialStatement){
        new UpdateFinState(mBudgetDatabase.financialStatementDao()).execute(financialStatement);
    }

    public void insertMainTask(FinancialStatement financialStatement){
        new InsertMain(mBudgetDatabase.financialStatementDao()).execute(financialStatement);
    }
    public void insertBudget(BudgetModel budgetModel){
        new InsertBudget(mBudgetDatabase.budgetDao()).execute(budgetModel);
    }
    public void updateBudget(BudgetModel budgetModel){
        new UpdateBudget(mBudgetDatabase.budgetDao()).execute(budgetModel);
    }
    public void deleteBudget(BudgetModel budgetModel){
        new DeleteBudget(mBudgetDatabase.budgetDao()).execute(budgetModel);
    }

}
