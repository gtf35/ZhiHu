package com.bill.zhihu.api.cmd;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bill.zhihu.api.net.ZhihuImageRequest;
import com.bill.zhihu.api.utils.ZhihuLog;

/**
 * 获取头像方法
 *
 * @author Bill Lv
 */
public class CmdLoadAvatarImage extends Command {

    private CallbackListener listener;
    private ZhihuImageRequest request;

    private String url;

    public CmdLoadAvatarImage(String imgUrl) {
        this.url = imgUrl;
    }

    @Override
    public void exec() {
        if (TextUtils.isEmpty(url)) {
            ZhihuLog.d(TAG, "url is null");
        } else {
            request = new ZhihuImageRequest(url, new Listener<Bitmap>() {

                @Override
                public void onResponse(Bitmap avatarImg) {
                    ZhihuLog.d(TAG, "fetch the avaatar img：" + url);
                    listener.callback(avatarImg);
                }
            }, 0, 0, Config.ARGB_8888, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    ZhihuLog.d(TAG, "fetch the captcha img faild");
                    ZhihuLog.d(TAG, "url " + url);
                    if (error.networkResponse != null) {
                        ZhihuLog.d(TAG, error.networkResponse.statusCode);
                        ZhihuLog.d(TAG, new String(error.networkResponse.data));
                    }

                }
            });
            volley.addQueue(request);
        }

    }

    @Override
    public void cancel() {
        // 由于匿名用户获取不到头像所以在创建的时候会导致request为null
        if (request == null)
            return;
        request.cancel();
    }

    @Override
    public <T extends CommandCallback> void setOnCmdCallBack(T callback) {
        this.listener = (CallbackListener) callback;
    }

    public interface CallbackListener extends CommandCallback {
        void callback(Bitmap captchaImg);
    }
}