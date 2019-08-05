package com.builov.myvendorsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.builov.myvendorsapp.R;
import com.builov.myvendorsapp.adapter.listViewAdapter;
import com.builov.myvendorsapp.database.DataBaseHelper;
import com.builov.myvendorsapp.database.getDataActivity;
import com.builov.myvendorsapp.database.workWithDb;

import java.util.ArrayList;
import java.util.HashMap;

public class addAdvanceActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
    //HashMap<String, String> dataItem;


    private DataBaseHelper mDBHelper;
    private SQLiteDatabase database;
    ListView listView;
    String rb;
    Context context;
    Button buttonOk;
    String row;
    String tables;
    String tblMat = "materials";
    String tblMan = "manufacturers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initUI();


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        buttonOk.setText("OK");

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });
        rb=getIntent().getStringExtra("rb");

        context = this.getApplicationContext();
        mDBHelper = new DataBaseHelper(this);
        database = mDBHelper.getWritableDatabase();


        if(rb.equals("1")){rb="2";} else if (rb.equals("2")){rb="1";}

        new workWithDb().showAll(getApplicationContext(),dataset, database, rb);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                HashMap<String, String> editHashMap = dataset.get(position);

                if (rb.equals("1")){
                    tables=tblMan;
                    row = "id";
                    rb="2";
                } else {

                    tables=tblMat;
                    row = "Id";
                    rb="1";
                }

                Bundle bundle = new getDataActivity().getPosition(database,context,editHashMap,tables,row,rb);

                if (rb.equals("1")){rb="2";}

                else if(rb.equals("2")){rb="1";}

                Intent intent=new Intent();
                intent.putExtra("dataset",dataset);
                intent.putExtra("bundle",bundle);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        if (rb.equals("2")){//в первом окне чек бокс стоял на Материалах,нужно отобразить Производителей
            new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
            //получили dataset из manufacturers
            String[] from = {"Name", "INN"};
            rb="1";
            int[] to = {R.id.mName_holder, R.id.mINN_Holder};
            new listViewAdapter().setAdvAdapter(from, to, rb, listView, dataset, getApplicationContext());
            //rb="2";

        } else

        if (rb.equals("1")) {//в первом окне чек бокс стоял на Производителях,нужно отобразить материалы
            new workWithDb().showAll(getApplicationContext(), dataset, database, rb);
            String[] from = {"mName"};
            rb="2";
            final int[] to = {R.id.mName_holder};
            new listViewAdapter().setAdvAdapter(from, to, rb, listView, dataset, getApplicationContext());
            //rb = "1";
        }
    }

    private void initUI() {
        listView = findViewById(R.id.detailListView);
        buttonOk = findViewById(R.id.buttonBack);
    }
}
