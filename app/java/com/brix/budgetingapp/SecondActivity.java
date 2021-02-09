package com.brix.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brix.budgetingapp.adapters.SecondSectionsPagerAdapter;
import com.brix.budgetingapp.adapters.SectionsPagerAdapter;
import com.brix.budgetingapp.fragment.FragmentBudget;
import com.brix.budgetingapp.util.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class SecondActivity extends AppCompatActivity {


    TextView txtTitle;
    TabLayout tabs;
    static Intent intent;
    CustomViewPager viewPager;
    public static String title = "Income";

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
        if(title=="Budget"){
            viewPager.setCurrentItem(2);
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideSoftKeyBoard();
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
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                title = "Income";
                break;

            default:
                break;
        }

    }
    private void hideSoftKeyBoard(){
        if(this.getCurrentFocus()!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.btnBack).callOnClick();
    }
}