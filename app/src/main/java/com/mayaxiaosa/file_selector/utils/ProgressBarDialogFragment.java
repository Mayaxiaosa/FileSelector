package com.mayaxiaosa.file_selector.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mayaxiaosa.file_selector.R;

/**
 * Created by Cui on 2016/9/3.
 * 带进度条的弹出框，用于上传头像
 */
public class ProgressBarDialogFragment extends BaseDialogFragment {
    private TextView mTitleTv;
    @Override
    protected View getDialogView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);
        return view;
    }

    @Override
    protected void initFindView(View view) {
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        mTitleTv.setText(title);
        mTitleTv.setVisibility(title.equals("")? View.GONE: View.VISIBLE);
    }
}
