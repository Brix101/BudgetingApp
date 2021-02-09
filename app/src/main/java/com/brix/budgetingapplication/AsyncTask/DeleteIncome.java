package com.brix.budgetingapplication.AsyncTask;

import android.os.AsyncTask;

import com.brix.budgetingapplication.models.IncomeCategory;
import com.brix.budgetingapplication.persistance.IncomeCatDao;

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
