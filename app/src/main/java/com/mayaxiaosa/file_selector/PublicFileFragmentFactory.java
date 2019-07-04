package com.mayaxiaosa.file_selector;

import com.mayaxiaosa.file_selector.pubfragment.BasePublicFileFragment;
import com.mayaxiaosa.file_selector.pubfragment.FileFragment;
import com.mayaxiaosa.file_selector.pubfragment.OtherFragment;
import com.mayaxiaosa.file_selector.pubfragment.PictureFragment;
import com.mayaxiaosa.file_selector.pubfragment.VideoFragment;

/**
 * Created by Cui on 2017/4/26.
 *
 * @Description 签到主页面工厂类，用于实例化签到主页面四个页面
 */

public class PublicFileFragmentFactory {
    public static final int FILE = 0;
    public static final int PIC = 1;
    public static final int VIDEO = 2;
    public static final int OTHER = 3;

    public BasePublicFileFragment createFragment(int position){
        switch (position) {
            case FILE:
                return new FileFragment();
            case PIC:
                return new PictureFragment();
            case VIDEO:
                return new VideoFragment();
            case OTHER:
                return new OtherFragment();
            default:
                break;
        }
        return new FileFragment();
    }
}
