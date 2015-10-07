package com.corporation.tvm.handin4camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class EditImageAct extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        Intent in = getIntent();
        String path = in.getStringExtra("path");
        ImageView i = (ImageView) findViewById(R.id.imageView1);
        Bitmap new_image = BitmapFactory.decodeFile(path);
        i.setImageBitmap(new_image);
    }

}
