package com.brix.budgetingapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.models.ExpenseCategory;

import java.util.ArrayList;

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder> {

    private ArrayList<ExpenseCategory> mExpenseCategories = new ArrayList<>();
    private OnExpenseListener mOnExpenseListener;

    public ExpenseRecyclerAdapter(ArrayList<ExpenseCategory> expenseCategories, OnExpenseListener onExpenseListener) {
        this.mExpenseCategories = expenseCategories;
        this.mOnExpenseListener = onExpenseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        return new ViewHolder(view, mOnExpenseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCategoryTitle.setText(mExpenseCategories.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return mExpenseCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCategoryTitle;
        OnExpenseListener onExpenseListener;
        public ViewHolder(@NonNull View itemView,OnExpenseListener onExpenseListener) {
            super(itemView);

            txtCategoryTitle = itemView.findViewById(R.id.txtCategoryTitle);
            this.onExpenseListener = onExpenseListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onExpenseListener.onExpenseClick(getAdapterPosition());
        }
    }
    public interface OnExpenseListener{
        void onExpenseClick(int position);
    }
}
