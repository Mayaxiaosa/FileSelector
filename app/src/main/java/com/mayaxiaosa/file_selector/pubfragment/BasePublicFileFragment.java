package com.mayaxiaosa.file_selector.pubfragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mayaxiaosa.file_selector.FileFragmentAdapter;
import com.mayaxiaosa.file_selector.R;
import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.AppContext;
import com.mayaxiaosa.file_selector.utils.BaseFragment;
import com.mayaxiaosa.file_selector.utils.FileManager;
import com.mayaxiaosa.file_selector.utils.IconCenterEditText;
import com.mayaxiaosa.file_selector.utils.LogUtils;
import com.mayaxiaosa.file_selector.utils.MyConfig;
import com.mayaxiaosa.file_selector.utils.ProgressBarDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cui on 2017/4/26.
 *
 * @Description 共享文件-本地文件父fragment
 */

public abstract class BasePublicFileFragment extends BaseFragment {
    protected static final int PERMISSION_CODE_EXTERNAL = 0x3;
    private static final int SEARCH_FILE_SUCCESS = 0;
    private static final int SCAN_FILE_SUCCESS = 1;
    private static final int SCAN_VIDEO_SUCCESS = 2;
    private static final int SCAN_OTHER_SUCCESS = 3;
    private static final int SCAN_FILE_MAX = 4;
    protected IconCenterEditText mEdtSearch;
    protected ListView mListView;
    protected TextView mSizeTv;
    protected ImageView mUpImg;


    protected String[] fileType = {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".txt"};

    /**
     * 文档类型
     */

    protected FileManager mFileManager;
    protected ProgressBarDialogFragment mProgressBarDialogFragment;
    protected FileFragmentAdapter mAdapter;
    protected List<FileBean> fileShowList;
    protected List<FileBean> videoShowList;
    protected List<FileBean> otherShowList;
    protected List<FileBean> searchShowList;
    protected List<FileBean> fileSelectedList;
    protected int totalCount = 0;
    protected ImageView mSearchDelete;
    protected ImageView mDeleteImg;
    protected boolean isLoading = false;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MyConfig.KEY_RESULT) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (result) {
            switch (requestCode) {
                case PERMISSION_CODE_EXTERNAL:
                    scanFile(fileType);
                    break;
            }
        }

    }

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_file, null);
    }

    @Override
    protected void initFindViewById(View view) {
        EventBus.getDefault().register(this);
//        mBottomLl = (RelativeLayout) view.findViewById(R.id.bottom_ll);
        mEdtSearch = (IconCenterEditText) view.findViewById(R.id.edt_search);
        mSearchDelete = (ImageView) view.findViewById(R.id.search_delete_iv);
//        mListView = (RefreshSwipeMenuListView) view.findViewById(R.id.listView);
        mListView = (ListView) view.findViewById(R.id.listView);
        mSizeTv = (TextView) view.findViewById(R.id.tv_file_size);
        mUpImg = (ImageView) view.findViewById(R.id.up_img);
        mDeleteImg = (ImageView) view.findViewById(R.id.delete_img);
        showProgressDialogFragment();

//        mUpImg.setVisibility(MyConfig.isUpLoad ? View.VISIBLE : View.GONE);
//        mDeleteImg.setVisibility(MyConfig.isUpLoad ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initData() {
        searchShowList = new ArrayList<>();
        fileShowList = new ArrayList<>();
        videoShowList = new ArrayList<>();
        otherShowList = new ArrayList<>();
        fileSelectedList = new ArrayList<>();
        mFileManager = FileManager.getInstance(getActivity());
    }

    protected void initListView(List<FileBean> list, boolean isCanRefresh) {
        mAdapter = new FileFragmentAdapter(list);
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);

