package com.builov.myvendorsapp.adapter;

import android.content.Context;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.builov.myvendorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class listViewAdapter {

    public void setAdapter(String[] from, int[] to, String rb, ListView listView, ArrayList<HashMap<String, String>> dataset, Context context) {

        //Создаем адаптер
        if (rb.equals("1")) {
            SimpleAdapter adapter = new SimpleAdapter(context, dataset, R.layout.materials_item, from, to);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        if (rb.equals("2")) {
            SimpleAdapter adapter = new SimpleAdapter(context, dataset, R.layout.manufacturers_item, from, to);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }


    public void setAdvAdapter(String[] from, int[] to, String rb, ListView listView, ArrayList<HashMap<String, String>> dataset, Context context) {

        //Создаем адаптер
        if (rb.equals("1")) {
            SimpleAdapter adapter = new SimpleAdapter(context, dataset, R.layout.manufacturers_item, from, to);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        if (rb.equals("2")) {
            SimpleAdapter adapter = new SimpleAdapter(context, dataset, R.layout.materials_item, from, to);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

        }
    }
}