package com.example.lr3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lr3.R;
import com.example.lr3.Room;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Room> arrayList;

    public CustomAdapter(Context context, ArrayList<Room> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Object getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rooms_list_view, null);
        TextView item = (TextView)convertView.findViewById(R.id.roomItem);

        Room room = arrayList.get(position);
        item.setText("roomId:\n" + room.getId() + "\nownerId:\n" + room.getOwnerId());
        return convertView;
    }

    @Override
    public int getCount()
    {
        return this.arrayList.size();
    }
}
