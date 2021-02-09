package com.brix.budgetingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapp.R;
import com.brix.budgetingapp.adapters.DailyRecylerAdpater;
import com.brix.budgetingapp.adapters.ExpenseRecyclerAdapter;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.persistance.BudgetDatabase;
import com.brix.budgetingapp.persistance.Repository;
import com.brix.budgetingapp.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class FragmentDaily extends Fragment {
    private Context mContext;
    View view;
    private RecyclerView dailyReView;
    private ArrayList<FinancialStatement> mFinancialStatements = new ArrayList<>();
    private DailyRecylerAdpater mDailyRecylerAdpater;
    private Repository mRepository;

    private ArrayList<ExpenseCategory> mExpenseCategory = new ArrayList<>();
    private ExpenseRecyclerAdapter mExpenseRecyclerAdapter;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily,container,false);
        mRepository = new Repository(getActivity().getApplicationContext());
        initRecycler();
        retrieveData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initRecycler(){
        dailyReView = (RecyclerView) view.findViewById(R.id.dailyReView);
        dailyReView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        dailyReView.addItemDecoration(itemDecorator);
        mDailyRecylerAdpater = new DailyRecylerAdpater(mFinancialStatements);
        dailyReView.setAdapter(mDailyRecylerAdpater);
    }

    private void retrieveData() {
        mRepository.getFinStateData().observe(getViewLifecycleOwner(), new Observer<List<FinancialStatement>>() {
            @Override
            public void onChanged(List<FinancialStatement> financialStatements) {
                if(mFinancialStatements.size() > 0){
                    mFinancialStatements.clear();
                }
                if(financialStatements != null){
                    mFinancialStatements.addAll(financialStatements);
                }
                mDailyRecylerAdpater.notifyDataSetChanged();
            }
        });
    }
}
