package com.example.fundus_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.esafirm.imagepicker.model.Image;
import com.example.fundus_app.LoadingDialog;
import com.example.fundus_app.Network;
import com.example.fundus_app.R;
import com.example.fundus_app.Useful;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_select_photo, btn_send_photo, btn_sample_img;
    private ImageView img_viwer;
    private TextView tv_path;
    private Boolean isPicked = false;
    private String img_path = "";
    private Bitmap bitmap;

    public static int REQUEST_REFRESH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();

        btn_select_photo.setOnClickListener(this);
        btn_send_photo.setOnClickListener(this);
        btn_sample_img.setOnClickListener(this);
//        ImagePicker.create(this).start();
    }

    private void bindUI() {
        btn_select_photo = findViewById(R.id.btn_select_photo);
        btn_send_photo = findViewById(R.id.btn_send_photo);
        btn_sample_img = findViewById(R.id.btn_sample_img);
        img_viwer = findViewById(R.id.img_viewer);
        tv_path = findViewById(R.id.tv_path);
    }

    private void getPic() {
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
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        RequestParams params = new RequestParams();
        if (path.isEmpty()) {
            params.put("image", bitmap);

        } else {
            File file = new File(path);
            try {
                params.put("image", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        Network.post(this, "/predict", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("response", String.valueOf(response));
                try {
                    if (response.getString("code").equals("s01")) {
                        String nonsymptom = response.getString("nonsymptom");
                        String symptom = response.getString("symptom");

                        Log.e("nonSymtpo", response.getString("nonsymptom"));
                        Log.e("symptom", response.getString("symptom"));

                        Toast.makeText(getApplicationContext(), response.getString("code"), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();

                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("nonsymptom", nonsymptom);
                        intent.putExtra("symptom", symptom);
                        startActivity(intent);


                    } else {
//                        String message = response.getString("message");
//                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "called");

        if (requestCode == REQUEST_REFRESH) {
            //샘플 사진에서 받아오기
            if (resultCode == RESULT_CANCELED) {   // RESULT_CANCEL
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
            if (data.getStringExtra("result").equals("0")) {
                img_viwer.setImageResource(R.drawable.non_1);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.non_1);

            } else if (data.getStringExtra("result").equals("1")) {
                img_viwer.setImageResource(R.drawable.non_2);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.non_2);

            } else if (data.getStringExtra("result").equals("2")) {
                img_viwer.setImageResource(R.drawable.non_3);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.non_3);

            } else if (data.getStringExtra("result").equals("3")) {
                img_viwer.setImageResource(R.drawable.sym_1);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.sym_1);

            } else if (data.getStringExtra("result").equals("4")) {
                img_viwer.setImageResource(R.drawable.sym_2);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.sym_2);

            } else if (data.getStringExtra("result").equals("5")) {
                img_viwer.setImageResource(R.drawable.sym_3);
                bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.sym_3);

            }
            isPicked = true;


        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            Log.e("image Name", image.getName());
            Log.e("image Path", image.getPath());
            Log.e("image Id", String.valueOf(image.getId()));
            img_path = image.getPath();

            tv_path.setText(img_path);
            Glide.with(this)
                    .load(image.getPath())
                    .into(img_viwer);

            isPicked = true;
            img_path = image.getPath();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_photo:
                getPic();
                break;
            case R.id.btn_send_photo:
                if (isPicked == false) {
                    Toast.makeText(this, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage(img_path);
                }
                break;
            case R.id.btn_sample_img:
                Intent in = new Intent(MainActivity.this, ExamplePhotoActivity.class);
                startActivityForResult(in, REQUEST_REFRESH);
                break;
        }
    }
}
