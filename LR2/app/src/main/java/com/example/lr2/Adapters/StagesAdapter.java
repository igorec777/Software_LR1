package com.example.lr2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lr2.R;

import java.util.ArrayList;

public class StagesAdapter extends BaseAdapter
{

    Context context;
    ArrayList<String> arrayList;

    public StagesAdapter(Context context, ArrayList<String> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount()
    {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.stages_list_view, null);
        TextView item = (TextView)convertView.findViewById(R.id.stageItem);

        String text = arrayList.get(position);
        item.setText(text);

        return convertView;
    }
}
