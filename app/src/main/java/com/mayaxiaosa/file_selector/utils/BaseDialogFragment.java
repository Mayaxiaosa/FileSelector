package com.mayaxiaosa.file_selector.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mayaxiaosa.file_selector.R;

/**
 * Created by Cui on 2016/8/4.
 * dialogFragment基类
 */

public abstract class BaseDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
        View view = getDialogView(LayoutInflater.from(getActivity()), container);
        try {
            initFindView(view);
            initData();
            initEvent();
        } catch (Exception e) {
            e.printStackTrace();
            dismiss();
        }
        return view;
    }
    /**
     * 默认屏幕宽度的3/4
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    /**
     * dialog填充视图
     * @param inflater
     * @return
     */
    protected abstract View getDialogView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化视图控件
     * @param view
     */
    protected void initFindView(View view){}

    /**
     * dialog配置
     * @param builder
     */
    protected void setDialogConfig(AlertDialog.Builder builder){}

    /**
     * dialog位置和大小
     * @param width
     * @param height
     * @param grivaty
     */
    public void setDialogSizeAndGrivaty(int width, int height, int grivaty){
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setGravity(grivaty);
    }

    /**
     * 设置dialog大小
     * @param width
     * @param height
     */
    public void setDialogSize(int width, int height) {
        getDialog().getWindow().setLayout(width, height);
    }

    /**
     * 设置dialog位置
     * @param grivaty
     */
    public void setDialogGrivaty(int grivaty) {
        getDialog().getWindow().setGravity(grivaty);
    }

    protected void initEvent(){}
    protected void initData(){}

    protected OnCancelListener mOnCancelListener;

    public interface  OnCancelListener{
        void cancel();
    }

    public void setOnCancelListener(OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }

}
