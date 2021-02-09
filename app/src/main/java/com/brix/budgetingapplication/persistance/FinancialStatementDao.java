package com.brix.budgetingapplication.persistance;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.models.GraphData;


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

    @Query("SELECT * FROM financialState WHERE CategoryType !=\"Budget\" AND Date = :Date")
     LiveData<List<FinancialStatement>> getDateSelector(String Date);

    @Query("SELECT * FROM financialState WHERE Id = :Id")
    FinancialStatement findById(int Id);

    @Query("SELECT * FROM financialState WHERE  CategoryType !=\"Budget\" AND Date BETWEEN :date1 AND :date2 ORDER BY Date ASC")
    LiveData<List<FinancialStatement>> getWeekSelector(String date1,String date2);

    @Query("SELECT SUM(Amount) FROM financialState WHERE  CategoryType  = \"Expense\" AND  Date =:date")
    LiveData<Double> getExpenseTotal(String date);

    @Query("SELECT SUM(Amount) FROM financialState WHERE  CategoryType  = \"Income\" AND  Date =:date")
    LiveData<Double> getIncomeTotal(String date);

    @Query("SELECT SUM(Amount) FROM financialState WHERE  CategoryType  = \"Expense\" AND  Date BETWEEN :date1 AND :date2")
    LiveData<Double> getExpenseWeekTotal(String date1,String date2);

    @Query("SELECT SUM(Amount) FROM financialState WHERE  CategoryType  = \"Expense\" AND SubCategory = :sub AND  Date BETWEEN :date1 AND :date2")
    LiveData<Double> getExpenseBudgetTotal(String date1,String date2,String sub);

    @Query("SELECT SUM(Amount) FROM financialState WHERE  CategoryType  = \"Income\" AND  Date BETWEEN :date1 AND :date2")
    LiveData<Double> getIncomeWeekTotal(String date1,String date2);

    @Query("SELECT SubCategory as Category, SUM(Amount) as Amount FROM financialState WHERE  CategoryType  = \"Expense\" AND  Date BETWEEN :date1 AND :date2 GROUP BY SubCategory")
    LiveData<List<GraphData>> getExpenseTotal(String date1, String date2);
}
