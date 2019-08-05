package com.builov.myvendorsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.builov.myvendorsapp.adapter.listViewAdapter;
import com.builov.myvendorsapp.database.DataBaseHelper;
import com.builov.myvendorsapp.database.getDataActivity;
import com.builov.myvendorsapp.database.sqlQuery;
import com.builov.myvendorsapp.database.workWithDb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataItem;
    ArrayList<String> stringArray = new ArrayList<String>();

    private RadioButton rbMaterials;
    private Button btnAdd;
    private Button btnSearch;
    private DataBaseHelper mDBHelper;
    private SQLiteDatabase database;
    private EditText etSearch;
    ListView listView;
    String tblMat = "materials";
    String tblMan = "manufacturers";
    String tblSvod = "manufacturers_materials";

    String[] strings;
    int j = 0;
    String row;
    String rowName;
    String tables;
    String selectName;
    String rb;

    String[] names;

    final int REQUEST_CODE_ADD = 1;
    final int REQUEST_CODE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setTitle("Товаровед");
        row = "Id";
        rowName = "mat.Id";
        tables = tblMat;
        selectName = "Name";
        rb = "1";
        registerForContextMenu(listView);
        mDBHelper = new DataBaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("Невозможно обновить БД");
        }

        database = mDBHelper.getWritableDatabase();

//Отображение данных при открытии приложения:
        new workWithDb().showAll(getApplicationContext(),dataset, database, rb);

        String[] from = {"mName"};
        final int[] to = {R.id.mName_holder};
        new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());

        rbMaterials.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               //если выбраны материалы
                if (rbMaterials.isChecked()) {
                    row = "Id";
                    rowName = "mat.Id";
                    tables = tblMat;
                    selectName = "Name";
                    rb = "1";
                    new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
                    String[] from = {"mName"};
                    int[] to = {R.id.mName_holder};
                    new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());

                    //если выбраны производители
                } else {
                    row = "id";
                    rowName = "man.id";
                    tables = tblMan;
                    selectName = "mName";
                    rb = "2";
                    new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
                    String[] from = {"Name", "INN"};
                    int[] to = {R.id.mName_holder, R.id.mINN_Holder};
                    new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());
                }
            }
        });






//Обработка кнопки Добавить
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addActivity.class);
                intent.putExtra("rb",rb);
                dataset.clear();
                new workWithDb().showAll(getApplicationContext(),dataset, database, rb);

                intent.putExtra("dataset",dataset);
                intent.putExtra("dataItem",dataItem);
                intent.putExtra("strings",strings);
                intent.putExtra("j",j);
                intent.putExtra("row",row);
                intent.putExtra("rowName",rowName);
                intent.putExtra("tables",tables);
                intent.putExtra("selectName",selectName);

                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });





