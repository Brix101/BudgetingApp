package com.brix.budgetingapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.brix.budgetingapp.adapters.ExpenseCategoryAdapter;
import com.brix.budgetingapp.adapters.IncomeCategoryAdapter;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.models.IncomeCategory;
import com.brix.budgetingapp.persistance.Repository;
import com.brix.budgetingapp.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class FragmentBudget extends Fragment implements ExpenseCategoryAdapter.OnExCatListener {

    private static final String TAG = "FragmentBudget";
    View view;
    private ExpenseCategoryAdapter mExpenseCategoryAdapter;
    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private RecyclerView recyclerCategorySelector;
    Repository mRepository;
    EditText txtBudgetCategory,txtBudgetAmount,txtBudgetDescription;
    LinearLayout btnBudgetSelector, BudgetlayoutSave,BudgetlayoutUpDe,BudgetRecycler;
    int CategoryId;
    Button btnBudgetSave;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_budget,container,false);
        mRepository = new Repository(getContext());
        txtBudgetCategory = (EditText)view.findViewById(R.id.txtBudgetCategory);
        txtBudgetAmount = (EditText)view.findViewById(R.id.txtBudgetAmount);
        txtBudgetDescription = (EditText)view.findViewById(R.id.txtBudgetDescription);
        btnBudgetSelector = (LinearLayout)view.findViewById(R.id.btnBudgetSelector);
        BudgetlayoutSave = (LinearLayout)view.findViewById(R.id.BudgetlayoutSave);
        BudgetlayoutUpDe = (LinearLayout)view.findViewById(R.id.BudgetlayoutUpDe);
        BudgetRecycler = (LinearLayout)view.findViewById(R.id.BudgetRecycler);
        btnBudgetSave = (Button)view.findViewById(R.id.btnBudgetSave);
        initExpenseRecycler();
        retrieveExpenseData();
        setOnTouchListener();
        BudgetRecycler.setVisibility(View.INVISIBLE);
        return view;
    }
    public void setOnTouchListener(){
        txtBudgetCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    BudgetRecycler.setVisibility(View.VISIBLE);
                    btnBudgetSelector.setVisibility(View.GONE);
                    txtBudgetCategory.requestFocus();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(getActivity().getCurrentFocus()!=null&& txtBudgetCategory.isFocused()){
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        txtBudgetAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtBudgetAmount.requestFocus();
                    BudgetRecycler.setVisibility(View.GONE);
                    btnBudgetSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        txtBudgetDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtBudgetDescription.requestFocus();
                    BudgetRecycler.setVisibility(View.GONE);
                    btnBudgetSelector.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
//        btnIncomeSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FinancialStatement financialStatement = new FinancialStatement();
//                financialStatement.setCategoryType("Income");
//                financialStatement.setSubCategoryID(CategoryId);
//                financialStatement.setAmount(Double.parseDouble(txtBudgetAmount.getText().toString()));
//                financialStatement.setDescription(txtBudgetDescription.getText().toString());
//                mRepository.insertMainTask(financialStatement);
//                getActivity().finish();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

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
        CategoryId = mExpenseCategory.get(position).getId();
        txtBudgetCategory.setText("");
        txtBudgetCategory.setText(mExpenseCategory.get(position).getCategoryName());
    }
}
