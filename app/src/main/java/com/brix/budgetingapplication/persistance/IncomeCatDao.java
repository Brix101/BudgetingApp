package com.brix.budgetingapplication.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.brix.budgetingapplication.models.IncomeCategory;

import java.util.List;

@Dao
public interface IncomeCatDao {

    @Insert
    void insert(IncomeCategory[] incomeCategories);

    @Query("SELECT * FROM incomeCat ORDER BY CategoryName")
    LiveData<List<IncomeCategory>> getData();

    @Delete
    int delete(IncomeCategory[] incomeCategories);

    @Update
    int update(IncomeCategory[] incomeCategories);

    @Query("SELECT * FROM incomeCat WHERE Id = :Id")
    IncomeCategory findById(int Id);
}
