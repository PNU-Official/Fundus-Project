package com.example.fundus_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_select_photo;
    private ImageView img_viwer;
    private TextView tv_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();

        btn_select_photo.setOnClickListener(this);
//        ImagePicker.create(this).start();
    }

    private void bindUI(){
        btn_select_photo = findViewById(R.id.btn_select_photo);
        img_viwer = findViewById(R.id.img_viewer);
        tv_path = findViewById(R.id.tv_path);
    }

    private void getPic(){
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        ImagePicker.create(this)
                .folderMode(false) // folder mode (false by default)
                .toolbarFolderTitle("앨범") // folder selection title
                .toolbarImageTitle("안저 사진을 골라주세요") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .single() // single mode
                .limit(1) // max images can be selected (99 by default)
                .enableLog(true) // disabling log
                .start(); // start image picker activity with request code
    }

    private void uploadImage(String path) {
        File file = new File(path);
        RequestParams params = new RequestParams();
        try {

            params.put("logo_img", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Network.post(this,"/setting/taky_ad/add_logo_image", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getString("code").equals("S01")) {
                        Toast.makeText(getApplicationContext(),response.getString("code"),Toast.LENGTH_SHORT).show();

                    } else {
                        String message = response.getString("message");
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });//network


    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            Log.e("image Name", image.getName());
            Log.e("image Path", image.getPath());
            Log.e("image Id", String.valueOf(image.getId()));
            tv_path.setText(image.getPath());
            Glide.with(this)
                    .load(image.getPath())
                    .into(img_viwer);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_photo:
                getPic();
                break;
        }
    }
}
