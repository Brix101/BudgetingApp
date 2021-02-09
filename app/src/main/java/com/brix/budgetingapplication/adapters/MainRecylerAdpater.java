package com.brix.budgetingapplication.adapters;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.models.FinancialStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainRecylerAdpater extends RecyclerView.Adapter<MainRecylerAdpater.ViewHolder> {

    private ArrayList<FinancialStatement> mfinancialStatements = new ArrayList<>();
    private OnMainListerner OnMainListerner;

    public MainRecylerAdpater(ArrayList<FinancialStatement> financialStatements,OnMainListerner mOnMainListerner) {
        this.mfinancialStatements = financialStatements;
        this.OnMainListerner = mOnMainListerner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_display, parent, false);
        return new ViewHolder(view,OnMainListerner);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String resDate = mfinancialStatements.get(position).getDate();
        Date date1 = new Date(resDate);
        SimpleDateFormat formatNowDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatNowMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

        String Day = formatNowDay.format(date1);
        String Month = formatNowMonth.format(date1);
        String Year = formatNowYear.format(date1);

        holder.txtDate.setText(Day);
        holder.txtMonthYear.setText(Month+" "+Year);
        double ResAmount = mfinancialStatements.get(position).getAmount();
        holder.txtResult.setText(Double.toString(ResAmount));
        String CatType = mfinancialStatements.get(position).getCategoryType();
        holder.txtDailyCatTitle.setText(mfinancialStatements.get(position).getSubCategory());
        if(CatType.equals("Income")){
            holder.txtCategoryType.setText("Income");
            holder.txtCategoryType.setTextColor(Color.BLUE);
            holder.txtResult.setTextColor(Color.BLUE);
        }else {
            holder.txtCategoryType.setText("Expense");
            holder.txtCategoryType.setTextColor(Color.RED);
            holder.txtResult.setTextColor(Color.RED);
        }
    }
    @Override
    public int getItemCount() {
        return mfinancialStatements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDailyCatTitle ,txtResult,txtCategoryType,txtDate,txtMonthYear;
        OnMainListerner OnMainListerner;
        public ViewHolder(@NonNull View itemView,OnMainListerner OnMainListerner) {
            super(itemView);
            txtDailyCatTitle = itemView.findViewById(R.id.txtDailyCatTitle);
            txtResult = itemView.findViewById(R.id.txtResult);
            txtCategoryType = itemView.findViewById(R.id.txtCategoryType);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtMonthYear = itemView.findViewById(R.id.txtMonthYear);
            this.OnMainListerner = OnMainListerner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnMainListerner.onMainClick(getAdapterPosition());
        }
    }
    public interface OnMainListerner{
        void onMainClick(int position);
    }
}
