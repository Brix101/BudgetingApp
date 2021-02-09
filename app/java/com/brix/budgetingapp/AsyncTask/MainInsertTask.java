package com.brix.budgetingapp.AsyncTask;

import android.os.AsyncTask;

import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.persistance.FinancialStatementDao;

public class MainInsertTask extends AsyncTask<FinancialStatement,Void,Void> {
    private FinancialStatementDao mFinancialStatementDao;

    public MainInsertTask(FinancialStatementDao mFinancialStatementDao) {
        this.mFinancialStatementDao = mFinancialStatementDao;
    }

    @Override
    protected Void doInBackground(FinancialStatement... financialStatements) {
        mFinancialStatementDao.insert(financialStatements);
        return null;
    }
}
