package wobiancao.imagenice9layout.app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import wobiancao.nice9.lib.ImageNice9Layout;

/**
 * Created by wxy on 2017/6/14.
 */

public class DemoDragActivity extends AppCompatActivity {
    Toolbar mToolbar;
    ImageNice9Layout mImageNice9Layout;
    int num = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        mImageNice9Layout = (ImageNice9Layout) findViewById(R.id.image_nice9_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_back);
        mToolbar.setTitle("拖拽");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bindData();


    }

    private void bindData() {
        String[] pices = getResources().getStringArray(R.array.Pictures);
        List<String> picStrings = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            picStrings.add(pices[i]);
        }
        mImageNice9Layout.bindData(picStrings);
    }

    public void onMinus(View v){
        num --;
        if (num <= 1){
            num = 1;
        }
        bindData();
    }

    public void onAdd(View v){
        num ++;
        if (num >= 9){
            num = 9;
        }
        bindData();
    }
}
