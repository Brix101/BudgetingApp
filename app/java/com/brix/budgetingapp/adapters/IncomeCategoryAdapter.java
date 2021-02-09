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

public class IncomeCategoryAdapter extends RecyclerView.Adapter<IncomeCategoryAdapter.ViewHolder> {

    private ArrayList<IncomeCategory> mIncomeCategory = new ArrayList<>();
    OnInCatListener mOnInCatListener;

    public IncomeCategoryAdapter(ArrayList<IncomeCategory> IncomeCategory,OnInCatListener onInCatListener) {
        this.mIncomeCategory = IncomeCategory;
        this.mOnInCatListener = onInCatListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_display, parent, false);
        return new ViewHolder(view,mOnInCatListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCatDisplay.setText(mIncomeCategory.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mIncomeCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtCatDisplay;
        OnInCatListener OnInCatListener;

        public ViewHolder(@NonNull View itemView,OnInCatListener onInCatListener) {
            super(itemView);
            txtCatDisplay = itemView.findViewById(R.id.txtCatDisplay);
            this.OnInCatListener = onInCatListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnInCatListener.onInCatClick(getAdapterPosition());
        }
    }
    public interface OnInCatListener{
        void onInCatClick(int position);
    }
}
