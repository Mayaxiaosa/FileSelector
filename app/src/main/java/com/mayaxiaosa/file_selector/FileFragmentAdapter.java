package com.mayaxiaosa.file_selector;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mayaxiaosa.file_selector.model.FileBean;
import com.mayaxiaosa.file_selector.utils.AppContext;
import com.mayaxiaosa.file_selector.utils.MyConfig;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FileFragmentAdapter extends BaseAdapter {
    private List<FileBean> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    protected boolean isMax=false;//是否已经加载到最大数
    private int mSize=100;

    private OnItemClickListener mOnItemClickListener;

    public FileFragmentAdapter(List<FileBean> list) {
        this.mData = list;
        mContext = AppContext.getInstance();
        mInflater = LayoutInflater.from(mContext);

    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }

    public List<FileBean> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView mFileNameTv;
        TextView mFileSizeTv;
        ImageView mIv;
        ImageView mCheckIv;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_file_fragment_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mFileNameTv = (TextView) convertView.findViewById(R.id.tv_file_name);
            viewHolder.mFileSizeTv = (TextView) convertView.findViewById(R.id.tv_file_size);
            viewHolder.mIv = (ImageView) convertView.findViewById(R.id.img_file);
            viewHolder.mCheckIv = (ImageView) convertView.findViewById(R.id.pic_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FileBean bean = mData.get(position);
        viewHolder.mFileNameTv.setText(bean.getFileName());
        long fizeSize = Long.parseLong(bean.getFileSize());
        long size = fizeSize / 1024;
        if (size / 1024 < 1) {
            viewHolder.mFileSizeTv.setText(size + "KB");
        } else {
            viewHolder.mFileSizeTv.setText(size / 1024 + "MB");
        }
        String fileName = MyConfig.getFileName(bean.getFileAppPath());
        String name = fileName.substring(fileName.indexOf("."), fileName.length());
        if (name.contains(".mp4")  //忽略大小写
                || name.equalsIgnoreCase(".wmv")
                || name.equalsIgnoreCase(".rmvb")
                || name.equalsIgnoreCase(".avi")
                || name.equalsIgnoreCase(".mkv")
                || name.equalsIgnoreCase(".flv")
        ) {
            Glide.with(mContext).load(Uri.fromFile(new File(bean.getFileAppPath()))).into(viewHolder.mIv);
        } else {
            viewHolder.mIv.setImageResource(bean.getIcon());
        }

        viewHolder.mCheckIv.setImageResource(bean.isSelected() ? R.drawable.pic_check_on : R.drawable.pic_check);
//单选的时候隐藏
//        viewHolder.mCheckIv.setVisibility(SelectFileActivity.isSingleSelection ? View.INVISIBLE : View.VISIBLE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(position, viewHolder.mCheckIv);
                }
            }
        });

        return convertView;
    }

    public interface OnItemClickListener {
        void itemClick(int position, ImageView imageView);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
