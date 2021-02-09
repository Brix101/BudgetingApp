package com.brix.budgetingapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragmentMonthly extends Fragment implements MainRecylerAdpater.OnMainListerner {

    private ArrayList<FinancialStatement> mFinancialStatements = new ArrayList<>();
    private RecyclerView MonthlyReview;
    private MainRecylerAdpater mDailyRecylerAdpater;
    private Repository mRepository;
    ImageButton btnMonthForward,btnMonthBackward;
    TextView txtMonth,txtExpenseMonthlyTotal,txtIncomeMonthlyTotal,txtMonthlyTotal;
    View view;
    int Counter;
    double incomeTotal;
    double expenseTotal;
    BudgetModel newUpdate = new BudgetModel();
    DecimalFormat df2 = new DecimalFormat("#.##");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_monthly,container,false);
        mRepository = new Repository(getActivity().getApplicationContext());
        btnMonthForward = (ImageButton) view.findViewById(R.id.btnMonthForward);
        btnMonthBackward = (ImageButton) view.findViewById(R.id.btnMonthBackward);
        txtMonth = (TextView) view.findViewById(R.id.txtMonth);
        txtExpenseMonthlyTotal = (TextView) view.findViewById(R.id.txtExpenseMonthlyTotal);
        txtIncomeMonthlyTotal = (TextView) view.findViewById(R.id.txtIncomeMonthlyTotal);
        txtMonthlyTotal = (TextView) view.findViewById(R.id.txtMonthlyTotal);
        initRecycler();
        FirstAndLastDate();
        CounterSetter();
        listener();
        return view;
    }

    private void initRecycler(){
        MonthlyReview = (RecyclerView) view.findViewById(R.id.MonthlyReview);
        MonthlyReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(MonthlyReview);
        MonthlyReview.addItemDecoration(itemDecorator);
        mDailyRecylerAdpater = new MainRecylerAdpater(mFinancialStatements, this);
        MonthlyReview.setAdapter(mDailyRecylerAdpater);
    }

    private void listener(){
        btnMonthBackward.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Counter>=2){
                    Counter --;
                }else {
                    Counter = 12;
                }
                withmonthSelector(Counter);
            }
        });
        btnMonthForward.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Counter<=11){
                    Counter ++;
                }else {
                    Counter = 1;
                }
                withmonthSelector(Counter);
            }
        });
        txtExpenseMonthlyTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incomeTotal - expenseTotal;
                txtMonthlyTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtExpenseMonthlyTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incomeTotal - expenseTotal;
                txtMonthlyTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CounterSetter(){
        LocalDate todaydate = LocalDate.now();
        Counter = Integer.parseInt(String.valueOf(todaydate.getMonthValue()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void withmonthSelector(int month){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM");
        LocalDate todaydate = LocalDate.now();
        String startDateStr = formatter.format(todaydate.withMonth(month).withDayOfMonth(1));
        String endDateStr = formatter.format(todaydate.withMonth(month).withDayOfMonth(todaydate.withMonth(month).lengthOfMonth()));
        String dMonth = formatter2.format(todaydate.withMonth(month));
        retrieveData(startDateStr,endDateStr);
        setTotal(startDateStr,endDateStr);
        txtMonth.setText(dMonth);
    }


    public void FirstAndLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,  0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MMMM");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        String month = df2.format(monthFirstDay);

        retrieveData(startDateStr,endDateStr);
        setTotal(startDateStr,endDateStr);
        txtMonth.setText(month);

    }
    public void setTotal(String date1,String date2){
        mRepository.getExpenseWeekTotal(date1,date2).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    expenseTotal = aDouble;
                    txtExpenseMonthlyTotal.setText(df2.format(aDouble));
                }else {
                    expenseTotal = 0;
                    txtExpenseMonthlyTotal.setText("0");
                }
            }
        });
        mRepository.getIncomeWeekTotal(date1,date2).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    incomeTotal = aDouble;
                    txtIncomeMonthlyTotal.setText(df2.format(aDouble));
                }else {
                    incomeTotal = 0;
                    txtIncomeMonthlyTotal.setText("0");
                }
            }
        });
        txtExpenseMonthlyTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incomeTotal - expenseTotal;
                txtMonthlyTotal.setText(df2.format(total));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtIncomeMonthlyTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incomeTotal - expenseTotal;
                txtMonthlyTotal.setText(df2.format(total));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void retrieveData(String date1,String date2) {
        mRepository.getWeekSelector(date1,date2).observe(getViewLifecycleOwner(), new Observer<List<FinancialStatement>>() {
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
                    double total = incomeTotal - expenseTotal;
                    txtMonthlyTotal.setText(df2.format(total));
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
