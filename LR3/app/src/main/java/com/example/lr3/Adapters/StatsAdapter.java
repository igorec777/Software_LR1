package com.example.lr3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lr3.R;

import java.util.ArrayList;

public class StatsAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Boolean> statsList;

    public StatsAdapter(Context context, ArrayList<Boolean> statsList)
    {
        this.context = context;
        this.statsList = statsList;
    }

    @Override
    public int getCount()
    {
        if (statsList.size() > 0)
            return this.statsList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return statsList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rooms_list_view, null);
        TextView item = (TextView)convertView.findViewById(R.id.roomItem);

        boolean result = statsList.get(position);

        if (result)
            item.setText("Победа");
        else
            item.setText("Поражение");

        return convertView;
    }
}
