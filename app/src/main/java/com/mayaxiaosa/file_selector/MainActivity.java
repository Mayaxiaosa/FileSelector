package com.mayaxiaosa.file_selector;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayaxiaosa.file_selector.pubfragment.BasePublicFileFragment;


public class MainActivity extends AppCompatActivity {

    private LinearLayout mTitleLl;
    private LinearLayout ll_return;


    private PublicFileFragmentFactory mFactory;
    private BasePublicFileFragment[] mFileFragments;


    private String[] mBottomTexts = new String[]{"文件", "图片","视频", "其他"};
    private int[] isVisible = new int[]{1 , 0 , 0 , 0};
    private int mOldPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFindView();
        initData();
        initEvent();
    }

    protected void initFindView() {
        ll_return= (LinearLayout) findViewById(R.id.ll_return);
        mTitleLl= (LinearLayout) findViewById(R.id.file_title_ll);
    }

    public void initData() {

        mFactory=new PublicFileFragmentFactory();
        mFileFragments=new BasePublicFileFragment[mBottomTexts.length];
        initMenu();
        setDefaultFragment();

    }

    protected void initEvent() {
        ll_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 文件页面
     */
    private void setDefaultFragment(){
        mOldPosition = 0;
        mFileFragments[0] = mFactory.createFragment(0);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, mFileFragments[0]).commit();
    }

    private void initMenu() {
        mTitleLl.removeAllViews();
        for (int i=0;i<mBottomTexts.length;i++){
            View view= LayoutInflater.from(this).inflate(R.layout.item_linelayout_file_title,mTitleLl,false);
            final TextView mTitleName = (TextView) view.findViewById(R.id.tv_name);
            final View mLine1dp = view.findViewById(R.id.tv_line_1dp);
            final View mLine2dp = view.findViewById(R.id.tv_line_2dp);
            mTitleName.setText(mBottomTexts[i]);
            if (isVisible[i]==1){
                mTitleName.setSelected(true);
                mLine1dp.setVisibility(View.GONE);
                mLine2dp.setVisibility(View.VISIBLE);
            }
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(finalI);
                }
            });
            mTitleLl.addView(view);
        }
    }

    public void setFragment(int position) {
        if (position != mOldPosition) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mFileFragments[position] == null) {
                mFileFragments[position] = mFactory.createFragment(position);
                transaction.add(R.id.frame_layout, mFileFragments[position]);
            }
            for (int j=0;j<isVisible.length;j++){
                if (j==position){
                    isVisible[j]=1;
                }else {
                    isVisible[j]=0;
                }
            }
            initMenu();
            transaction.show(mFileFragments[position]);
            transaction.hide(mFileFragments[mOldPosition]).commit();
            mOldPosition = position;
        }
    }
}
