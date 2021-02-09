package com.brix.budgetingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brix.budgetingapp.R;
import com.brix.budgetingapp.models.IncomeCategory;

import java.util.ArrayList;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder> {

    private ArrayList<IncomeCategory> mIncomeCategory = new ArrayList<>();
    private OnIncomeListener onIncomeListener;

    public IncomeRecyclerAdapter(ArrayList<IncomeCategory> IncomeCategory,OnIncomeListener onIncomeListener) {
        this.mIncomeCategory = IncomeCategory;
        this.onIncomeListener = onIncomeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        return new ViewHolder(view, onIncomeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCategoryTitle.setText(mIncomeCategory.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mIncomeCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCategoryTitle;
        OnIncomeListener onIncomeListener;

        public ViewHolder(@NonNull View itemView,OnIncomeListener onIncomeListener) {
            super(itemView);

            txtCategoryTitle = itemView.findViewById(R.id.txtCategoryTitle);
            this.onIncomeListener = onIncomeListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onIncomeListener.onIncomeClick(getAdapterPosition());
        }
    }

    public interface OnIncomeListener{
        void onIncomeClick(int position);
    }
}
