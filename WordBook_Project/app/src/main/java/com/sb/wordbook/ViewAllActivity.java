package com.sb.wordbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sb.data.DataManager;
import com.sb.data.WordData;
import com.sb.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViewAllActivity extends Activity {

    private ArrayList<WordData> wordDatas;
    private Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_view_all);
        relativeLayout.setBackgroundResource(R.drawable.backimage);

        wordDatas = DataManager.getInstance().getDatas();

        GridView gridview = (GridView)findViewById(R.id.grid);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(getApplicationContext(), ViewOneActivity.class);
                it.putExtra("index", position);
                startActivity(it);

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Iterator<String> it = bitmaps.keySet().iterator();

        while( it.hasNext() ) {
            String key = it.next();
            Bitmap b = bitmaps.get(key);

            if( b != null ) {
                b.recycle();
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {

        private Context context;

        private class ViewHolder {

            public ImageView imgView;
        }

        public ImageAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return wordDatas.size();
        }

        public Object getItem(int position) {
            return wordDatas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            WordData data = wordDatas.get(position);
            ViewHolder holder = null;

            // Bitmap 초기화
            final String imgPath = Utils.getRootDir(context) + data.getImageFile();
            Bitmap bitmap = null;

            if( bitmaps.containsKey(imgPath) ) {
                bitmap = bitmaps.get(imgPath);

            } else {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 6;
                    bitmap = BitmapFactory.decodeFile(imgPath, options);

                    bitmaps.put(imgPath, bitmap);

                } catch( Exception e ) {
                    e.printStackTrace();
                }
            }

            // View 초기화
            if( convertView == null ) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);

                // 홀더 생성 및 등록
                holder = new ViewHolder();
                holder.imgView = (ImageView)convertView.findViewById(R.id.img);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.imgView.setImageBitmap(bitmap);

            return convertView;
        }
    }
}
