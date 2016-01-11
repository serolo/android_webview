package com.example.sebastianromerolopez.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

/**
 * Created by sebastianromerolopez on 2015-12-24.
 */
public class IgniteUIWebView extends WebView {

    private boolean oneTabOpen = false;
    private boolean isInSideClicked = false;

    /***************************************************************************
     * Constructs a new WebView with a Context object.
     *
     * @param context A Context object used to access application assets.
     */
    public IgniteUIWebView(Context context) {
        super(context.getApplicationContext());
        initialize(context);
    }

    /***************************************************************************
     * Initializes the WebView.
     */
    private void initialize(Context context) {

        this.addJavascriptInterface( this , "fuelUI_host");

        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        setBackgroundColor(0);
        setBackgroundColor(Color.TRANSPARENT);

        setWebChromeClient(this.getWebChromeClient());
        setWebViewClient(this.getWebViewClient());

        // Do not set initial scale, on the WebView. Certain devices, regardless
        // of target SDK level, experience rendering issues - improper scaling,
        // black screen canvas, etc.
        //setInitialScale(100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //initializePost19();
        }

        initializeWebSettings();
        //initializeCookieSettings();
    }

    /***************************************************************************
     * Initializes the WebView web settings.
     */
    private void initializeWebSettings() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setAllowFileAccess(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // API 18 deprecations
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //initializeWebSettingsPre18(webSettings);
        }

        // web view local storage
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        // API 19 deprecations
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //initializeWebSettingsPre19(webSettings);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //initializeWebSettings21(webSettings);
        }

        // web view content view-port scaling and zooming
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);


    }

    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("FuelIgniteUI", "WebChromeClient onConsoleMessage, message = " + consoleMessage.message());
                return true;
            }

            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                return true;
            }
        };
    }

    private WebViewClient getWebViewClient() {
        return new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("FuelIgniteUI", "WebViewClient onPageStarted, url = " + url);
            }

            public void onPageFinished(WebView view, String url) {
                Log.e("FuelIgniteUI", "WebViewClient onPageFinished, url = " + url);
                view.setBackgroundColor(0x00000000);
                if (Build.VERSION.SDK_INT >= 11) {
                    view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if( oneTabOpen ) {
            return super.dispatchTouchEvent(event);
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect rect1 = new Rect(0,0,100,getHeight());
            Rect rect2 = new Rect(getWidth()-100,0,getWidth(),getHeight());

            if( rect1.contains((int)event.getX(), (int)event.getY()) ) {
                isInSideClicked = true;
            }
            else if( rect2.contains((int)event.getX(), (int)event.getY()) ) {
                isInSideClicked = true;
            }

            if(isInSideClicked){
                return super.dispatchTouchEvent(event);
            }else{
                return false;
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP && isInSideClicked) {
            isInSideClicked = false;
            return super.dispatchTouchEvent(event);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE && isInSideClicked) {
            return super.dispatchTouchEvent(event);
        }else{
            isInSideClicked = false;
            return false;
        }
    }

    @JavascriptInterface
    public void OpenCloseTabCallback(boolean open) {
        Log.e("FuelIgniteUI", "OpenCloseTabCallback open = " + open);
        oneTabOpen = open;
    }
}
