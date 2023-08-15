package com.example.ewardrobe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    private Boolean arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void setToolBar(int title, boolean _arrow){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        this.arrow = _arrow;

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(arrow);
            actionBar.setTitle(title);
        }
    }
}