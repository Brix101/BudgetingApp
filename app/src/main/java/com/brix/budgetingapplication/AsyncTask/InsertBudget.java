package com.brix.budgetingapplication.AsyncTask;

import android.os.AsyncTask;

import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.persistance.BudgetDao;

public class InsertBudget extends AsyncTask<BudgetModel,Void,Void> {

    private BudgetDao mBudgetDao;

    public InsertBudget(BudgetDao mBudgetDao) {
        this.mBudgetDao = mBudgetDao;
    }

    @Override
    protected Void doInBackground(BudgetModel... budgetModels) {
        mBudgetDao.insert(budgetModels);
        return null;
    }
}
