package com.brix.budgetingapplication.AsyncTask;


import android.os.AsyncTask;

import com.brix.budgetingapplication.models.IncomeCategory;
import com.brix.budgetingapplication.persistance.IncomeCatDao;

public class UpdateIncome extends AsyncTask<IncomeCategory,Void,Void> {

    private IncomeCatDao mIncomeCatDao;

    public UpdateIncome(IncomeCatDao incomeCatDao) {
        this.mIncomeCatDao = incomeCatDao;
    }
    @Override
    protected Void doInBackground(IncomeCategory... incomeCategories) {
        mIncomeCatDao.update(incomeCategories);
        return null;
    }
}
