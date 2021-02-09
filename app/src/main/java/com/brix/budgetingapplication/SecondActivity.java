package com.brix.budgetingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brix.budgetingapplication.adapters.SecondSectionsPagerAdapter;
import com.brix.budgetingapplication.util.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class SecondActivity extends AppCompatActivity {

    TextView txtTitle;
    TabLayout tabs;
    CustomViewPager viewPager;
    public static String title = "Expense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SecondSectionsPagerAdapter PagerAdapter = new SecondSectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(PagerAdapter);
        viewPager.setPagingEnabled(false);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        switch (title){
            case "Income":
                viewPager.setCurrentItem(0);
                break;
            case "Expense":
                viewPager.setCurrentItem(1);
                break;
            case "Budget":
                viewPager.setCurrentItem(2);
                break;
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tab_position = tabs.getSelectedTabPosition();

                switch (tab_position){
                    case 0:
                        txtTitle.setText("Income");
                        break;
                    case 1:
                        txtTitle.setText("Expense");
                        break;
                    case 2:
                        txtTitle.setText("Budget");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                title = "Expense";
                break;
            default:
                break;
        }

    }
    @Override
    public void onBackPressed() {
        findViewById(R.id.btnBack).callOnClick();
    }
}