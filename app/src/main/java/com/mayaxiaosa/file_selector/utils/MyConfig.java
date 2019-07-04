package com.mayaxiaosa.file_selector.utils;

import android.content.SharedPreferences;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.mayaxiaosa.file_selector.R;
import com.mayaxiaosa.file_selector.model.FileBean;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static android.content.Context.MODE_APPEND;

public class MyConfig {
    public static final int KEY_RESULT = 107;
    /**
     * 文档类型
     */
    public static final int TYPE_DOC = 0;
    /**
     * apk类型
     */
    public static final int TYPE_APK = 1;
    /**
     * 压缩包类型
     */
    public static final int TYPE_ZIP = 2;

    public static int[] FILE_PICTURE =
            new int[]{R.drawable.pic_file_word, R.drawable.pic_file_txt,
                    R.drawable.pic_file_excel, R.drawable.pic_file_ppt,
                    R.drawable.pic_file_rar, R.drawable.pic_file_video,
                    R.drawable.pic_file_pic, R.drawable.pic_file_no, R.drawable.pic_file_pdf};

    /**
     * 通过文件名获取文件类型图片
     * @param fileName
     * @return
     */
    public static int getFilePictureByName(String fileName) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String postfix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String extension = mimeTypeMap.getMimeTypeFromExtension(postfix);
        if (extension != null) {
            if (extension.contains("word")) {
                return FILE_PICTURE[0];
            } else if (extension.contains("image")) {
                return  FILE_PICTURE[6];
            } else if (extension.contains("video")) {
                return  FILE_PICTURE[5];
            } else if (extension.contains("excel")) {
                return  FILE_PICTURE[2];
            } else if (extension.contains("powerpoint")) {
                return  FILE_PICTURE[3];
            } else if (extension.contains("text")) {
                return  FILE_PICTURE[1];
            } else if (extension.contains("rar") || extension.contains("zip")|| extension.contains("7z")) {
                return  FILE_PICTURE[4];
            } else if (extension.contains("pdf")) {
                return  FILE_PICTURE[8];
            } else {
                return  FILE_PICTURE[7];
            }
        }
        return  FILE_PICTURE[7];

    }
    /**
     * 通过文件名获取文件图标
     */
    public static int getFileIconByPath(String path) {
        path = path.toLowerCase();
        int iconId;
        if (path.endsWith(".txt")) {
            iconId =  FILE_PICTURE[1];
        } else if (path.endsWith(".doc") || path.endsWith(".docx")) {
            iconId = FILE_PICTURE[0];
        } else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
            iconId =  FILE_PICTURE[2];
        } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
            iconId =  FILE_PICTURE[3];
        } else if (path.endsWith(".pdf")) {
            iconId =  FILE_PICTURE[8];
        } else if (path.endsWith(".jpg") | path.endsWith(".png")) {
            iconId =  FILE_PICTURE[6];
        } else {
            iconId =  FILE_PICTURE[7];
        }
        return iconId;
    }
    //存储最近选择文件
    public static void saveSelectFile(FileBean bean, String keyWord, boolean b) {
        List<FileBean> list = readSelectFileList(keyWord);
        if (list != null && list.size() > 0) {
            try {
                for (FileBean file : list) {
                    if (file.getFileAppPath().equals(bean.getFileAppPath())) {
                        if (!b) {
                            list.remove(file);
                        } else {
                            return;
                        }
                    }
                }
            } catch (ConcurrentModificationException e) {
                LogUtils.e("ConcurrentModificationException");
            }
            if (b) {
                list.add(bean);
            }
//			if (b) {
//				list.add(bean);
//			}else {
//				list.remove(b);
//			}
        } else {
            list = new ArrayList<>();
            list.add(bean);
        }
        saveSelectFileList(list, keyWord);
    }

    //存储最近选择文件
    public static void saveSelectFileList(List<FileBean> list, String keyWord) {
        SharedPreferences preferences = AppContext.getInstance().getSharedPreferences(
                "pufile",
                MODE_APPEND);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(list);
            // 将字节流编码成base64的字符窜
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Base64.Encoder encoder = Base64.getEncoder();
//                byte[] encode = encoder.encode(baos.toByteArray());
//                String oAuth_Base64 = new String(encode);
//            }
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(keyWord, oAuth_Base64);
            editor.commit();
        } catch (IOException e) {
            MyConfig.makeToast(e.toString());
        }
    }


    //读取最近选择文件
    public static List<FileBean> readSelectFileList(String keyWord) {
        List<FileBean> list = null;
        SharedPreferences preferences = AppContext.getInstance().
                getSharedPreferences("pufile", MODE_APPEND);
        String productBase64 = preferences.getString(keyWord, "");

        //读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                list = (List<FileBean>) bis.readObject();
            } catch (ClassNotFoundException e) {
                return null;
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void makeToast(String msg) {
        Toast.makeText(AppContext.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }


    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".txt")) {
            return TYPE_DOC;
        } else if (path.endsWith(".apk")) {
            return TYPE_APK;
        } else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            return TYPE_ZIP;
        } else {
            return -1;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件的路径
     * @return
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }
    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            return true;
        }
        return false;
    }
}
