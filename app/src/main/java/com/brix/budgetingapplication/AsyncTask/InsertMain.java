package com.brix.budgetingapplication.AsyncTask;


import android.os.AsyncTask;

import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.persistance.FinancialStatementDao;

public class InsertMain extends AsyncTask<FinancialStatement,Void,Void> {
    private FinancialStatementDao mFinancialStatementDao;

    public InsertMain(FinancialStatementDao mFinancialStatementDao) {
        this.mFinancialStatementDao = mFinancialStatementDao;
    }

    @Override
    protected Void doInBackground(FinancialStatement... financialStatements) {
        mFinancialStatementDao.insert(financialStatements);
        return null;
    }
}
