package com.brix.budgetingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.brix.budgetingapp.adapters.DailyRecylerAdpater;
import com.brix.budgetingapp.models.FinancialStatement;
import com.brix.budgetingapp.persistance.BudgetDatabase;
import com.brix.budgetingapp.persistance.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.brix.budgetingapp.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
//    variables
    private static final String TAG = "MainActivity";
    Intent intent;
//    Ui components


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_account_box_24);
//        tabs.getTabAt(2).
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                finish();
                intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOption:
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.show();
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
        case R.id.SetBudget:
            SecondActivity.title = "Budget";
            findViewById(R.id.fab).callOnClick();
            break;
        default:
            break;
    }
    }

}