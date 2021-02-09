package com.brix.budgetingapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapp.MainActivity;
import com.brix.budgetingapp.R;
import com.brix.budgetingapp.SecondActivity;
import com.brix.budgetingapp.adapters.ExpenseCategoryAdapter;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;
import com.brix.budgetingapp.persistance.Repository;
import com.brix.budgetingapp.util.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentExpense extends Fragment implements ExpenseCategoryAdapter.OnExCatListener {

    private static final String TAG = "FragmentExpense";
    View view;
    private ExpenseCategoryAdapter mExpenseCategoryAdapter;
    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private RecyclerView recyclerCategorySelector;
    Repository mRepository;
    EditText txtExpenseDate,txtExpenseCategory,txtExpenseAmount,txtExpenseDescription;
    LinearLayout btnExpenseSelector, ExpenselayoutSave,ExpenselayoutUpDe,ExpenseRecycler;
    Button btnExpenseSave,btnExpenseUpdate,btnExpenseDelete;
    int mYear, mMonth, mDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expense, container, false);
        mRepository = new Repository(getContext());
        txtExpenseDate = (EditText)view.findViewById(R.id.txtExpenseDate);
        txtExpenseCategory = (EditText)view.findViewById(R.id.txtExpenseCategory);
        txtExpenseAmount = (EditText)view.findViewById(R.id.txtExpenseAmount);
        txtExpenseDescription = (EditText)view.findViewById(R.id.txtExpenseDescription);
        btnExpenseSelector = (LinearLayout)view.findViewById(R.id.btnExpenseSelector);
        ExpenselayoutSave = (LinearLayout)view.findViewById(R.id.ExpenselayoutSave);
        ExpenselayoutUpDe = (LinearLayout)view.findViewById(R.id.ExpenselayoutUpDe);
        ExpenseRecycler = (LinearLayout)view.findViewById(R.id.ExpenseRecycler);
        btnExpenseSave = (Button)view.findViewById(R.id.btnExpenseSave);
        btnExpenseUpdate = (Button)view.findViewById(R.id.btnExpenseUpdate);
        btnExpenseDelete = (Button)view.findViewById(R.id.btnExpenseDelete);
        initExpenseRecycler();
        retrieveExpenseData();
        listeners();
        ExpenseRecycler.setVisibility(View.INVISIBLE);
        getLatestDate();
        return view;
    }
    public void listeners(){
        txtExpenseDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    getDatepicker(getContext());
                    return true;
                }
                return false;
            }
        });
        txtExpenseCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    ExpenseRecycler.setVisibility(View.VISIBLE);
                    btnExpenseSelector.setVisibility(View.GONE);
                    txtExpenseCategory.requestFocus();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(getActivity().getCurrentFocus()!=null&& txtExpenseCategory.isFocused()){
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        txtExpenseAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtExpenseAmount.requestFocus();
                    ExpenseRecycler.setVisibility(View.GONE);
                    btnExpenseSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        txtExpenseDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtExpenseDescription.requestFocus();
                    ExpenseRecycler.setVisibility(View.GONE);
                    btnExpenseSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        btnExpenseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinancialStatement financialStatement = new FinancialStatement();
                financialStatement.setCategoryType("Expense");
                financialStatement.setSubCategory(txtExpenseCategory.getText().toString());
                financialStatement.setAmount(Double.parseDouble(txtExpenseAmount.getText().toString()));
                financialStatement.setDescription(txtExpenseDescription.getText().toString());
                financialStatement.setDate(txtExpenseDate.getText().toString());
                mRepository.insertMainTask(financialStatement);
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void getLatestDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("d/M/yyyy");
        txtExpenseDate.setText(dateOnly.format(cal.getTime()));
    }
    public  void getDatepicker(Context context){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtExpenseDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void initExpenseRecycler(){
        recyclerCategorySelector = (RecyclerView)view.findViewById(R.id.recyclerCategorySelector);
        recyclerCategorySelector.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerCategorySelector.addItemDecoration(itemDecorator);
        mExpenseCategoryAdapter = new ExpenseCategoryAdapter(mExpenseCategory,this::onExCatClick);
        recyclerCategorySelector.setAdapter(mExpenseCategoryAdapter);
    }

    //    For Retreiving Data
    private void retrieveExpenseData() {
        mRepository.getExpenseData().observe(getViewLifecycleOwner(), new Observer<List<ExpenseCategory>>() {
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

    @Override
    public void onExCatClick(int position) {
        txtExpenseCategory.setText("");
        txtExpenseCategory.setText(mExpenseCategory.get(position).getCategoryName());
    }
}
