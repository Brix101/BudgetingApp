package com.brix.budgetingapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.models.BudgetModel;

import java.util.ArrayList;

public class BudgetTotalAdapter  extends RecyclerView.Adapter<BudgetTotalAdapter.Viewholder> {

    private ArrayList<BudgetModel> mBudgetModels = new ArrayList<>();
    private onBudgetClickListener onBudgetClickListener;

    public BudgetTotalAdapter(ArrayList<BudgetModel> mBudgetModels,onBudgetClickListener onBudgetClickListener) {
        this.mBudgetModels = mBudgetModels;
        this.onBudgetClickListener = onBudgetClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_total_budget, parent, false);
        return new Viewholder(view,onBudgetClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.txtResBudgetCategory.setText(mBudgetModels.get(position).getCategory()+" Budget");
        Log.d("TAG", "onBindViewHolder: "+mBudgetModels.get(position).getCategory());
        holder.txtResBudgetAmount.setText(String.valueOf(mBudgetModels.get(position).getBudgetAmount()));
        holder.txtTotalExpense.setText(String.valueOf(mBudgetModels.get(position).getExpenseAmount()));
        double b1 = mBudgetModels.get(position).getBudgetAmount();
        double b2 = mBudgetModels.get(position).getExpenseAmount();
        String total = String.valueOf(b1 - b2);
        holder.txtCatBudgetRemain.setText(total);
    }



    @Override
    public int getItemCount() {
        return mBudgetModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtResBudgetCategory,txtResBudgetAmount,txtTotalExpense,txtCatBudgetRemain;
        onBudgetClickListener onBudgetClickListener;

        public Viewholder(@NonNull View itemView,onBudgetClickListener onBudgetClickListener) {
            super(itemView);
            txtResBudgetCategory = itemView.findViewById(R.id.txtResBudgetCategory);
            txtResBudgetAmount = itemView.findViewById(R.id.txtResBudgetAmount);
            txtTotalExpense = itemView.findViewById(R.id.txtTotalExpense);
            txtCatBudgetRemain = itemView.findViewById(R.id.txtCatBudgetRemain);
            this.onBudgetClickListener = onBudgetClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBudgetClickListener.onBudgetClick(getAdapterPosition());
        }
    }
    public interface onBudgetClickListener{
        void onBudgetClick(int position);
    }
}
