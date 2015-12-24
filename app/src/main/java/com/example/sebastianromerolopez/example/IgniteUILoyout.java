package com.example.sebastianromerolopez.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

/**
 * Created by sebastianromerolopez on 2015-12-23.
 */
public class IgniteUILoyout extends RelativeLayout {

    private WebView webView;

    /***************************************************************************
     * Constructs
     *
     * @param context A Context object used to access application assets.
     */
    public IgniteUILoyout(Context context) {
        super(context.getApplicationContext());
        initialize(context);
    }

    /***************************************************************************
     * Initializes the Layout.
     */
    private void initialize(Context context) {

        webView = new IgniteUIWebView( context );
        webView.setLayoutParams(
            new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        );

        this.addView(webView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("IgniteUIWebView", "onTouchEvent");
        return super.onTouchEvent(ev);
    }

    public void loadContent () {
        webView.loadUrl("file:///android_asset/igniteUI.html");
    }
}
