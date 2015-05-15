package com.bill.zhihu.api;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.bill.zhihu.R;
import com.bill.zhihu.ZhihuApp;

import java.io.File;

/**
 * 用户 api
 */
public class UserApi {

    private static Context mContext = ZhihuApp.getContext();
    private static Resources res = mContext.getResources();

    /**
     * 清除缓存
     */
    public static void clearCache() {
        File cacheDir = mContext.getCacheDir();
        File[] cacheFiles = cacheDir.listFiles();
        for (File file : cacheFiles) {
            if (!file.delete()) {
                Toast.makeText(mContext,
                        res.getString(R.string.cache_delete_toast),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取缓存可用空间
     *
     * @return kb
     */
    public static float getCacheUsableSpace() {
        File cacheDir = mContext.getCacheDir();
        long usableSpace = cacheDir.getUsableSpace();
        return usableSpace / 1024f;
    }
}
