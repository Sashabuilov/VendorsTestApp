package com.builov.myvendorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editActivity extends AppCompatActivity {

    EditText etName;
    EditText etINN;
    Button btn_edit_cancel;
    Button btn_edit_OK;
    Button addDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //инициализируем UI элементы
        initUI();

        //Обьявляем и инициализируем intent, который вместе с данными вернется на главную активность
        final Intent intent = new Intent();

        //вынимаем из полученного из MainActivity intent три переменные ИД, ИМЯ, ИНН
        final String id = this.getIntent().getStringExtra("Id");
        String name = this.getIntent().getStringExtra("name");
        final String inn = this.getIntent().getStringExtra("INN");
        final String rb = this.getIntent().getStringExtra("rb");

        if (rb.equals("1")) {
            etINN.setEnabled(false);
            etINN.setHint("Не доступно");

        }

        //полученное имя вставляем в EditText etName
        etName.setText(name);
        etINN.setText(inn);


        //после ввода новых данных (или оставленя старых) помещаем три переменные ИД, ИМЯ, ИНН в intent
        btn_edit_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().equals("") || etINN.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Заполнены не все поля ввода", Toast.LENGTH_SHORT).show();
                } else {


                    intent.putExtra("id", id);
                    intent.putExtra("mName", etName.getText().toString());
                    intent.putExtra("INN", etINN.getText().toString());
                    intent.putExtra("rb", rb);

                    //убиваем активность
                    finish();
               }
            }
        });

        //обработка кнопки "Отмена"
        btn_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //после того как активность "умирает", возвращаем на активность MainActivity наполненный даннными intent
        //см.OnActivityResult c ключем REQUEST_CODE_EDIT
        setResult(RESULT_OK, intent);
    }

    private void initUI() {
        etName = findViewById(R.id.et_edit_name);
        etINN = findViewById(R.id.et_edit_INN);
        btn_edit_cancel = findViewById(R.id.btn_edit_cancel);
        btn_edit_OK = findViewById(R.id.btn_edit_ok);
        addDetail=findViewById(R.id.button_add_new);
    }
}
