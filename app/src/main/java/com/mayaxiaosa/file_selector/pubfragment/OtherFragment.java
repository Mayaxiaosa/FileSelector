package com.mayaxiaosa.file_selector.pubfragment;


import android.view.View;

import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.MyConfig;

/**
 * 其他文件
 */
public class OtherFragment extends BasePublicFileFragment {

    @Override
    public void initData() {
        super.initData();

        initListView(otherShowList, true);


        fileType = new String[]{".rar",".apk",".exe",".7z",".zip",".apk"};
//        showProgressDialogFragment();
        scanOtherFile(fileType);
//        if (fileLocalBeanList.size() == 0) {
//            List<FileBean> list = getSpecificTypeOfFile(getActivity(), fileType);
//            if (list != null)
//                fileLocalBeanList.addAll(list);
//            if (fileLocalBeanList.size() > 10) {
//                fileShowList.addAll(fileLocalBeanList.subList(0, 9));
//            } else {
//                fileShowList.addAll(fileLocalBeanList);
//            }
//            mAdapter.notifyDataSetChanged();
//            relativeLayout_loading.setVisibility(View.GONE);
//        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        fileShowList.clear();
//        fileLocalBeanList.clear();
//        if (fileLocalBeanList.size() == 0) {
//            List<FileBean> list = getSpecificTypeOfFile(getActivity(), new String[]{".rar",".apk",".exe",".7z",".zip",".apk"});
//            if (list != null)
//                fileLocalBeanList.addAll(list);
//            if (fileLocalBeanList.size() > 10) {
//                fileShowList.addAll(fileLocalBeanList.subList(0, 9));
//            } else {
//                fileShowList.addAll(fileLocalBeanList);
//            }
//            mAdapter.notifyDataSetChanged();
//            relativeLayout_loading.setVisibility(View.GONE);
//        }
//    }

    @Override
    protected void initEvent() {
        super.initEvent();
        //删除文件
        mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                if (fileSelectedList != null && fileSelectedList.size() != 0) {
                    for (FileBean bean : fileSelectedList) {
                        String path = bean.getFileAppPath();
                        fileDelete(path);
                    }
                    clearSelect();
                    fileSelectedList.clear();
                    onResume();
                } else {
                    MyConfig.makeToast("请选择文件");
                }
            }
        });
    }
//
//    @Override
//    public void onRefresh() {
//        otherShowList.clear();
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSize =20;
//                scanOtherFile(fileType);
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
//                scanOtherFile(fileType);
//            }
//        }, MyConfig.REFRESH_CD);
//    }
    public void loadMore(){
        isLoading = true;
        showProgressDialogFragment();
        scanOtherFile(fileType);
    }
}



