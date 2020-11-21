package com.bigjpg.util;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.RawRes;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;

/**
 * 描述:文件工具类
 *
 * @author mfx
 */
public class FileUtil {

    /**
     * 创建文件的模式，已经存在的文件要覆盖
     */
    public final static int MODE_COVER = 1;

    /**
     * 创建文件的模式，文件已经存在则不做其它事
     */
    public final static int MODE_UNCOVER = 0;

    private FileUtil() {

    }

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("param invalid, filePath: " + filePath);
            return false;
        }

        File f = new File(filePath);
        return f.exists();
    }

    /**
     * 创建文件目录 不存在就创建
     *
     * @param dirPath
     * @return
     */
    public static boolean createDirectory(String dirPath) {
        if (StringUtil.isEmpty(dirPath)) {
            return false;
        }

        File file = new File(dirPath);

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    /**
     * 删除目录 及其 子目录
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteDirectory(String dirPath) {
        if (StringUtil.isEmpty(dirPath)) {
            LogUtil.e("param invalid, filePath: " + dirPath);
            return false;
        }

        File file = new File(dirPath);
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();

            for (File listFile : list) {
                if (listFile.isDirectory()) {
                    deleteDirectory(listFile.getAbsolutePath());
                } else {
                    listFile.delete();
                }
            }
        }
        file.delete();
        return true;
    }

    /**
     * 删除目录下所有文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteChildren(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("param invalid, filePath: " + filePath);
            return false;
        }

        File file = new File(filePath);

        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list == null) {
                return true;
            }
            for (File f : list) {
                if (f.isDirectory()) {
                    deleteDirectory(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
        }

        return true;
    }

    public static boolean deleteChildren(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list == null) {
                return true;
            }
            for (File f : list) {
                if (f.isDirectory()) {
                    deleteDirectory(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
        }

        return true;
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return the number of bytes in this file.
     */
    public static long getFileSize(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("Invalid param. filePath: " + filePath);
            return 0;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return file.length();
    }

    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        return file.length();
    }

    /**
     * 获取文件夹大小
     *
     * @param dirPath
     * @return （单位 byte）
     */
    public static long getDirSize(String dirPath) {
        if (StringUtil.isEmpty(dirPath)) {
            LogUtil.e("Invalid param. dirPath: " + dirPath);
            return 0;
        } else {
            return getDirSize(new File(dirPath));
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param dir
     * @return （单位 byte）
     */
    public static long getDirSize(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return 0;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }

        long dirSize = 0;
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取文件最后修改时间
     *
     * @param filePath
     * @return
     */
    public static long getFileModifyTime(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("Invalid param. filePath: " + filePath);
            return 0;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }

        return file.lastModified();
    }

    /**
     * 设置文件最后修改时间
     *
     * @param filePath
     * @param modifyTime
     * @return
     */
    public static boolean setFileModifyTime(String filePath, long modifyTime) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("Invalid param. filePath: " + filePath);
            return false;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        return file.setLastModified(modifyTime);
    }

    /**
     * 将byte[]写入文件
     *
     * @param filePath 格式如： /sdcard/abc/a.obj
     * @param content  写入内容byte[]
     * @return
     * @attention 当文件存在将被替换 当其所在目录不存在，将尝试创建
     */
    public static boolean writeFile(String filePath, byte[] content) {
        if (StringUtil.isEmpty(filePath) || null == content) {
            return false;
        }

        FileOutputStream fos = null;
        try {
            String pth = filePath.substring(0, filePath.lastIndexOf("/"));
            File pf;
            pf = new File(pth);
            if (pf.exists() && !pf.isDirectory()) {
                pf.delete();
            }
            pf = new File(filePath);
            if (pf.exists()) {
                if (pf.isDirectory())
                    deleteDirectory(filePath);
                else
                    pf.delete();
            }

            pf = new File(pth + File.separator);
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    LogUtil.e("Can't make dirs, path=" + pth);
                }
            }

            fos = new FileOutputStream(filePath);
            fos.write(content);
            fos.flush();
            fos.close();
            fos = null;
            pf.setLastModified(System.currentTimeMillis());

            return true;

        } catch (Exception ex) {
            LogUtil.e("Exception, ex: " + ex.toString());
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
        return false;
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return 输入流
     */
    public static InputStream readFile(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LogUtil.e("Invalid param. filePath: " + filePath);
            return null;
        }

        InputStream is;

        try {
            if (isFileExists(filePath)) {
                File f = new File(filePath);
                is = new FileInputStream(f);
            } else {
                return null;
            }
        } catch (Exception ex) {
            LogUtil.e("Exception, ex: " + ex.toString());
            return null;
        }
        return is;
    }

    /**
     * 读取输入流 转化为 byte[]
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static byte[] inputStreamToByteArray(InputStream is)
            throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int c = is.read(buf);
        while (-1 != c) {
            baos.write(buf, 0, c);
            c = is.read(buf);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * 获取文件的输入流
     *
     * @param path
     * @return
     */
    public static FileInputStream getFileInputStream(String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fis = new FileInputStream(file);
            }
        } catch (Exception e) {
        }
        return fis;
    }

    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
        }
        return fis;
    }

    /**
     * 获取文件的输出流
     *
     * @param path
     * @return
     */
    public static OutputStream getFileOutputStream(String path) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fos = new FileOutputStream(file);
            }
        } catch (Exception e) {
            return null;
        }
        return fos;
    }

    /**
     * 获取文件的数据
     *
     * @param path
     * @return
     */
    public static byte[] getFileData(String path) {
        byte[] data = null;// 返回的数据
        try {
            File file = new File(path);
            if (file.exists()) {
                data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
            }
        } catch (Exception e) {
        }
        return data;
    }

    /**
     * 写入新文件
     */
    public static void writeData(String filepath, byte[] data) {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 重写文件的数据
     *
     * @param path
     * @param data
     */
    public static void rewriteData(String path, byte[] data) {
        try {
            createFile(path, MODE_COVER);
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 重写文件的数据
     *
     * @param path
     * @param is
     */
    public static void rewriteData(String path, InputStream is) {
        try {
            createFile(path, MODE_COVER);
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                byte[] data = new byte[1024];
                int receive;
                while ((receive = is.read(data)) != -1) {
                    fos.write(data, 0, receive);
                    fos.flush();
                }
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 向文件的末尾添加数据
     *
     * @param path
     * @param data
     */
    public static boolean appendData(String path, byte[] data) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 向文件末尾添加数据
     *
     * @param path
     * @param is
     */
    public static void appendData(String path, InputStream is) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, true);
                byte[] data = new byte[1024];
                int receive;
                while ((receive = is.read(data)) != -1) {
                    fos.write(data, 0, receive);
                    fos.flush();
                }
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 删除文件或文件夹(包括目录下的文件)
     */
    public static void deleteFile(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        try {
            File f = new File(filePath);
            if (f.exists() && f.isDirectory()) {
                File[] delFiles = f.listFiles();
                if (delFiles != null) {
                    for (File delFile : delFiles) {
                        deleteFile(delFile.getAbsolutePath());
                    }
                }
            }
            f.delete();
        } catch (Exception e) {
            LogUtil.e("FileUtil", "deleteFile:" + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @param deleteParent 是否删除父目录
     */
    public static void deleteFile(String filePath, boolean deleteParent) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        try {
            File f = new File(filePath);
            if (f.exists() && f.isDirectory()) {
                File[] delFiles = f.listFiles();
                if (delFiles != null) {
                    for (File delFile : delFiles) {
                        deleteFile(delFile.getAbsolutePath(), deleteParent);
                    }
                }
            }
            if (deleteParent) {
                f.delete();
            } else if (f.isFile()) {
                f.delete();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 创建一个空的文件(创建文件的模式，已经存在的是否要覆盖)
     *
     * @param path
     * @param mode
     */
    public static boolean createFile(String path, int mode) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                if (mode == FileUtil.MODE_COVER) {
                    file.delete();
                    file.createNewFile();
                }
            } else {
                // 如果路径不存在，先创建路径
                File mFile = file.getParentFile();
                if (!mFile.exists()) {
                    mFile.mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 创建一个空的文件夹(创建文件夹的模式，已经存在的是否要覆盖)
     *
     * @param path
     * @param mode
     */
    public static void createFolder(String path, int mode) {
        if (StringUtil.isEmpty(path)) {
            return;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                if (mode == FileUtil.MODE_COVER) {
                    file.delete();
                    file.mkdirs();
                }
            } else {
                file.mkdirs();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 重命名文件/文件夹
     *
     * @param path
     * @param newName
     */
    public static boolean renameFile(final String path, final String newName) {
        boolean result = false;
        if (StringUtil.isEmpty(path) || StringUtil.isEmpty(newName)) {
            return false;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                result = file.renameTo(new File(newName));
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 列出目录文件
     *
     * @return
     */
    public static File[] listFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    /**
     * @param path
     * @return
     */
    public static String getAudioMimeType(String path) {
        boolean isM4A = path.toLowerCase().endsWith(".m4a");
        return isM4A ? "audio/mp4" : "audio/mpeg";
    }

    /**
     * 移动文件
     *
     * @param oldFilePath 旧路径
     * @param newFilePath 新路径
     * @return
     */
    public static boolean copyFile(String oldFilePath, String newFilePath) {
        if (TextUtils.isEmpty(oldFilePath) || TextUtils.isEmpty(newFilePath)) {
            return false;
        }
        File oldFile = new File(oldFilePath);
        if (oldFile.isDirectory() || !oldFile.exists()) {
            return false;
        }
        try {
            File newFile = new File(newFilePath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(oldFile));
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buf = new byte[1024];
            int read;
            while ((read = bis.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            fos.flush();
            fos.close();
            bis.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * InputStream转换成byte[]
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            byteStream.write(ch);
        }
        byte data[] = byteStream.toByteArray();
        byteStream.close();
        return data;
    }

    /**
     * 读取文件转为字符串
     *
     * @param filePath
     * @return
     */
    public static String readFileToString(String filePath) {
        String str;
        try {
            File readFile = new File(filePath);
            if (!readFile.exists()) {
                return null;
            }
            FileInputStream inStream = new FileInputStream(readFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            str = stream.toString();
            stream.close();
            inStream.close();
            return str;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 保存文件
     *
     * @param toSaveString
     * @param filePath
     */
    public static void writeStringToFile(String toSaveString, String filePath) {
        try {
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }

            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建nomedia文件
     * <p>
     * 没有就创建
     *
     * @param path 父目录路径 如 app/music/logo/
     */
    public static void createNoMediaFile(String path) {
        if (StringUtil.isEmpty(path)) {
            return;
        }
        String s = path + ".nomedia";
        createFile(s, FileUtil.MODE_UNCOVER);
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static boolean isVideo(String mimeType) {
        return mimeType.substring(0, 1).equals("v");
    }

    public static boolean isAudio(String mimeType) {
        return mimeType.substring(0, 1).equals("a");
    }

    public static boolean isImage(String mimeType) {
        return mimeType.substring(0, 1).equals("i");
    }

    public static String getRawFile(Context context, @RawRes int id) {
        final InputStream is = context.getApplicationContext().getResources().openRawResource(id);
        return readFrom(is);
    }

    public static String getAssetsFile(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (is == null) return null;
        return readFrom(is);
    }


    private static String readFrom(InputStream is) {
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder sb = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    public static boolean saveBitmap(Bitmap bitmap, String savePath, Bitmap.CompressFormat format) {
        return saveBitmap(bitmap, savePath, format, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, String savePath, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || TextUtils.isEmpty(savePath)) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            bitmap.compress(format, quality, fos);
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getReadableFileSize(float fileSizeInBytes){
        String[] units = {"kB", " MB", " GB", " TB", "PB", "EB", "ZB", "YB"};
        int index = -1;
        do{
            fileSizeInBytes = fileSizeInBytes / 1024;
            index++;
        }while (fileSizeInBytes > 1024);

        //超大的数值
        if(index >= units.length){
            index = units.length - 1;
        }
        return NumberFormatter.scale1(fileSizeInBytes) + units[index];
    }
}
