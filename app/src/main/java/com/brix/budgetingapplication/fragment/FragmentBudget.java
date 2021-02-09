package com.brix.budgetingapplication.fragment;

import android.app.Activity;
import android.content.Context;
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

import com.brix.budgetingapplication.MainActivity;
import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.adapters.ExpenseCategoryAdapter;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.ExpenseCategory;
import com.brix.budgetingapplication.persistance.Repository;
import com.brix.budgetingapplication.util.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentBudget extends Fragment implements ExpenseCategoryAdapter.OnExCatListener {

    private ExpenseCategoryAdapter mExpenseCategoryAdapter;
    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private RecyclerView recyclerCategorySelector;
    Repository mRepository;
    EditText txtBudgetCategory,txtBudgetAmount;
    LinearLayout btnBudgetSelector,BudgetRecycler;
    View view;
    int CategoryId;
    Intent intent;
    Button btnBudgetSave;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_budget,container,false);
        mRepository = new Repository(getContext());
        txtBudgetCategory = (EditText)view.findViewById(R.id.txtBudgetCategory);
        txtBudgetAmount = (EditText)view.findViewById(R.id.txtBudgetAmount);
        btnBudgetSelector = (LinearLayout)view.findViewById(R.id.btnBudgetSelector);
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
        btnBudgetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtBudgetCategory.length()!=0 && txtBudgetAmount.length()!=0){
                    BudgetModel budgetModel = new BudgetModel();
                    MainActivity.newUpdate.setCategory(txtBudgetCategory.getText().toString());
                    MainActivity.newUpdate.setBudgetAmount(Double.parseDouble(txtBudgetAmount.getText().toString()));

                    getActivity().finish();
                    intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("BudgetInsert", budgetModel);
                    startActivity(intent);

                }else if(txtBudgetCategory.length()==0){
                    BudgetRecycler.setVisibility(View.VISIBLE);
                    btnBudgetSelector.setVisibility(View.GONE);
                    txtBudgetCategory.requestFocus();
                    if(getActivity().getCurrentFocus()!=null&& txtBudgetCategory.isFocused()){
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }else if(txtBudgetAmount.length()==0){
                    showKeyboard(txtBudgetAmount);
                    BudgetRecycler.setVisibility(View.GONE);
                    btnBudgetSelector.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getExpenseData(String sub){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,  0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);

        mRepository.getExpenseBudgetTotal(startDateStr,endDateStr,sub).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    MainActivity.newUpdate.setExpenseAmount(aDouble);
                }
            }
        });
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
    public void onExCatClick(int position) {
        CategoryId = mExpenseCategory.get(position).getId();
        txtBudgetCategory.setText("");
        txtBudgetCategory.setText(mExpenseCategory.get(position).getCategoryName());
        getExpenseData(mExpenseCategory.get(position).getCategoryName());
        showKeyboard(txtBudgetAmount);
        BudgetRecycler.setVisibility(View.GONE);
        btnBudgetSelector.setVisibility(View.VISIBLE);
    }
}
