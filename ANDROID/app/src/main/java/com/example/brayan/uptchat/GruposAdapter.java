package com.example.brayan.uptchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GruposAdapter extends BaseAdapter {

    private Context context;
    private List<Grupo> items;

    public GruposAdapter(Context context, List<Grupo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.activity_fields, parent, false);
        }


        TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        ImageView imgImg = (ImageView) rowView.findViewById(R.id.imageView);

        Grupo item = this.items.get(position);
        tvTitle.setText(item.getNombre());
        imgImg.setImageResource(R.drawable.ic_accessibility_black_24dp);

        return rowView;
    }

}