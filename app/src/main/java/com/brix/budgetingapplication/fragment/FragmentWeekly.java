package com.brix.budgetingapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentWeekly extends Fragment implements MainRecylerAdpater.OnMainListerner {

    private ArrayList<FinancialStatement> mFinancialStatements = new ArrayList<>();
    private RecyclerView WeeklyReview;
    private MainRecylerAdpater mDailyRecylerAdpater;
    private Repository mRepository;
    private static final String TAG = "FragmentWeekly";
    TextView txtweek,txtIncomeWeekTotal,txtExpenseWeekTotal,txtWeekTotal;
    ImageButton btnWeekBackward,btnWeekForward;
    double incometotal;
    double expensetotal;
    Calendar calendar = Calendar.getInstance();
    DecimalFormat df2 = new DecimalFormat("#.##");
    int Counter;
    View view;
    BudgetModel newUpdate = new BudgetModel();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_weekly,container,false);
        mRepository = new Repository(getActivity().getApplicationContext());
        txtweek = (TextView) view.findViewById(R.id.txtweek);
        txtIncomeWeekTotal= (TextView)view.findViewById(R.id.txtIncomeWeekTotal);
        txtExpenseWeekTotal= (TextView)view.findViewById(R.id.txtExpenseWeekTotal);
        txtWeekTotal= (TextView)view.findViewById(R.id.txtWeekTotal);
        btnWeekBackward = (ImageButton)view.findViewById(R.id.btnWeekBackward);
        btnWeekForward = (ImageButton)view.findViewById(R.id.btnWeekForward);
        initRecycler();
        Counter = Calendar.DAY_OF_WEEK;
        getCurrentWeek(Calendar.DAY_OF_WEEK);
        listener();


        return view;
    }
    public void listener(){
        btnWeekBackward.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Counter>=2){
                    Counter --;
                }else {
                    Counter = 52;
                }
                getCurrentWeek(Counter);
            }
        });
        btnWeekForward.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Counter<=51){
                    Counter ++;
                }else {
                    Counter = 1;
                }
                getCurrentWeek(Counter);
            }
        });
    }

    private void initRecycler(){
        WeeklyReview = (RecyclerView) view.findViewById(R.id.WeeklyReview);
        WeeklyReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(WeeklyReview);
        WeeklyReview.addItemDecoration(itemDecorator);
        mDailyRecylerAdpater = new MainRecylerAdpater(mFinancialStatements,this::onMainClick);
        WeeklyReview.setAdapter(mDailyRecylerAdpater);

    }
    public void setTotal(String date1,String date2){

        mRepository.getExpenseWeekTotal(date1,date2).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    expensetotal = aDouble;
                    txtExpenseWeekTotal.setText(df2.format(aDouble));
                }else {
                    expensetotal = 0;
                    txtExpenseWeekTotal.setText("0");
                }
            }
        });
        mRepository.getIncomeWeekTotal(date1,date2).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble !=null){
                    incometotal = aDouble;
                    txtIncomeWeekTotal.setText(df2.format(aDouble));
                }else {
                    incometotal = 0;
                    txtIncomeWeekTotal.setText("0");
                }
            }
        });
        txtExpenseWeekTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incometotal - expensetotal;
                txtWeekTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtIncomeWeekTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double total = incometotal - expensetotal;
                txtWeekTotal.setText(df2.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCurrentWeek(int week){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM dd");

        calendar.setWeekDate(2021,week,1);
        Date monthFirstDay = calendar.getTime();
        calendar.setWeekDate(2021,week,7);
        Date monthLastDay = calendar.getTime();

        String startDateStr = format.format(monthFirstDay);
        String endDateStr = format.format(monthLastDay);
        txtweek.setText(format2.format(monthFirstDay)+" ~ "+format2.format(monthLastDay));
        retrieveData(startDateStr,endDateStr);
        setTotal(startDateStr,endDateStr);
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
                    double total = incometotal - expensetotal;
                    txtWeekTotal.setText(df2.format(total));
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
