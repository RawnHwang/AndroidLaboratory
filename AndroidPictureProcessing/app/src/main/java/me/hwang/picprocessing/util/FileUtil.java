package me.hwang.picprocessing.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import me.hwang.picprocessing.Application;

public class FileUtil {

    private static Context context = Application.getContext();

    private static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String PATH_SEPARATOR = "/";
    private static final String IMAGE_DIRECTORY = "images";

    public interface Format {
        int IMAGE_JPEG = 0;
        int IMAGE_PNG = 1;
    }

    public static String getAppStoragePath() {
        return EXTERNAL_STORAGE_PATH + PATH_SEPARATOR + context.getPackageName();
    }

    public static File getAppStorageDirectory() {
        File file = new File(getAppStoragePath());

        if (!file.exists()) {
            boolean isSuccess = file.mkdir();

            if (!isSuccess)
                return null;
        }

        return file;
    }

    public static String getImageStoragePath() {
        return getAppStoragePath() + PATH_SEPARATOR + IMAGE_DIRECTORY;
    }

    public static File getImageStorageDirectory() {
        File file = new File(getAppStorageDirectory(), IMAGE_DIRECTORY);

        if (!file.exists()) {
            boolean isSuccess = file.mkdir();

            if (!isSuccess)
                return null;
        }

        return file;
    }

    public static String generateFileName(int fileFormat) {
        StringBuilder fileName = new StringBuilder();
        String prefix = "";
        String suffix = "";
        switch (fileFormat) {
            case Format.IMAGE_JPEG:
                prefix = "app_camera_";
                suffix = ".jpg";
                break;
            case Format.IMAGE_PNG:
                prefix = "app_camera_";
                suffix = ".png";
                break;

            default:
                break;
        }

        fileName.append(prefix);
        fileName.append(System.currentTimeMillis());
        fileName.append(suffix);

        return fileName.toString();
    }
}
