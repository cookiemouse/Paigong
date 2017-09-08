package com.tianyigps.dispatch2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;

public class ShowPicActivity extends Activity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_pic);


        init();

        setEventListener();
    }

    private void init() {
        mImageView = findViewById(R.id.iv_activity_show_pic);

        String url = getIntent().getStringExtra("URL");
        Log.i("TAG", "init: url-->" + url);
        Picasso.with(ShowPicActivity.this)
                .load(url)
                .error(R.color.colorNull)
                .fit()
                .centerInside()
                .into(mImageView);
    }

    public void setEventListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
