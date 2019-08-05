package com.builov.myvendorsapp.adapter;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.builov.myvendorsapp.R;
import java.util.ArrayList;
public class detailAdapter {
    public void setAdapter(ArrayList<String> stringArray,Context
            context,ListView listView){
        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.details_item,R.id.testtvfordetails,stringArray);
        listView.setAdapter(adapter);
    }
}