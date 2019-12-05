package com.example.fundus_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fundus_app.R;
import com.example.fundus_app.CustomAdapter;

import java.util.ArrayList;

public class ExamplePhotoActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter adapter;
    private ArrayList<String> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_photo);

        bindUI();
        getData();
    }

    private void bindUI(){
        listView = findViewById(R.id.listView);
        adapter = new CustomAdapter(this,1,dataArrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("result",String.valueOf(position));
                setResult(RESULT_OK,intent);
                Log.e("position", String.valueOf(position));
                finish();
            }
        });


    }
    
    private void getData(){
        dataArrayList.add("정상 1");
        dataArrayList.add("정상 2");
        dataArrayList.add("정상 3");
        dataArrayList.add("질환 의심 1");
        dataArrayList.add("질환 의심 2");
        dataArrayList.add("질환 의심 3");

        adapter.notifyDataSetChanged();
    }



}
