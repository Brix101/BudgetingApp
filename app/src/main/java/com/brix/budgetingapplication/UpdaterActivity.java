package com.brix.budgetingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brix.budgetingapplication.adapters.ExpenseCategoryAdapter;
import com.brix.budgetingapplication.adapters.IncomeCategoryAdapter;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.ExpenseCategory;
import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.models.IncomeCategory;
import com.brix.budgetingapplication.persistance.Repository;
import com.brix.budgetingapplication.util.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdaterActivity extends AppCompatActivity implements IncomeCategoryAdapter.OnInCatListener,ExpenseCategoryAdapter.OnExCatListener{

    private ExpenseCategoryAdapter mExpenseCategoryAdapter;
    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private ArrayList<IncomeCategory> mIncomeCategory = new ArrayList<>();
    private IncomeCategoryAdapter mIncomeCategoryAdapter;
    private RecyclerView recyclerUpdate;
    private Repository mRepository;
    EditText txtUpdateDate,txtUpdateCategory,txtUpdateAmount,txtUpdateDescription;
    LinearLayout btnUpdateSelector, UpdateRecycler;
    Button btnUpdate;
    ImageButton btnUpdateBack;
    TextView txtUpdateTitle,txtCategories;
    int mYear, mMonth, mDay;
    FinancialStatement InitialfinState;
    BudgetModel budgetModel;
    LinearLayout layoutCategories;
    int resId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mRepository = new Repository(this);
        recyclerUpdate = (RecyclerView) findViewById(R.id.recyclerUpdate);
        txtUpdateDate = (EditText) findViewById(R.id.txtUpdateDate);
        txtUpdateCategory = (EditText) findViewById(R.id.txtUpdateCategory);
        txtUpdateAmount = (EditText) findViewById(R.id.txtUpdateAmount);
        txtUpdateDescription = (EditText) findViewById(R.id.txtUpdateDescription);
        btnUpdateSelector = (LinearLayout) findViewById(R.id.btnUpdateSelector);
        UpdateRecycler = (LinearLayout) findViewById(R.id.UpdateRecycler);
        txtUpdateTitle = (TextView) findViewById(R.id.txtUpdateTitle);
        txtCategories = (TextView) findViewById(R.id.txtCategories);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdateBack = (ImageButton) findViewById(R.id.btnUpdateBack);
        layoutCategories = (LinearLayout) findViewById(R.id.layoutCategories);
        listeners();
        getData();
        Updater();
        if(txtUpdateTitle.getText().toString().equals("Income")){
            initIncomeRecycler();
            retrieveIncomeData();
        }else{
            initExpenseRecycler();
            retrieveExpenseData();
        }

    }
    private void getData(){
        if(getIntent().hasExtra("SelectedItem")){
            InitialfinState = getIntent().getParcelableExtra("SelectedItem");
            resId = InitialfinState.getId();
            txtUpdateTitle.setText(InitialfinState.getCategoryType());
            txtCategories.setText(InitialfinState.getCategoryType()+" Category");
            txtUpdateDate.setText(InitialfinState.getDate());
            txtUpdateCategory.setText(InitialfinState.getSubCategory());
            txtUpdateAmount.setText(String.valueOf(InitialfinState.getAmount()));
            txtUpdateDescription.setText(InitialfinState.getDescription());
        }
        if (getIntent().hasExtra("SelectedBudgetItem")){
            budgetModel = getIntent().getParcelableExtra("SelectedBudgetItem");
            resId = budgetModel.getId();
            txtUpdateTitle.setText("Budget");
            txtUpdateCategory.setText(budgetModel.getCategory());
            txtUpdateAmount.setText(String.valueOf(budgetModel.getBudgetAmount()));
            txtUpdateDate.setVisibility(View.GONE);
            txtUpdateDescription.setVisibility(View.GONE);
            txtUpdateCategory.setEnabled(false);
        }
    }
    public void Updater(){

        if(txtUpdateTitle.getText().toString().equals("Budget")){
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BudgetModel Update = mRepository.findBudgetById(resId);
                            Update.setCategory(txtUpdateDate.getText().toString());
                            Update.setBudgetAmount(Double.parseDouble(txtUpdateAmount.getText().toString()));
                            mRepository.updateBudget(Update);
                        }
                    });
                    thread.start();
                    Toast.makeText(getApplication().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    thread.interrupt();
                    btnUpdateBack.callOnClick();
                }
            });
        }else {
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FinancialStatement Update = mRepository.findById(resId);
                            Update.setDate(txtUpdateDate.getText().toString());
                            Update.setSubCategory(txtUpdateCategory.getText().toString());
                            Update.setAmount(Double.parseDouble(txtUpdateAmount.getText().toString()));
                            Update.setDescription(txtUpdateDescription.getText().toString());

                            mRepository.updateFinState(Update);
                        }
                    });
                    thread.start();
                    Toast.makeText(getApplication().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    thread.interrupt();
                    btnUpdateBack.callOnClick();
                }
            });
        }
    }
    public void initIncomeRecycler(){
        recyclerUpdate.setLayoutManager(new GridLayoutManager(this, 3));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerUpdate.addItemDecoration(itemDecorator);
        mIncomeCategoryAdapter = new IncomeCategoryAdapter(mIncomeCategory,this::onInCatClick);
        recyclerUpdate.setAdapter(mIncomeCategoryAdapter);
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
                mIncomeCategoryAdapter.notifyDataSetChanged();
            }
        });

    }
    public void initExpenseRecycler(){
        recyclerUpdate.setLayoutManager(new GridLayoutManager(this, 3));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerUpdate.addItemDecoration(itemDecorator);
        mExpenseCategoryAdapter = new ExpenseCategoryAdapter(mExpenseCategory,this::onExCatClick);
        recyclerUpdate.setAdapter(mExpenseCategoryAdapter);
    }

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
                mExpenseCategoryAdapter.notifyDataSetChanged();
            }
        });
    }
    public void listeners(){
        txtUpdateDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtUpdateDate.requestFocus();
                    getDatepicker();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(txtUpdateDate.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        txtUpdateCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateRecycler.setVisibility(View.VISIBLE);
                    btnUpdateSelector.setVisibility(View.GONE);
                    txtUpdateCategory.requestFocus();

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(txtUpdateCategory.getWindowToken(), 0);
                        return true;
                }
                return false;
            }
        });
        txtUpdateAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtUpdateAmount.requestFocus();
                    UpdateRecycler.setVisibility(View.GONE);
                    btnUpdateSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        txtUpdateDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtUpdateDescription.requestFocus();
                    UpdateRecycler.setVisibility(View.GONE);
                    btnUpdateSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        btnUpdateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public  void getDatepicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        String dateString = format.format(calendar.getTime());

                        txtUpdateDate.setText(dateString);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public static void showKeyboard(EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        });
    }
    @Override
    public void onInCatClick(int position) {
        txtUpdateCategory.setText(mIncomeCategory.get(position).getCategoryName());
        showKeyboard(txtUpdateAmount);
        UpdateRecycler.setVisibility(View.GONE);
        btnUpdateSelector.setVisibility(View.VISIBLE);
    }

    @Override
    public void onExCatClick(int position) {
        txtUpdateCategory.setText(mExpenseCategory.get(position).getCategoryName());
        showKeyboard(txtUpdateAmount);
        UpdateRecycler.setVisibility(View.GONE);
        btnUpdateSelector.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        btnUpdateBack.callOnClick();
        super.onBackPressed();
    }
}