//обработка кнопки Поиск
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etSearch.getText().toString().equals("")){

                String query = "SELECT * FROM materials WHERE mName=" + "'" + etSearch.getText().toString() + "'";
                Cursor crsr = database.rawQuery(query, null);
                crsr.moveToFirst();
                //Пробегаем по всем клиентам
                while (!crsr.isAfterLast()) {
                    String answer = crsr.getString(0);
                    crsr.moveToNext();
                    Toast.makeText(getApplicationContext(),"ID искомого элемента= "+answer, Toast.LENGTH_SHORT).show();
                }
                crsr.close();
                } else Toast.makeText(getApplicationContext(),"Заполните строку поиска", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Обработка нажатия на кнопки контекстного меню:
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
//кнопка детализация
            case R.id.show_advance:

                //подготовливаем переход на таблицу деталки
                Intent intent = new Intent(this, detailsActivity.class);

                //получаем позицию нажатого элемента в таблице
                HashMap<String, String> dataedit = dataset.get(info.position);

                //запускаем getDataActivity.showAdvance и получаем обьект Bundle
                Bundle bundle = new getDataActivity().showAdvance(tblMan, tblMat, tblSvod, dataedit,
                        database, strings, j, getApplicationContext(), row, rowName, tables, selectName);

                //извлекаем длину таблицы из полученного обьекта Bundle
                j = bundle.getInt("j");

                //извлекаем все элементы таблицы (массив Strings[j]) из полученного обьекта Bundle
                strings = bundle.getStringArray("strings");

                //Кладем в массив строк состоящий из j элементов, все значения из strings[j]
                stringArray.addAll(Arrays.asList(strings).subList(0, j));

                //кладем в intent колличество элементов в таблице
                intent.putExtra("count", j);

                //кладем в intent массив строк из таблицы
                intent.putExtra("Name", stringArray);

                //стартуем intent
                startActivity(intent);
                stringArray.clear();
                j = 0;
                return true;

//кнопка редактирования
            case R.id.edit:

                //подготовливаем переход на таблицу деталки
                Intent editIntent = new Intent(MainActivity.this, editActivity.class);

                //получаем позицию выбранного элемента
                HashMap<String, String> editHashMap = dataset.get(info.position);

                //запускаем getDataActivity.getPosition и получаем обьект Bundle наполненный данными
                Bundle editBundle = new getDataActivity().getPosition(database, getApplicationContext(), editHashMap, tables, row, rb);

                //получаем из Bundle три переменные ИМЯ, ИД, ИНН
                String mName = editBundle.getString("mName");
                String mId = editBundle.getString("Id");
                String mINN = editBundle.getString("mINN");

                //наполняем этими переменными editIntent
                editIntent.putExtra("name", mName);
                editIntent.putExtra("Id", mId);
                editIntent.putExtra("INN", mINN);
                editIntent.putExtra("rb",rb);


                //вызываем активность editActivity, передаем в нее наши данные внутри intent и ждем от нее ответ.
                //полученный ответ обрабатываем в onActivityResult под ключем REQUEST_CODE_EDIT
                startActivityForResult(editIntent, REQUEST_CODE_EDIT);
                return true;

//кнопка удалить
            case R.id.delete:
                HashMap<String, String> datadelet = dataset.get(info.position);
                deleteItem(datadelet);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Получение и работа с данными из других активностей:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (data == null) {return;}
        rb = data.getStringExtra("rb");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

//обрабатываем полученные данные из addActivity
                case REQUEST_CODE_ADD:


                    //извлекаем данные из data
                    String name = data.getStringExtra("name");
                    String inn = data.getStringExtra("inn");
                    String tempID = data.getStringExtra("tempID");
                    rb = data.getStringExtra("rb");
                    //вызываем sqlQuery().insert в который передаем базу данных, ИМЯ, ИНН
                    new sqlQuery().insert(database,rb,name,inn,dataset,getApplicationContext());

                    if (rb.equals("1")) {
                        new sqlQuery().svodInsert("Id", tblMat,database, tempID);
                    } else if(rb.equals("2")){
                        new sqlQuery().svodInsert("id", tblMan,database, tempID);
                    }

                    //new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());

                    new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
                    break;

//обрабатываем полученные данные из editActivity
                case REQUEST_CODE_EDIT:
                    //данные из активности editActivity кладутся в Intent data
                    //проверяем чтобы он не был пустым
                    if (data == null) {return;}
                    //assert data != null;
                    //извлекаем данные из data
                    String resultId = data.getStringExtra("id");//ИД
                    String resultName = data.getStringExtra("mName");//ИМЯ
                    String resultINN = data.getStringExtra("INN");//ИНН
                    rb = data.getStringExtra("rb");
                    //обновляем данные
                    //вызываем sqlQuery().update в который передаем базу данных, ИД, ИМЯ, ИНН
                    new sqlQuery().update(database, resultId, resultName, resultINN,getApplicationContext());

                    //обновляем данные в listView
                    new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
                    //new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());
                    break;
            }
            // если вернулось не ОК
        } else {
            Toast.makeText(this, "Системная ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    //работа с удалением элементов
    public void deleteItem(HashMap<String, String> data) {
        if (rb.equals("1")) {

            new sqlQuery().del(database, "Id", tblMat, data, rbMaterials, getApplicationContext());
            new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
            new sqlQuery().svodDel(database, "Id", data, rbMaterials, getApplicationContext());

            String[] from = {"mName"};
            final int[] to = {R.id.mName_holder};
            new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
            new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());
            //new listViewAdapter().setAdapter(from, to, rbMaterials, listView, dataset, getApplicationContext());
        }

        if (rb.equals("2")) {
            new sqlQuery().del(database, "id", tblMan, data, rbMaterials, getApplicationContext());
            new workWithDb().showAll(getApplicationContext(),dataset, database, rb);
            new sqlQuery().svodDel(database, "id", data, rbMaterials, getApplicationContext());
            String[] from = {"Name", "INN"};
            final int[] to = {R.id.mName_holder, R.id.mINN_Holder};
            new listViewAdapter().setAdapter(from, to, rb, listView, dataset, getApplicationContext());
            //new listViewAdapter().setAdapter(from, to, rbMaterials, listView, dataset, getApplicationContext());
        }
    }

    //Инициализация контекстного меню:
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    //инициализация UI элементов:
    public void initUI() {
        btnAdd = findViewById(R.id.btnAdd);
        rbMaterials = findViewById(R.id.radioMat);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        listView = findViewById(R.id.listHolder);
    }
}
