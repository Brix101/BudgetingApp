package com.brix.budgetingapp.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.IncomeCategory;

import java.util.List;

@Dao
public interface IncomeCatDao {

    @Insert
    void insert(IncomeCategory[] incomeCategories);

    @Query("SELECT * FROM incomeCat")
    LiveData<List<IncomeCategory>> getData();

    @Delete
    int delete(IncomeCategory[] incomeCategories);

    @Update
    int update(IncomeCategory[] incomeCategories);

    @Query("SELECT * FROM incomeCat WHERE Id = :Id")
    IncomeCategory findById(int Id);
}
