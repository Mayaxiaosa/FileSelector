package com.mayaxiaosa.file_selector.pubfragment;

import android.view.View;

import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.MyConfig;

/**
 *
 */
public class VideoFragment extends BasePublicFileFragment {

    @Override
    public void initData() {
        super.initData();
        initListView(videoShowList, false);
//        showProgressDialogFragment();
        scanVideo();
//        if (fileLocalBeanList.size()==0) {
//            fileLocalBeanList.addAll(FileManager.getVideos());
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
//        if (fileLocalBeanList.size()==0) {
//            fileLocalBeanList.addAll(FileManager.getVideos());
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
//    @Override
//    public void onRefresh() {
//        videoShowList.clear();
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mListView.complete();
//            }
//        }, MyConfig.REFRESH_CD);
//    }
//
//    @Override
//    public void onLoadMore() {
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                mListView.complete();
//            }
//        }, MyConfig.REFRESH_CD);
//
//    }
    public void loadMore(){
    }
}
