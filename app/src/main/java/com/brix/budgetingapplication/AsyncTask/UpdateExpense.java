package com.brix.budgetingapplication.AsyncTask;


import android.os.AsyncTask;

import com.brix.budgetingapplication.models.ExpenseCategory;
import com.brix.budgetingapplication.persistance.ExpenseCatDao;

public class UpdateExpense extends AsyncTask<ExpenseCategory,Void,Void> {

    private ExpenseCatDao mExpenseCatDao;

    public UpdateExpense(ExpenseCatDao mExpenseCatDao) {
        this.mExpenseCatDao = mExpenseCatDao;
    }
    @Override
    protected Void doInBackground(ExpenseCategory... expenseCategories) {
        mExpenseCatDao.update(expenseCategories);
        return null;
    }
}
