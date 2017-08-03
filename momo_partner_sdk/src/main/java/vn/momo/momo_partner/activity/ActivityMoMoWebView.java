package vn.momo.momo_partner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import vn.momo.momo_partner.ClientHttpAsyncTask;
import vn.momo.momo_partner.MoMoParameterNamePayment;
import vn.momo.momo_partner.R;
import vn.momo.momo_partner.utils.MoMoConfig;
import vn.momo.momo_partner.utils.MoMoLoading;
import vn.momo.momo_partner.utils.MoMoUtils;

/**
 * Created by hungdo on 7/14/17.
 */

public class ActivityMoMoWebView extends Activity{

    private  static  WebView webView;
    LinearLayout lnBack, lnReload;
    TextView tvTitle;
    ImageView imgReload;
    String webURL = "";
    Bundle dataExtra = null;
    String jsonData = "";
    String urlRequest = "";
    ImageView imgClose;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.momo_webview_activity);
        imgClose = (ImageView)findViewById(R.id.imgClose);
        dataExtra = getIntent().getExtras();
        if(dataExtra != null){
            webURL = dataExtra.getString(MoMoConfig.INTENT_URL_WEB);
            jsonData = dataExtra.getString(MoMoConfig.INTENT_JSON_DATA);
            urlRequest = dataExtra.getString(MoMoConfig.INTENT_URL_REQUEST);
        }

        webView = (WebView)findViewById(R.id.webView);

        lnBack = (LinearLayout)findViewById(R.id.lnBack);
        lnReload = (LinearLayout)findViewById(R.id.lnReload);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        imgReload = (ImageView)findViewById(R.id.imgReload);
        lnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == motionEvent.ACTION_DOWN){
                    imgClose.setAlpha((float)0.5);
                }
                if(motionEvent.getAction() == motionEvent.ACTION_UP){
                    imgClose.setAlpha((float)1.0);
                }
                return false;
            }
        });
        lnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataRequest = "";
                try {
                    JSONObject obj = new JSONObject(jsonData);
                    obj.put("requestType", "close");
                    obj.put("url_occur", webView.getUrl());
                    dataRequest = obj.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                (new ClientHttpAsyncTask(ActivityMoMoWebView.this, new ClientHttpAsyncTask.RequestToServerListener() {
                    @Override
                    public void receiveResultFromServer(String param) {
                        finish();
                    }
                }, dataRequest, urlRequest, false)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                finish();
            }
        });
        lnReload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == motionEvent.ACTION_DOWN){
                    imgReload.setAlpha((float)0.5);
                }
                if(motionEvent.getAction() == motionEvent.ACTION_UP){
                    imgReload.setAlpha((float)1.0);
                }
                return false;
            }
        });
        lnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgReload.setImageResource(R.drawable.ic_reload);
                webView.reload();

            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    AlertDialog dialog = new AlertDialog.Builder(ActivityMoMoWebView.this)
                            .setTitle(url)
                            .setMessage(message)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    //Log.d("TEST_CANCEL", "OK");
                                    if(dialog != null)
                                        dialog.dismiss();
                                }
                            })
                            .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            })
                            .create();
                    if(Build.VERSION.SDK_INT > 19){//Android 6
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
                    }else{
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                    dialog.show();
                    return false;
                }
            });
        }else{
            webView.setWebChromeClient(new WebChromeClient());
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDatabaseEnabled(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new myWebViewClient());
        webView.requestFocus();
        webView.loadUrl(webURL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//         This line enable webview inspect from chrome while debugging.
//         open chrome -> go to "chrome://inspect" -> connect your device and debug.
            webView.setWebContentsDebuggingEnabled(true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
    }


    public class myWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(final WebView view, final String url) {
            MoMoLoading.hideLoading(ActivityMoMoWebView.this);
            try{
                super.onPageFinished(view, url);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            Uri uri = Uri.parse(url);
            if(tvTitle != null)
                tvTitle.setText(uri.getScheme()+"://"+uri.getHost() +":"+ uri.getPort());
            MoMoLoading.showLoading(ActivityMoMoWebView.this);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.setVisibility(View.VISIBLE);
            if(url.contains("payment.momo.vn/callbacksdk")){
                //todo
                handleUrlCallback(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

    }

    private void handleUrlCallback(String mUrl){
        Intent intent = new Intent();
        String[] dataUrl = null;
        if(mUrl.contains("callbacksdk?")){
            dataUrl = mUrl.split("callbacksdk\\?");
        }
        if(dataUrl != null && dataUrl.length == 2 && dataUrl[1].contains("&")){
            for (String param : dataUrl[1].split("&")) {
                try{
                    String keyParam =  param.substring(0, param.indexOf("="));
                    String valueParam = param.substring(param.indexOf("=") + 1);
                    if(keyParam != null && valueParam != null && valueParam.length() > 0){
                        if(keyParam.equals("status")){
                            intent.putExtra(keyParam, Integer.valueOf(valueParam));
                            dataExtra.putInt(keyParam, Integer.valueOf(valueParam));
                        }else{
                            Log.d("dataweb ", valueParam);
                            if(keyParam.equals(MoMoParameterNamePayment.EXTRA_DATA) && valueParam != null){
                                valueParam = MoMoUtils.decodeString(valueParam);
                            }
                            if(keyParam.equals(MoMoParameterNamePayment.EXTRA) && valueParam != null){
                                valueParam = MoMoUtils.decodeString(valueParam);
                            }
                            dataExtra.putString(keyParam, valueParam);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        dataExtra.putString("url", webURL);
        intent.putExtras(dataExtra);
        ActivityMoMoWebView.this.setResult(Activity.RESULT_OK, intent);
        ActivityMoMoWebView.this.finish();
    }

}
