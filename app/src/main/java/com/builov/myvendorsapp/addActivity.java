package com.builov.myvendorsapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.builov.myvendorsapp.adapter.listViewAdapter;
import com.builov.myvendorsapp.database.DataBaseHelper;
import com.builov.myvendorsapp.database.sqlQuery;
import com.builov.myvendorsapp.database.workWithDb;

import java.util.ArrayList;
import java.util.HashMap;

public class addActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataItem;

    Button add;
    Button btn_add_advance;
    Button btn_cancel;
    EditText etName;
    EditText etINN;
    String rb="";
    TextView tv_Choise;
    ListView addListView;
    Context mContext;
    Intent intent = new Intent();

    private DataBaseHelper mDBHelper;
    private SQLiteDatabase database;

    private String svodINN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mDBHelper = new DataBaseHelper(this);
        database = mDBHelper.getWritableDatabase();

        mContext = this.getApplicationContext();
        initUI();
        setTitle("Добавление");
        etINN.setEnabled(false);
        etINN.setText("Не доступно");
        rb = this.getIntent().getStringExtra("rb");

        if (rb.equals("1")) {
            etINN.setEnabled(false);
            etINN.setText("Не доступно");
            setTitle("МАТЕРИАЛЫ");
        }

        if (rb.equals("2")){
            etINN.setEnabled(true);
            etINN.setText("");
            setTitle("ПРОИЗВОДИТЕЛИ");
            tv_Choise.setText("ВЫБЕРИТЕ МАТЕРИАЛ");
        }

        btn_add_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this, addAdvanceActivity.class);
                intent.putExtra("rb", rb);
                startActivityForResult(intent,1);

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("name", etName.getText().toString());
                intent.putExtra("inn", etINN.getText().toString());
                intent.putExtra("rb", rb);
                setResult(RESULT_OK, intent);
                database.delete("temp_table",null,null);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getBundleExtra("bundle");
        //Toast.makeText(getApplicationContext(),rb,Toast.LENGTH_SHORT).show();

        String svodId = bundle.getString("Id");
        String svodName = bundle.getString("mName");
        svodINN = bundle.getString("mINN");

        if (rb.equals("1")){


        }

         new sqlQuery().tempInsert(database,rb,svodName,svodINN);

        if(rb.equals("1")){

            rb="3";
            String[] from = {"tempName", "tempINN"};
            int[] to = {R.id.mName_holder, R.id.mINN_Holder};
            new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
            rb="2";
            new listViewAdapter().setAdapter(from,to,rb,addListView,dataset,getApplicationContext());
            rb="1";
        }

        intent.putExtra("tempID",svodId);

    }

    public void initUI() {
        etName = findViewById(R.id.etName);
        etINN = findViewById(R.id.etInn);
        add = findViewById(R.id.button_add_new);
        btn_add_advance =findViewById(R.id.button_add);
        btn_cancel=findViewById(R.id.btn_add_cancel);
        tv_Choise=findViewById(R.id.tv_Choise);
        addListView = findViewById(R.id.addListView);
    }
}