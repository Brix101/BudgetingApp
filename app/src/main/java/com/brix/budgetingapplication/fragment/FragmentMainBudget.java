package com.brix.budgetingapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.UpdaterActivity;
import com.brix.budgetingapplication.adapters.BudgetTotalAdapter;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.persistance.Repository;
import com.brix.budgetingapplication.util.VerticalSpacingItemDecorator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentMainBudget extends Fragment implements BudgetTotalAdapter.onBudgetClickListener {

    private ArrayList<BudgetModel> mBudgetModels = new ArrayList<>();
    private RecyclerView BudgetRecyclerView;
    private Repository mRepository;
    BudgetTotalAdapter mBudgetTotalAdapter;
    TextView txtBudgetTotal,txtBudgetIncomeTotal,txtBudgetRemainTotal;
    double income;
    double budgettotal;
    DecimalFormat df2 = new DecimalFormat("#.##");
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main_budget,container,false);
        mRepository = new Repository(getActivity().getApplicationContext());
        txtBudgetTotal = (TextView)view.findViewById(R.id.txtBudgetTotal);
        txtBudgetIncomeTotal = (TextView)view.findViewById(R.id.txtBudgetIncomeTotal);
        txtBudgetRemainTotal = (TextView)view.findViewById(R.id.txtBudgetRemainTotal);
        initRecycler();
        retrieveData();
        FirstAndLastDate();
        return view;
    }

    public void FirstAndLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,  0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);

        mRepository.geTotalBudget().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    budgettotal = aDouble;
                    txtBudgetTotal.setText(df2.format(aDouble));
                }else {
                    budgettotal = 0;
                    txtBudgetTotal.setText("0");
                }
            }
        });

        mRepository.getIncomeWeekTotal(startDateStr,endDateStr).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    income = aDouble;
                    txtBudgetIncomeTotal.setText(df2.format(aDouble));
                }else {
                    income = 0;
                    txtBudgetIncomeTotal.setText("0");
                }
            }
        });
        txtBudgetTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = income - budgettotal;
                txtBudgetRemainTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtBudgetIncomeTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = income - budgettotal;
                txtBudgetRemainTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("System Message");
            builder.setCancelable(false);
            builder.setMessage("Would you like to Delete this Data?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    deleteBudget(mBudgetModels.get(viewHolder.getAdapterPosition()));
                    mBudgetTotalAdapter.notifyDataSetChanged();
                    double total = income - budgettotal;
                    txtBudgetRemainTotal.setText(df2.format(total));
                    Toast.makeText(getContext(),"Deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBudgetTotalAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private void deleteFinState(BudgetModel budgetModel){
        mBudgetModels.remove(budgetModel);
        mBudgetTotalAdapter.notifyDataSetChanged();
        mRepository.deleteBudget(budgetModel);
    }
    private void initRecycler(){
        BudgetRecyclerView = (RecyclerView) view.findViewById(R.id.BudgetRecyclerView);
        BudgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(BudgetRecyclerView);
        BudgetRecyclerView.addItemDecoration(itemDecorator);
        mBudgetTotalAdapter = new BudgetTotalAdapter(mBudgetModels,this::onBudgetClick);
        BudgetRecyclerView.setAdapter(mBudgetTotalAdapter);


    }

    private void retrieveData() {
        mRepository.getBudgetData().observe(getViewLifecycleOwner(), new Observer<List<BudgetModel>>() {
            @Override
            public void onChanged(List<BudgetModel> budgetModels) {
                if (mBudgetModels.size() > 0) {
                    mBudgetModels.clear();
                }
                if (budgetModels != null) {
                    mBudgetModels.addAll(budgetModels);
                }
                mBudgetTotalAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteBudget(BudgetModel budgetModel){
        mBudgetModels.remove(budgetModel);
        mBudgetTotalAdapter.notifyDataSetChanged();
        mRepository.deleteBudget(budgetModel);
    }
    @Override
    public void onBudgetClick(int position) {
        Intent intent = new Intent(getContext(), UpdaterActivity.class);
        intent.putExtra("SelectedBudgetItem",mBudgetModels.get(position));
        startActivity(intent);
    }
}
