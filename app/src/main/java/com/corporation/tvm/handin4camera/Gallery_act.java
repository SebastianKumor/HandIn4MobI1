package com.corporation.tvm.handin4camera;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.corporation.tvm.helpers.DatabaseHelper;

public class Gallery_act extends AppCompatActivity {
    private int count;
    private Bitmap[] thumbnails;
    private String[] arrPath;
    private ImageAdapter imageAdapter;

    DatabaseHelper dbsHelper;
    SQLiteDatabase dbs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_act);


    }

    @Override
    protected void onStart(){

        super.onStart();

        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        Uri mImageUri =MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imagecursor = getContentResolver().query(
                mImageUri,
                columns,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%/MyCameraApp/%"},
                null);
        int image_column_index = imagecursor
                .getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            arrPath[i] = imagecursor.getString(dataColumnIndex);
        }
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);

        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagecursor.close();


        dbsHelper = new DatabaseHelper(getApplication().getApplicationContext());
        dbs = dbsHelper.getWritableDatabase();
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu_drawer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.camera_button:

                Intent intent = new Intent(Gallery_act.this,CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.map_button:
                Intent intent2 = new Intent(Gallery_act.this,MapsActivity.class);
                startActivity(intent2);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {

            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView
                        .findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageview.setId(position);

            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent(Gallery_act.this,
                            EditImageAct.class);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]),
                            "image/*");
                    intent.putExtra("path", arrPath[id]);
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
    }
}
