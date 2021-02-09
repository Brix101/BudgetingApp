package com.brix.budgetingapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapp.MainActivity;
import com.brix.budgetingapp.R;
import com.brix.budgetingapp.adapters.IncomeCategoryAdapter;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;
import com.brix.budgetingapp.persistance.Repository;
import com.brix.budgetingapp.util.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentIncome extends Fragment implements IncomeCategoryAdapter.OnInCatListener {

    View view;
    private ArrayList<IncomeCategory> mIncomeCategory = new ArrayList<>();
    private IncomeCategoryAdapter mIncomeCategoryAdapter;
    private RecyclerView recyclerCategorySelector;
    Repository mRepository;
    EditText editTextDate, txtIncomeCategory,txtIncomeAmount,txtIncomeDescription;
    LinearLayout btnIncomeSelector, IncomelayoutSave,IncomelayoutUpDe,IncomeRecycler;
    int mYear, mMonth, mDay;
    Button btnIncomeSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_income,container,false);
        mRepository = new Repository(getContext());
        editTextDate = (EditText)view.findViewById(R.id.editTextDate);
        txtIncomeCategory = (EditText)view.findViewById(R.id.txtIncomeCategory);
        txtIncomeAmount = (EditText)view.findViewById(R.id.txtIncomeAmount);
        txtIncomeDescription = (EditText)view.findViewById(R.id.txtIncomeDescription);
        btnIncomeSelector = (LinearLayout)view.findViewById(R.id.btnIncomeSelector);
        IncomelayoutSave = (LinearLayout)view.findViewById(R.id.IncomelayoutSave);
        IncomelayoutUpDe = (LinearLayout)view.findViewById(R.id.IncomelayoutUpDe);
        IncomeRecycler = (LinearLayout)view.findViewById(R.id.IncomeRecycler);
        btnIncomeSave = (Button)view.findViewById(R.id.btnIncomeSave);
        initIncomeRecycler();
        retrieveIncomeData();
        setOnTouchListener();
        IncomeRecycler.setVisibility(View.INVISIBLE);
        getLatestDate();
        return view;
    }
    public void setOnTouchListener(){
        editTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    getDatepicker(getContext());
                    return true;
                }
                return false;
            }
        });
        txtIncomeCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    IncomeRecycler.setVisibility(View.VISIBLE);
                    btnIncomeSelector.setVisibility(View.GONE);
                    txtIncomeCategory.requestFocus();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(getActivity().getCurrentFocus()!=null&& txtIncomeCategory.isFocused()){
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        txtIncomeAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtIncomeAmount.requestFocus();
                    IncomeRecycler.setVisibility(View.GONE);
                    btnIncomeSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        txtIncomeDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtIncomeDescription.requestFocus();
                    IncomeRecycler.setVisibility(View.GONE);
                    btnIncomeSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        btnIncomeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinancialStatement financialStatement = new FinancialStatement();
                financialStatement.setCategoryType("Income");
                financialStatement.setSubCategory(txtIncomeCategory.getText().toString());
                financialStatement.setAmount(Double.parseDouble(txtIncomeAmount.getText().toString()));
                financialStatement.setDescription(txtIncomeDescription.getText().toString());
                financialStatement.setDate(editTextDate.getText().toString());
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
        editTextDate.setText(dateOnly.format(cal.getTime()));
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
                        editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initIncomeRecycler(){
        recyclerCategorySelector = (RecyclerView)view.findViewById(R.id.recyclerCategorySelector);
        recyclerCategorySelector.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerCategorySelector.addItemDecoration(itemDecorator);
        mIncomeCategoryAdapter = new IncomeCategoryAdapter(mIncomeCategory,this::onInCatClick);
        recyclerCategorySelector.setAdapter(mIncomeCategoryAdapter);
    }
    private void retrieveIncomeData() {
        mRepository.getIncomeData().observe(getViewLifecycleOwner(), new Observer<List<IncomeCategory>>() {
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

    @Override
    public void onInCatClick(int position) {
        txtIncomeCategory.setText("");
        txtIncomeCategory.setText(mIncomeCategory.get(position).getCategoryName());
    }
}
