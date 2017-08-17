package com.tianyigps.dispatch2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.utils.Uri2FileU;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private static final int REQUEST = 1;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        init();

        setEventListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST == requestCode && RESULT_OK == resultCode) {
            Uri uri = data.getData();
            String path = new Uri2FileU(TestActivity.this).getRealPathFromUri(uri);

            Log.i(TAG, "onActivityResult: uri-->" + uri);
            Log.i(TAG, "onActivityResult: path-->" + path);

            Picasso.with(TestActivity.this)
                    .load(uri)
                    .fit()
                    .centerCrop()
                    .into(mImageView);
        }
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.iv_activity_test);
    }

    private void setEventListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST);
            }
        });
    }
}
