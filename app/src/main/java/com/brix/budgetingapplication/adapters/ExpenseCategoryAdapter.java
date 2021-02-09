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

public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryAdapter.ViewHolder>{

    private ArrayList<ExpenseCategory> mExpenseCategories = new ArrayList<>();
    private OnExCatListener mOnExCatListener;

    public ExpenseCategoryAdapter(ArrayList<ExpenseCategory> expenseCategories,OnExCatListener OnExCatListener) {
        this.mExpenseCategories = expenseCategories;
        this.mOnExCatListener = OnExCatListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_display, parent, false);
        return new ViewHolder(view,mOnExCatListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCatDisplay.setText(mExpenseCategories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mExpenseCategories.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCatDisplay;
        OnExCatListener OnExCatListener;

        public ViewHolder(@NonNull View itemView,OnExCatListener OnExCatListener) {
            super(itemView);
            txtCatDisplay = itemView.findViewById(R.id.txtCatDisplay);
            this.OnExCatListener = OnExCatListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnExCatListener.onExCatClick(getAdapterPosition());
        }
    }
    public interface OnExCatListener{
        void onExCatClick(int position);
    }
}
