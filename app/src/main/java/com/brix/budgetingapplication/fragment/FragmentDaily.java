package com.brix.budgetingapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapplication.MainActivity;
import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.UpdaterActivity;
import com.brix.budgetingapplication.adapters.MainRecylerAdpater;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.models.FinancialStatement;
import com.brix.budgetingapplication.persistance.Repository;
import com.brix.budgetingapplication.util.VerticalSpacingItemDecorator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentDaily extends Fragment implements MainRecylerAdpater.OnMainListerner {

    private RecyclerView CalendarReview;
    private ArrayList<FinancialStatement> mFinancialStatements = new ArrayList<>();
    private MainRecylerAdpater mDailyRecylerAdpater;
    private Repository mRepository;
    DecimalFormat df2 = new DecimalFormat("#.##");
    CalendarView calendarView;
    TextView txtExpenseTotal2,txtIncomeTotal2,txtTotal2;
    double incometotal,expensetotal;
    LinearLayout totalViewer2;
    BudgetModel newUpdate = new BudgetModel();
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily,container,false);
        mRepository = new Repository(getActivity().getApplicationContext());
        calendarView = (CalendarView)view.findViewById(R.id.calendarView);
        txtExpenseTotal2 = (TextView) view.findViewById(R.id.txtExpenseTotal2);
        txtIncomeTotal2 = (TextView) view.findViewById(R.id.txtIncomeTotal2);
        txtTotal2 = (TextView) view.findViewById(R.id.txtTotal2);
        totalViewer2 =(LinearLayout)view.findViewById(R.id.totalViewer2);
        initRecycler();
        retrieveData(getLatestDate());
        setTotal(getLatestDate());
        getCalendarDate();
        return view;
    }

    public void setTotal(String date){
        mRepository.getIncomeTotal(date).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    incometotal = aDouble;
                    txtIncomeTotal2.setText(df2.format(aDouble));
                    totalViewer2.setVisibility(View.VISIBLE);
                }else {
                    incometotal = 0;
                    txtIncomeTotal2.setText("0");
                }
            }
        });
        mRepository.getExpenseTotal(date).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble!=null){
                    expensetotal = aDouble;
                    txtExpenseTotal2.setText(df2.format(aDouble));
                    totalViewer2.setVisibility(View.VISIBLE);
                }else {
                    expensetotal = 0;
                    txtExpenseTotal2.setText("0");
                }
            }
        });
        txtExpenseTotal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total = incometotal - expensetotal;
                txtTotal2.setText(df2.format(total));
            }
        });
        txtIncomeTotal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total = incometotal - expensetotal;
                txtTotal2.setText(df2.format(total));
            }
        });
    }

    private void initRecycler(){
        CalendarReview = (RecyclerView) view.findViewById(R.id.CalendarReview);
        CalendarReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(CalendarReview);
        CalendarReview.addItemDecoration(itemDecorator);
        mDailyRecylerAdpater = new MainRecylerAdpater(mFinancialStatements,this::onMainClick);
        CalendarReview.setAdapter(mDailyRecylerAdpater);

    }

    public void getCalendarDate() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = format.format(calendar.getTime());
                retrieveData(dateString);
                setTotal(dateString);

            }
        });
    }

    public String getLatestDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        return dateOnly.format(cal.getTime());
    }

    private void retrieveData(String date) {
        mRepository.getDateSelector(date).observe(getViewLifecycleOwner(), new Observer<List<FinancialStatement>>() {
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
                    deleteFinState(mFinancialStatements.get(viewHolder.getAdapterPosition()));
                    mDailyRecylerAdpater.notifyDataSetChanged();
                    double total = incometotal - expensetotal;
                    txtTotal2.setText(df2.format(total));
                    Toast.makeText(getContext(),"Deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDailyRecylerAdpater.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private void deleteFinState(FinancialStatement financialStatement){
        mFinancialStatements.remove(financialStatement);
        mDailyRecylerAdpater.notifyDataSetChanged();
        mRepository.deleteFinState(financialStatement);

        mRepository.updateTotalExpense(financialStatement.getSubCategory()).observe(getViewLifecycleOwner(), new Observer<BudgetModel>() {
            @Override
            public void onChanged(BudgetModel budgetModel) {
                if (budgetModel != null) {
                    MainActivity.newUpdate.setId(budgetModel.getId());
                    MainActivity.newUpdate.setCategory(budgetModel.getCategory());
                    MainActivity.newUpdate.setBudgetAmount(budgetModel.getBudgetAmount());
                    MainActivity.newUpdate.setExpenseAmount(budgetModel.getExpenseAmount() - financialStatement.getAmount());
                    double total = budgetModel.getExpenseAmount()-financialStatement.getAmount();
                }
            }
        });
//        For sending data
        Intent intent = new Intent("Budget").putExtra("Budget", newUpdate);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onMainClick(int position) {
        Intent intent = new Intent(getContext(), UpdaterActivity.class);
        intent.putExtra("SelectedItem",mFinancialStatements.get(position));
        startActivity(intent);
    }
}
