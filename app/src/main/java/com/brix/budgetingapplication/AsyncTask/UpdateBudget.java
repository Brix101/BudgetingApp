package com.brix.budgetingapplication.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.persistance.BudgetDao;

public class UpdateBudget extends AsyncTask<BudgetModel,Void,Void> {

    private BudgetDao mBudgetDao;

    public UpdateBudget(BudgetDao mBudgetDao) {
        this.mBudgetDao = mBudgetDao;
    }

    @Override
    protected Void doInBackground(BudgetModel... budgetModels) {
        mBudgetDao.update(budgetModels);
        return null;
    }
}