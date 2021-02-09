package com.brix.budgetingapp.AsyncTask;

import android.os.AsyncTask;

import com.brix.budgetingapp.models.IncomeCategory;
import com.brix.budgetingapp.persistance.IncomeCatDao;

public class DeleteIncome extends AsyncTask<IncomeCategory,Void,Void> {

    private IncomeCatDao mIncomeCatDao;

    public DeleteIncome(IncomeCatDao incomeCatDao) {
        this.mIncomeCatDao = incomeCatDao;
    }

    @Override
    protected Void doInBackground(IncomeCategory... incomeCategories) {
        mIncomeCatDao.delete(incomeCategories);
        return null;
    }
}
