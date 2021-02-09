package com.brix.budgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brix.budgetingapp.adapters.DailyRecylerAdpater;
import com.brix.budgetingapp.adapters.ExpenseRecyclerAdapter;
import com.brix.budgetingapp.adapters.IncomeRecyclerAdapter;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;
import com.brix.budgetingapp.persistance.BudgetDatabase;
import com.brix.budgetingapp.persistance.Repository;
import com.brix.budgetingapp.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements ExpenseRecyclerAdapter.OnExpenseListener, IncomeRecyclerAdapter.OnIncomeListener {

    Intent intent;
    TextView txtTitle;
    RecyclerView CategoryRecycler;
    LinearLayout CategoryHidden;
    ImageButton btnAddCat, btnCancelCat;
    EditText txtCategoryAdd;
    public static String cattype;
    String isUpdate = "New";
    Button btnSave;
    private static final String TAG = "CategoryActivity";
    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private ExpenseRecyclerAdapter mExpenseRecyclerAdapter;
    private ArrayList<IncomeCategory> mIncomeCategory = new ArrayList<>();
    private IncomeRecyclerAdapter mIncomeRecyclerAdapter;
    Repository mRepository;
    String mIntialExpense,mInitialIncome;
    int exId,inId;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        txtTitle = findViewById(R.id.txtTitle);
        CategoryRecycler = (RecyclerView)findViewById(R.id.CategoryRecycler);
        CategoryHidden = findViewById(R.id.CategoryHidden);
        btnAddCat = findViewById(R.id.btnAddCat);
        btnCancelCat = findViewById(R.id.btnCancelCat);
        txtCategoryAdd = findViewById(R.id.txtCategoryAdd);
	    mRepository = new Repository(this);
	    btnSave = findViewById(R.id.btnSave);

        txtTitle.setText(cattype +" Category");

        if(cattype =="Expense"){
            initExpenseRecycler();
            retrieveExpenseData();
        }else if(cattype =="Income"){
            initIncomeRecycler();
            retrieveIncomeData();
        }

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAddCat:
                btnAddCat.setVisibility(View.GONE);
                btnCancelCat.setVisibility(View.VISIBLE);
                CategoryRecycler.setVisibility(View.GONE);
                CategoryHidden.setVisibility(View.VISIBLE);
                if(isUpdate=="Update"){
                    txtTitle.setText(isUpdate+" "+cattype +" Category");
                    btnSave.setText("Update");
                }else {
                    txtTitle.setText(isUpdate+" "+cattype +" Category");
                    btnSave.setText("Save");
                }
                break;
            case R.id.btnCancelCat:
                InputMethodManager img= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                img.hideSoftInputFromWindow(txtCategoryAdd.getWindowToken(),0);
                btnAddCat.setVisibility(View.VISIBLE);
                btnCancelCat.setVisibility(View.GONE);
                CategoryRecycler.setVisibility(View.VISIBLE);
                CategoryHidden.setVisibility(View.GONE);
                break;
            case R.id.btnSave:
                if(isUpdate=="Update"){
                    if(cattype =="Expense"){
                        if(mIntialExpense != txtCategoryAdd.getText().toString()){
                            updateExpense();
                        }
                    }else if(cattype =="Income"){
                        if(mInitialIncome != txtCategoryAdd.getText().toString()){
                            updateIncome();
                        }
                    }
                }else {
                    if(cattype =="Expense"){
                        ExpenseCategory _new = new ExpenseCategory(txtCategoryAdd.getText().toString());
                        InsertExpense insertExpense = new InsertExpense();
                        insertExpense.execute(_new);
                    }else if(cattype =="Income"){
                        IncomeCategory _new = new IncomeCategory(txtCategoryAdd.getText().toString());
                        InsertIncome insertIncome = new InsertIncome();
                        insertIncome.execute(_new);
                    }
                    Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                }
                txtCategoryAdd.setText("");
                btnCancelCat.callOnClick();
                isUpdate ="New";
                txtTitle.setText(cattype +" Category");
                break;
            default:
                break;
        }
    }

    public void updateExpense(){

      Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
              ExpenseCategory expenseCategory =  BudgetDatabase.getInstance(getApplicationContext()).expenseCatDao().findById(exId);
              expenseCategory.setCategoryName(txtCategoryAdd.getText().toString());
              mRepository.updateExpenseTask(expenseCategory);
            }
        });

      thread.start();
        Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show();
      thread.interrupt();

    }
    public void updateIncome(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                IncomeCategory incomeCategory = BudgetDatabase.getInstance(getApplicationContext()).incomeCatDao().findById(inId);
                incomeCategory.setCategoryName(txtCategoryAdd.getText().toString());
                mRepository.updateIncomeTask(incomeCategory);
            }
        });
        thread.start();
        Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show();
        thread.interrupt();
    }


    @Override
    public void onExpenseClick(int position) {
        String res = mExpenseCategory.get(position).getCategoryName();
        exId = mExpenseCategory.get(position).getId();
        txtCategoryAdd.setText(res);
        mIntialExpense = res;
        isUpdate = "Update";
        btnAddCat.callOnClick();
    }
    @Override
    public void onIncomeClick(int position) {
        String res = mIncomeCategory.get(position).getCategoryName();
        inId = mIncomeCategory.get(position).getId();
        txtCategoryAdd.setText(res);
        mInitialIncome = res;
        isUpdate = "Update";
        btnAddCat.callOnClick();
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
            builder.setTitle("System Message");
            builder.setMessage("Would you like to Delete this Data?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(cattype =="Expense"){
                        deleteExpenseData(mExpenseCategory.get(viewHolder.getAdapterPosition()));
                    }else if(cattype =="Income"){
                        deleteIncomeData(mIncomeCategory.get(viewHolder.getAdapterPosition()));
                    }
                    if(cattype =="Expense"){
                        mExpenseRecyclerAdapter.notifyDataSetChanged();
                    }else if(cattype =="Income"){
                        mIncomeRecyclerAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(CategoryActivity.this,"Deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(cattype =="Expense"){
                        mExpenseRecyclerAdapter.notifyDataSetChanged();
                    }else if(cattype =="Income"){
                        mIncomeRecyclerAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };
    private void deleteExpenseData(ExpenseCategory expenseCategory){
        mExpenseCategory.remove(expenseCategory);
        mExpenseRecyclerAdapter.notifyDataSetChanged();
        mRepository.deleteExpenseTask(expenseCategory);
    }
    private void deleteIncomeData(IncomeCategory incomeCategory){
        mIncomeCategory.remove(incomeCategory);
        mIncomeRecyclerAdapter.notifyDataSetChanged();
        mRepository.deleteIncomeTask(incomeCategory);
    }



//    RecyclerInitializers
    private void initExpenseRecycler(){
        CategoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        CategoryRecycler.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(CategoryRecycler);
        mExpenseRecyclerAdapter = new ExpenseRecyclerAdapter(mExpenseCategory,this::onExpenseClick);
        CategoryRecycler.setAdapter(mExpenseRecyclerAdapter);
    }
    private void initIncomeRecycler(){
        CategoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        CategoryRecycler.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(CategoryRecycler);
        mIncomeRecyclerAdapter = new IncomeRecyclerAdapter(mIncomeCategory,this::onIncomeClick);
        CategoryRecycler.setAdapter(mIncomeRecyclerAdapter);
    }

//    For Retreiving Data
    private void retrieveExpenseData() {
        mRepository.getExpenseData().observe(this, new Observer<List<ExpenseCategory>>() {
            @Override
            public void onChanged(List<ExpenseCategory> expenseCategories) {
                if(mExpenseCategory.size() > 0){
                    mExpenseCategory.clear();
                }
                if(expenseCategories != null){
                    mExpenseCategory.addAll(expenseCategories);
                }
                mExpenseRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
    private void retrieveIncomeData() {
       mRepository.getIncomeData().observe(this, new Observer<List<IncomeCategory>>() {
           @Override
           public void onChanged(List<IncomeCategory> incomeCategories) {
               if(mIncomeCategory.size() > 0){
                   mIncomeCategory.clear();
               }
               if(incomeCategories != null){
                   mIncomeCategory.addAll(incomeCategories);
               }
               mIncomeRecyclerAdapter.notifyDataSetChanged();
           }
       });

    }

    // For Inserting Data
    class InsertExpense extends AsyncTask<ExpenseCategory,Void,Void>{

        @Override
        protected Void doInBackground(ExpenseCategory... expenseCategories) {
            BudgetDatabase.getInstance(getApplicationContext()).expenseCatDao().insert(expenseCategories);
            return null;
        }
    }
    class InsertIncome extends AsyncTask<IncomeCategory,Void,Void>{

        @Override
        protected Void doInBackground(IncomeCategory... incomeCategories) {
            BudgetDatabase.getInstance(getApplicationContext()).incomeCatDao().insert(incomeCategories);
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        findViewById(R.id.btnBack).callOnClick();
    }

}