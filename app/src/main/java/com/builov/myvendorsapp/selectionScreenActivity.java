package com.builov.myvendorsapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.builov.myvendorsapp.adapter.detailAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class selectionScreenActivity extends AppCompatActivity {
    ListView selectListView;
    ArrayList<String> stringArray;

    HashMap<String, String> dataItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        selectListView = findViewById(R.id.detailListView);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        Intent intent = new Intent();
        int i = intent.getIntExtra("count",0);
        for (int a=0;a<i; a++){
            stringArray = (ArrayList<String>) getIntent().getSerializableExtra("Name");
            Toast.makeText(getApplicationContext(), Integer.toString(a),Toast.LENGTH_SHORT).show();
        }
        new detailAdapter().setAdapter(stringArray,getApplicationContext(),selectListView
        );

    }

}
