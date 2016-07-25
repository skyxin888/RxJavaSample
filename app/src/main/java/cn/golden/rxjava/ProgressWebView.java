package cn.golden.rxjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by wxs on 15/7/21.
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private Context mContext;
    private String cookieStr;


    public String getCookieStr() {
        return cookieStr;
    }

    public interface OnWebViewTitleListener {
        void onReceivedTitle(String title);
    }
    public OnWebViewTitleListener callback;

    public interface OnWebViewGetSchemeListener{
        void getSchemeData(String url);
    }
    private OnWebViewGetSchemeListener onWebViewGetSchemeListener;

    public void setOnWebViewGetSchemeListener(OnWebViewGetSchemeListener onWebViewGetSchemeListener) {
        this.onWebViewGetSchemeListener = onWebViewGetSchemeListener;
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.webview_color_progressbar);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new WebChromeClient());



        //是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }

    }
    
    public void setProgressBarGone(){
        progressbar.setVisibility(View.GONE);
    }

    public void setOnWebViewTitleListener(OnWebViewTitleListener listener){
        callback = listener;
    }
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            // TODO Auto-generated method stub
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("k12tob://") && !url.contains("cancel_filter")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }else if(url.contains("cancel_filter")){
                if(onWebViewGetSchemeListener != null){
                    onWebViewGetSchemeListener.getSchemeData(url);
                }
            }else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            cookieStr = CookieManager.getInstance().getCookie(url);
            if (callback != null){
                callback.onReceivedTitle(view.getTitle());
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(view.getContext().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            try {
                view.stopLoading();
            } catch (Exception e) {
            }
            try {
                view.clearView();
            } catch (Exception e) {
            }
            if (view.canGoBack()) {
                view.goBack();
            }
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}