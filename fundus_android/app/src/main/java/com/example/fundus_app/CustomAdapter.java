package com.example.fundus_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private ArrayList<String> items;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Context context = parent.getContext();

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_item, null);
        }

        // ImageView 인스턴스
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        // 리스트뷰의 아이템에 이미지를 변경한다.
        if ("정상 1".equals(items.get(position)))
            imageView.setImageResource(R.drawable.non_1);
        else if ("정상 2".equals(items.get(position)))
            imageView.setImageResource(R.drawable.non_2);
        else if ("정상 3".equals(items.get(position)))
            imageView.setImageResource(R.drawable.non_3);
        else if ("질환 의심 1".equals(items.get(position)))
            imageView.setImageResource(R.drawable.sym_1);
        else if ("질환 의심 2".equals(items.get(position)))
            imageView.setImageResource(R.drawable.sym_2);
        else if ("질환 의심 3".equals(items.get(position)))
            imageView.setImageResource(R.drawable.sym_3);


        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(items.get(position));


        return v;
    }
}


