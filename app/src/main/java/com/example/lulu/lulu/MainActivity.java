package com.example.lulu.lulu;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
//        webView.loadUrl("http://juju.011pp.com:11116/");
        webView.loadUrl("https://www.baidu.com/");
        WebSettings webSettings = webView .getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setAllowFileAccess(true); // 允许访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

         String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.e("TAG", "ANDROID_ID: " + androidId);
//        getOnlyId(this);
//        549614307f56fd39
//        7ae6287337d63b6a
        String channel = ChannelUtils.getChannel(this);
        Log.e("TAG", "CHANNEL: "+channel);
        postDataWithParame(channel,androidId);
    }

    /**
     * 安装量统计
     * @param channel
     * @param androidId
     */
    private void postDataWithParame(String channel, String androidId) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("channel",channel);//渠道名
        formBody.add("mac",androidId);//唯一标识
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://ceshi.com/api/channel.php")
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "onResponse: "+response.code() );
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    /**
     * 改写物理按键返回的逻辑
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            // 返回上一页面
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
