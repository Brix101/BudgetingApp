package com.brix.budgetingapp.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.brix.budgetingapp.models.FinancialStatement;

import java.util.List;

@Dao
public interface FinancialStatementDao {

    @Insert
    void insert(FinancialStatement[] financialStatements);

    @Query("SELECT * FROM financialState")
    LiveData<List<FinancialStatement>> getData();

    @Delete
    int delete(FinancialStatement[] financialStatements);

    @Update
    int update(FinancialStatement[] financialStatements);

}
