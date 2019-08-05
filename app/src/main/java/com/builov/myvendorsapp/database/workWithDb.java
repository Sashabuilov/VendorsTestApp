package com.builov.myvendorsapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class workWithDb {

    public void showAll(Context context,ArrayList<HashMap<String, String>> dataset, SQLiteDatabase database, String rb) {

        dataset.clear();
        String query = "";

        //Toast.makeText(context,rb,Toast.LENGTH_SHORT).show();

        //проверяем, в каком положении был переключатель (Материал или производитель)

        if (rb.equals("1")) query = "SELECT * FROM materials";
        if (rb.equals("2")) query = "SELECT * FROM manufacturers";
        if (rb.equals("3")) query = "SELECT * FROM temp_table";

        //создаем запрос
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        //Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {
            HashMap<String, String> dataItem = new HashMap<String, String>();

            //Заполняем клиента
            if (rb.equals("1")) {
                dataItem.put("Id", cursor.getString(0));
                dataItem.put("mName", cursor.getString(1));
            } else

            if (rb.equals("2")) {
                dataItem.put("id", cursor.getString(0));
                dataItem.put("Name", cursor.getString(1));
                dataItem.put("INN", cursor.getString(2));
            } else

            if (rb.equals("3")){

                dataItem.put("_id", cursor.getString(0));
                dataItem.put("tempName", cursor.getString(1));
                dataItem.put("tempINN", cursor.getString(2));
            }
            //Закидываем клиента в список клиентов
            assert dataset != null;
            dataset.add(dataItem);
            //Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();
    }
}