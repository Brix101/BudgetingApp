package com.brix.budgetingapplication.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.FinancialStatement;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert
    void insert(BudgetModel[] expenseCategories);

    @Delete
    int delete(BudgetModel[] expenseCategories);

    @Update
    int update(BudgetModel[] expenseCategories);

    @Query("SELECT * FROM moneyBudget")
    LiveData<List<BudgetModel>> getBudgetData();

    @Query("SELECT * FROM moneyBudget WHERE Category =:category")
    LiveData<BudgetModel> updateTotalExpense(String category);


    @Query("SELECT SUM(BudgetAmount) FROM moneyBudget")
    LiveData<Double> geTotalBudget();

    @Query("SELECT * FROM moneyBudget WHERE Id = :Id")
    BudgetModel findBudgetById(int Id);
}
