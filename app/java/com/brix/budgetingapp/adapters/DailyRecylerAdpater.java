package com.brix.budgetingapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapp.R;
import com.brix.budgetingapp.models.ExpenseCategory;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.persistance.BudgetDatabase;

import java.util.ArrayList;

public class DailyRecylerAdpater extends RecyclerView.Adapter<DailyRecylerAdpater.ViewHolder> {

    private ArrayList<FinancialStatement> mfinancialStatements = new ArrayList<>();



    public DailyRecylerAdpater(ArrayList<FinancialStatement> financialStatements) {
        this.mfinancialStatements = financialStatements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        double ResAmount = mfinancialStatements.get(position).getAmount();
        holder.txtResult.setText(Double.toString(ResAmount));
        holder.txtCategoryType.setText(mfinancialStatements.get(position).getCategoryType());
        holder.txtDailyCatTitle.setText(mfinancialStatements.get(position).getSubCategory());

    }

    @Override
    public int getItemCount() {
        return mfinancialStatements.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDailyCatTitle ,txtResult,txtCategoryType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDailyCatTitle = itemView.findViewById(R.id.txtDailyCatTitle);
            txtResult = itemView.findViewById(R.id.txtResult);
            txtCategoryType = itemView.findViewById(R.id.txtCategoryType);

        }


    }


}
