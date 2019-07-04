package com.mayaxiaosa.file_selector.pubfragment;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayaxiaosa.file_selector.R;
import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.FileManager;
import com.mayaxiaosa.file_selector.utils.LogUtils;
import com.mayaxiaosa.file_selector.utils.MyConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({"MissingSuperCall", "LongLogTag"})
public class PictureFragment extends BasePublicFileFragment {


    // 最多选择图片的个数
    private static int MAX_NUM = 9;


    private PictureAdapter adapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
//    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();

    /**
     * 具体下载图片，缓存图片，显示图片的具体执行类，它有两个具体的方法displayImage(...)、loadImage(...)，
     */
    private ImageLoader loader;

    /**
     * 用于指导每一个Imageloader根据网络图片的状态（空白、下载错误、正在下载）显示对应的图片，
     * 是否将缓存加载到磁盘上，下载完后对图片进行怎么样的处理。
     */
    private DisplayImageOptions options;

//    private ContentResolver mContentResolver;


//    private ImageFloder imageAll, currentImageFolder;

    public List<ImageItem> images = new ArrayList<ImageItem>();

    //已选择的图片
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    private GridView mGridView;
    private TextView mPSize;
    private ImageView mUpImg;
    private ImageView mDeleteImg;
//    private ImageView mPreviewImg;

    private List<FileBean> fileSelectedList;
    private FileBean fileBean;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_select_picture, null);
    }

    @Override
    protected void initFindViewById(View view) {
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mPSize = (TextView) view.findViewById(R.id.tv_file_size);
        mUpImg = (ImageView) view.findViewById(R.id.up_img);
        mDeleteImg = (ImageView) view.findViewById(R.id.delete_img);
//        mPreviewImg = (ImageView) view.findViewById(R.id.preview_img);
//        RelativeLayout   mBottomRl = (RelativeLayout) view.findViewById(R.id.bottom_rl);

//        mUpImg.setVisibility(MyConfig.isUpLoad? View.VISIBLE: View.GONE);
//        mDeleteImg.setVisibility(MyConfig.isUpLoad? View.GONE: View.VISIBLE);
//        mPreviewImg.setVisibility(MyConfig.isUpLoad? View.VISIBLE: View.GONE);
//        mBottomRl.setVisibility(SelectFileActivity.isSingleSelection?View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public void initData() {
        fileSelectedList = new ArrayList<>();
        initLoader();
//        imageAll = new ImageFloder();
//        imageAll.setDir(getResources().getString(R.string.dir_all_pictures));
//        currentImageFolder = imageAll;
//        mDirPaths.add(imageAll);


        images.addAll(FileManager.getImagePathList());
        adapter = new PictureAdapter();
        mGridView.setAdapter(adapter);
    }


    private void initLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置当前线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5对UIL进行加密命名
                .diskCacheSize(100 * 1024 * 1024)//50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(300)// 可以缓存的文件数量
                .tasksProcessingOrder(QueueProcessingType.LIFO)//后进先出
                .build();

        loader = ImageLoader.getInstance();
        //初始化操作
        loader.init(config);
//        mContentResolver = getActivity().getContentResolver();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)//设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.loading)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.loading)//设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//启用EXIF和JPEG图像格式
                .imageScaleType(ImageScaleType.EXACTLY)//图像将完全按比例缩小的目标大小
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            String size = getSize();
            mPSize.setText(size + "");
        } catch (NullPointerException e) {
            LogUtils.e("getSize()==null");
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        try {
            mPSize.setText(getSize());
        } catch (NullPointerException e) {
            LogUtils.e( "getSize()==null");
        }
    }

    @Override
    protected void initEvent() {
        mUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                    selectFile();

            }
        });

        //删除文件
        mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                if (fileSelectedList != null && fileSelectedList.size() != 0) {
                    for (FileBean bean:fileSelectedList) {
                        String path = bean.getFileAppPath();
                        picDelete(path);
                    }
                    clearSelect();
                    mPSize.setText("");
                    fileSelectedList.clear();
//                    mDirPaths.clear();
//                    tmpDir=new HashMap<String, Integer>();
                    images.clear();
                    images.addAll(FileManager.getImagePathList());
                    adapter.notifyDataSetChanged();
                } else {
                    MyConfig.makeToast("请选择文件");
                }
            }
        });
//
//        /**
//         * 预览
//         */
//        mPreviewImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPicture != null && selectedPicture.size() > 0) {
//                    PhotoPreviewIntent intent = new PhotoPreviewIntent(getActivity());
//                    intent.setCurrentItem(0);
//                    intent.setPhotoPaths(selectedPicture);
//                    startActivityForResult(intent, PhotoPreviewActivity.REQUEST_PREVIEW);
//                }
//            }
//        });
    }

    @Override
    public void loadMore() {

    }


    void picDelete(String filePath){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = getActivity().getContentResolver();
        String where = MediaStore.Images.Media.DATA + "='" + filePath + "'";
//删除图片
        mContentResolver.delete(uri, where, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(getActivity(), new String[] {filePath}, null,null);
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.parse(filePath));
            getActivity().sendBroadcast(intent);
        }

    }
//
//    @Override
//    public void onRefresh() {
//
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }


    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //自定义的一个类用来缓存convertview
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.grid_item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.checkBox = (Button) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setVisibility(SelectFileActivity.isSingleSelection?View.INVISIBLE:View.VISIBLE);//单选
            final ImageItem item = images.get(position);

            //显示图片
            loader.displayImage("file://" + item.path, holder.iv, options);

//            if (SelectFileActivity.isSingleSelection){//单选图片点击
//                holder.iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent it = getActivity().getIntent();
//                        it.putExtra("url", item.path);
//                        getActivity().setResult(Activity.RESULT_OK, it);
//                        getActivity().finish();
//                    }
//                });
//            }else {//多选
                //是否选中
                boolean isSelected = (selectedPicture.contains(item.path));
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                    //若选中的图片多于所设置的上限，不再加入
//                    if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.chose_at_most) + MAX_NUM + getResources().getString(R.string.sheet), Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                        File f = new File(item.path);
                        fileBean = new FileBean(item.path, MyConfig.getFileName(item.path),  MyConfig.FILE_PICTURE[6], f.length()+"");

                        //二次选择，移除
                        if (selectedPicture.contains(item.path)) {
                            selectedPicture.remove(item.path);
                            MyConfig.saveSelectFile(fileBean,    "selectfile", false);
                        } else {
                            //加入数组
                            fileSelectedList = MyConfig.readSelectFileList(   "selectfile");
                            if (fileSelectedList != null && fileSelectedList.size() != 0) {
                                if (fileSelectedList.size()==9){
                                    MyConfig.makeToast("最多选择9份文件");
                                    return;
                                }
                            }
                            selectedPicture.add(item.path);
                            MyConfig.saveSelectFile(fileBean,    "selectfile", true);
                        }
                        String size = getSize();
                        mPSize.setText(size + "");
                        v.setSelected(selectedPicture.contains(item.path));
                    }
                });
                holder.checkBox.setSelected(isSelected);
//            }

            return convertView;
        }
    }


    //自定义的一个类用来缓存convertview
    class ViewHolder {
        ImageView iv;
        Button checkBox;
    }



    class ImageFloder {
        /**
         * 图片的文件夹路径
         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    public static class ImageItem {
        String path;

        public ImageItem(String p) {
            this.path = p;
        }
    }


}
