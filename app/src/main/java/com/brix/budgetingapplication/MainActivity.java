package com.brix.budgetingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.brix.budgetingapplication.adapters.SectionsPagerAdapter;
import com.brix.budgetingapplication.models.BudgetModel;
import com.brix.budgetingapplication.persistance.Repository;
import com.brix.budgetingapplication.util.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
//    variables
    Intent intent;

//    Ui components
    CustomViewPager viewPager;
    public static BudgetModel newUpdate = new BudgetModel();
    Repository mRepository;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        mRepository = new Repository(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("Budget"));
        if(getIntent().hasExtra("Budget")){
            mRepository.updateBudget(newUpdate);
        }
        if (getIntent().hasExtra("BudgetInsert")){
            mRepository.insertBudget(newUpdate);
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
        }

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(getIntent().hasExtra("Budget")){
                mRepository.updateBudget(newUpdate);
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(tabs.getSelectedTabPosition()==3){
                    SecondActivity.title = "Budget";
                }intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOption:
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.show();
                break;
            case R.id.fabStats:
                intent = new Intent(this, StatsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

// OnClick Function For Menu

    public void onClick(MenuItem item) { switch(item.getItemId()) {
        case R.id.SetExpense:
            finish();
            intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            CategoryActivity.cattype = "Expense";
            break;
        case R.id.SetIncome:
            finish();
            intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            CategoryActivity.cattype = "Income";
            break;
        default:
            break;
    }
    }
}