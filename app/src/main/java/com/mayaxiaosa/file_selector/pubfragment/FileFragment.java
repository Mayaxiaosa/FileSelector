package com.mayaxiaosa.file_selector.pubfragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.IconCenterEditText;
import com.mayaxiaosa.file_selector.utils.MyConfig;

/**
 * file
 */
public class FileFragment extends BasePublicFileFragment {



    @Override
    public void initData() {
        super.initData();
        initListView(fileShowList, true);
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_CODE_EXTERNAL);
        } else {
            scanFile(fileType);
        }
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        mEdtSearch.setVisibility(View.VISIBLE);
        //开始输入
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdtSearch.getText().toString().length() > 0) {
                    mSearchDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        //取消搜索
        mSearchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdtSearch.setText("");
                initListView(fileShowList, true);
                mSearchDelete.setVisibility(View.GONE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mEdtSearch.isLeft = false;
            }
        });
        //搜索
        mEdtSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                String keyWord = mEdtSearch.getText().toString();
                if (!keyWord.equals("")) {
                    searchFile(keyWord, fileType);
                    initListView(searchShowList, true);

                }
//                for (FileBean bean : fileLocalBeanList) {
//                    if (bean.getFileName().contains(keyWord)) {
//                        fileShowList.add(bean);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
            }
        });
        //删除文件
        mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectedList = MyConfig.readSelectFileList("selectfile");
                if (fileSelectedList != null && fileSelectedList.size() != 0) {
                    for (FileBean bean : fileSelectedList) {
                        String path = bean.getFileAppPath();
                        fileDelete(path);
                    }
                    fileSelectedList.clear();
                    clearSelect();
                    scanFile(fileType);
//                    getFileList();
                } else {
                    MyConfig.makeToast("请选择文件");
                }
            }
        });
    }
//    @Override
//    public void onRefresh() {
//        fileShowList.clear();
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSize = 20;
//                scanFile(fileType);
//            }
//        }, MyConfig.REFRESH_CD);
//    }
//
//    @Override
//    public void onLoadMore() {
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSize = mSize + 20;
//                scanFile(fileType);
//            }
//        }, MyConfig.REFRESH_CD);
//    }

    public void loadMore() {
        isLoading = true;
        showProgressDialogFragment();
        scanFile(fileType);
    }

}
