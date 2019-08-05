package com.builov.myvendorsapp;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.builov.myvendorsapp.adapter.detailAdapter;
import java.util.ArrayList;
public class detailsActivity extends AppCompatActivity {
    Button back;
    ArrayList<String> stringArray;
    ListView detailListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailListView = findViewById(R.id.detailListView);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        int i = bundle.getInt( "count");
        for (int a=0;a<i; a++){
            stringArray = (ArrayList<String>) getIntent().getSerializableExtra("Name");
        }
        new detailAdapter().setAdapter(stringArray,getApplicationContext(),detailListView);



        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//
            }
        });
    }
}