package com.example.lr3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lr3.Cell;
import com.example.lr3.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Cell> cellList;

    public GridViewAdapter(Context context, ArrayList<Cell>  cellList)
    {
        this.context = context;
        this.cellList = cellList;
    }

    @Override
    public int getCount()
    {
        return cellList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return cellList.get(position);
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
        convertView = inflater.inflate(R.layout.fields_grid_view, null);

        ImageView item = (ImageView)convertView.findViewById(R.id.fieldItem);

        Cell cell = cellList.get(position);

        item.setImageResource(cell.getImage());

        return convertView;
    }
}
