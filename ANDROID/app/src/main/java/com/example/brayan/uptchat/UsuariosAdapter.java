package com.example.brayan.uptchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends BaseAdapter {

    private Context context;
    private List<Usuario> items;
    private ArrayList<Integer> mSelection = new ArrayList<Integer>();

    public UsuariosAdapter(Context context, List<Usuario> items) {
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
    public void setNewSelection(int position) {
        mSelection.add(position);
        notifyDataSetChanged();
    }
    public void removeSelection(int position) {
        mSelection.remove(Integer.valueOf(position));
        notifyDataSetChanged();
    }
    public int getSelectionCount() {
        return mSelection.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.activity_fields, parent, false);
        }

       /* if (mSelection.contains(position)) {
            rowView.setBackgroundColor(context.getResources().getColor(
                    android.R.color.tab_indicator_text)); // color when selected
        }*/
        TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        ImageView imgImg = (ImageView) rowView.findViewById(R.id.imageView);


        Usuario item = this.items.get(position);
        tvTitle.setText(item.getNick());
        imgImg.setImageResource(R.drawable.ic_accessibility_black_24dp);

        return rowView;
    }

}