//        if (isCanRefresh) {
//            mListView.setListViewMode(RefreshSwipeMenuListView.HEADER);
//            mListView.setOnRefreshListener(this);
//        }
        setAdapterClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mSizeTv.setText(getSize());
        } catch (NullPointerException e) {
            LogUtils.e("文件管理*****onResume*****getSize()==null");
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        try {
            mSizeTv.setText(getSize());
        } catch (NullPointerException e) {
            LogUtils.e("文件管理****onHiddenChanged******getSize()==null");
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();//已选择2个文件，共2.3M

//        mListView.setOnRefreshListener(new MyOnRefreshNewListener());


        setAdapterClickListener();
        mUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                selectFile();


            }
        });


    }

    protected void selectFile() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < fileSelectedList.size(); i++) {
            list.add(fileSelectedList.get(i).getFileAppPath());
        }
        Intent it = getActivity().getIntent();
        it.putStringArrayListExtra("url", list);
        getActivity().setResult(Activity.RESULT_OK, it);
        getActivity().finish();
    }


    private void setAdapterClickListener() {

        mAdapter.setOnItemClickListener(new FileFragmentAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, ImageView imageView) {

                FileBean bean = mAdapter.getData().get(position);


//                if (SelectFileActivity.isSingleSelection){//单选
//                    Intent it = getActivity().getIntent();
//                    it.putExtra("url", bean.getFileAppPath());
//                    getActivity().setResult(Activity.RESULT_OK, it);
//                    getActivity().finish();
//                    return;
//                }

                int i = 1024 * 1024 * 20;
                if (!bean.isSelected()) {
                    if (Long.parseLong(bean.getFileSize()) < i) {
                        fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                        if (fileSelectedList != null && fileSelectedList.size() != 0) {
                            if (fileSelectedList.size() == 9) {
                                MyConfig.makeToast("最多选择9份文件");
                                return;
                            }
                        }
                        bean.setSelected(true);
                        imageView.setImageResource(R.drawable.pic_check_on);
                        MyConfig.saveSelectFile(bean,   "selectfile", true);
                        mSizeTv.setText(getSize());
                    } else {
                        MyConfig.makeToast("文件不能超过20M");
                    }
                } else {
                    bean.setSelected(false);
                    imageView.setImageResource(R.drawable.pic_check);
                    MyConfig.saveSelectFile(bean,  "selectfile", false);
                    mSizeTv.setText(getSize());
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    if (totalItemCount != 0 && totalItemCount - firstVisibleItem <= 13 && !isLoading && !mAdapter.isMax()) {
                        LogUtils.e("文件管理**********ListView滚动");
                        mAdapter.setSize(mAdapter.getData().size() + 100);
                        loadMore();
                    }
                } catch (IndexOutOfBoundsException e) {
                    return;
                }
            }
        });

    }

    public abstract void loadMore();

    /*
     *    文件删除
     */
    public static void fileDelete(String filePath) {
        File file = new File(filePath);
        file.exists();
    }

    protected String getSize() {
        fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
        long size = 0;
        for (FileBean b : fileSelectedList) {
            size = size + Long.parseLong(b.getFileSize());
        }
        long sizeend = size / 1024;
        if (sizeend / 1024 < 1) {
            return "已选择" + fileSelectedList.size() + "个文件,共" + sizeend + "KB";
        } else {
            return "已选择" + fileSelectedList.size() + "个文件,共" + sizeend / 1024 + "MB";
        }
    }


    /**
     * 发送图片弹出框
     */
    protected void showProgressDialogFragment() {
        mProgressBarDialogFragment = new ProgressBarDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "正在加载更多文件...");
        mProgressBarDialogFragment.setArguments(bundle);
        try {
            mProgressBarDialogFragment.show(getChildFragmentManager(), "progressbar");
        } catch (IllegalStateException e) {//java.lang.IllegalStateException: Activity has been destroyed
            return;
        }
        getChildFragmentManager().executePendingTransactions();
        //必须在build.create()之后调用
        mProgressBarDialogFragment.setCancelable(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        clearSelect();
    }

    void clearSelect() {
        SharedPreferences preferences = AppContext.getInstance().getSharedPreferences("pufile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(   "selectfile");
        edit.commit();
        try {
            mSizeTv.setText("");
        } catch (NullPointerException e) {
            return;
        }
    }


    /**
     * 根据 extension 扫描文件
     * 文档/其他文件
     *
     * @param extension
     * @return
     */
    public List<FileBean> getSpecificTypeOfFile(String[] extension) {
        //从外存中获取
        LogUtils.e("文件管理**********开始扫描文件");
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };
        //构造筛选语句
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver = AppContext.getInstance().getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
        LogUtils.e("文件管理**********查询的SQL语句：" + selection);
        List<FileBean> list = new ArrayList<>();
        if (cursor == null) {
            return list;
        }
        //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        File file;
        while (cursor.moveToNext()) {
            //输出文件的完整路径
            String data = cursor.getString(0);
            int dot = data.lastIndexOf("/");
            String name = data.substring(dot + 1);
            file = new File(data);
            long length = file.length();
            if (length > 0) {
                FileBean fileBean = new FileBean(data, name,  MyConfig.getFilePictureByName(name), length + "");
                if (fileSelectedList != null && fileSelectedList.size() > 0) {
                    for (FileBean bean : fileSelectedList) {
                        if (fileBean.getFileAppPath().equals(bean.getFileAppPath())) {
                            fileBean.setSelected(true);
                        }
                    }
                }
                list.add(fileBean);
                LogUtils.e("文件管理**********已达到最大数据  list.size()=" + list.size());
                LogUtils.e("文件管理**********已达到最大数据  mAdapter.getData().size()=" + mAdapter.getData().size());
                LogUtils.e("文件管理**********已达到最大数据  mAdapter.getSize()=" + mAdapter.getSize());
                if (list.size() == mAdapter.getSize()) {
                    LogUtils.e("文件管理******上****返回了" + list.size() + "条数据");
                    cursor.close();
                    return list;
                }

            }
        }
        cursor.close();
        if (mAdapter.getData().size() != 0 && (list.size() > 100 || list.size() < mAdapter.getSize())) {
            mAdapter.setMax(true);
            Message message = new Message();
            message.what = SCAN_FILE_MAX;
            EventBus.getDefault().post(message);
        }
        LogUtils.e("文件管理******下****返回了" + list.size() + "条数据");
        return list;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Message msg) {
        List<FileBean> list;
        switch (msg.what) {
            case SCAN_FILE_SUCCESS:
                list = (List<FileBean>) msg.obj;
                fileShowList.clear();
                fileShowList.addAll(list);
                totalCount = fileShowList.size();
                getFileSuccess();
                break;
            case SCAN_VIDEO_SUCCESS:
                list = (List<FileBean>) msg.obj;
                videoShowList.clear();
                videoShowList.addAll(list);
                totalCount = videoShowList.size();

                getFileSuccess();
                break;
            case SCAN_OTHER_SUCCESS:
                list = (List<FileBean>) msg.obj;
                otherShowList.clear();
                otherShowList.addAll(list);
                totalCount = otherShowList.size();
                getFileSuccess();
                break;
            case SEARCH_FILE_SUCCESS://搜索文件
                list = (List<FileBean>) msg.obj;
                searchShowList.clear();
                searchShowList.addAll(list);
                totalCount = searchShowList.size();

                getFileSuccess();
                break;
            case SCAN_FILE_MAX:
                MyConfig.makeToast("全部加载完成。");
                break;
        }
        isLoading = false;

    }

    /**
     * 搜索文件结果
     *
     * @param list
     */
    private void getSearchFileSuccess(List<FileBean> list) {
        mAdapter = new FileFragmentAdapter(list);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 扫描文件结果
     */
    protected void getFileSuccess() {
        if (mProgressBarDialogFragment != null) mProgressBarDialogFragment.dismiss();
//        mListView.complete();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 开启子线程扫描文档
     *
     * @param extension
     */
    protected void scanFile(final String[] extension) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FileBean> list = getSpecificTypeOfFile(extension);
                Message message = new Message();
                message.what = SCAN_FILE_SUCCESS;
                message.obj = list;
                EventBus.getDefault().post(message);

            }
        }).start();
    }


    /**
     * 开启子线程扫描视频
     */
    protected void scanVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FileBean> list = FileManager.getVideos();
                Message message = new Message();
                message.what = SCAN_VIDEO_SUCCESS;
                message.obj = list;
                EventBus.getDefault().post(message);
            }
        }).start();
    }

    /**
     * 开启子线程扫描其他文件
     */
    protected void scanOtherFile(final String[] fileType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FileBean> list = getSpecificTypeOfFile(fileType);
                Message message = new Message();
                message.what = SCAN_OTHER_SUCCESS;
                message.obj = list;
                EventBus.getDefault().post(message);
            }
        }).start();
    }

    /**
     * 搜索本地文件
     *
     * @param keyword
     * @param fileType
     */
    protected void searchFile(final String keyword, final String[] fileType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                //从外存中获取
                LogUtils.e("开始扫描文件");
                Uri fileUri = MediaStore.Files.getContentUri("external");
                //筛选列，这里只筛选了：文件路径和不含后缀的文件名
                String[] projection = new String[]{
                        MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
                };
                //构造筛选语句
                //构造筛选语句
                String selection = "";
                for (int i = 0; i < fileType.length; i++) {
                    if (i != 0) {
                        selection = selection + " OR ";
                    }
                    selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + fileType[i] + "'";
                }
                selection = " title LIKE '%" + keyword + "%'" + " AND ( " + selection + " )";
                LogUtils.e("查询语句:          " + selection);
                //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
                String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
                //获取内容解析器对象
                ContentResolver resolver = AppContext.getInstance().getContentResolver();
                //获取游标
                Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
                List<FileBean> list = new ArrayList<>();
                if (cursor == null) {
                    message.what = SEARCH_FILE_SUCCESS;
                    message.obj = list;
                    EventBus.getDefault().post(message);
                }
                //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
                File file;
                while (cursor.moveToNext()) {
                    //输出文件的完整路径
                    String data = cursor.getString(0);
                    int dot = data.lastIndexOf("/");
                    String name = data.substring(dot + 1);
                    file = new File(data);
                    long length = file.length();
                    if (length > 0) {
                        list.add(new FileBean(data, name,  MyConfig.getFilePictureByName(name), length + ""));
                    }
                }
                message.what = SEARCH_FILE_SUCCESS;
                message.obj = list;
                EventBus.getDefault().post(message);
                LogUtils.e("搜索结束 共" + list.size() + "条");
                cursor.close();
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

}
