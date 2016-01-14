package com.example.sebastianromerolopez.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    public enum Orientation {
        landscape,
        portrait
    };

    private Orientation localOrientation = Orientation.portrait;

    private boolean oneTabOpen = false;
    private boolean isInSideClicked = false;

    private float localRatio = 1;

    private int webViewWidth = 0;
    private int webViewHeight = 0;
    private float webViewRatio = 1;

    private int missionTabsCount = 0;
    private int leaderBoardTabsCount = 0;

    Paint paint = new Paint();

    private Rect[] missionTabPositions = new Rect[4];
    private Rect[] leaderBoardTabPositions = new Rect[4];

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

        this.addJavascriptInterface(this, "fuelUI_host");

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



        //missionTabPositions[0] = new Rect(0,getHeight()/2-300,150,getHeight()/2-150);

    }

    @Override
    public void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        Log.e("FuelIgniteUI", "onSizeChanged w = " + w + " - h = " + h);
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
/*
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);

        for( int i=0 ; i<missionTabsCount ; i++ ) {
            canvas.drawRect( missionTabPositions[i], paint );
        }

        for( int i=0 ; i<leaderBoardTabsCount ; i++ ) {
            canvas.drawRect( leaderBoardTabPositions[i], paint );
        }
        */

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
        Log.e("IgniteUIWebView", "onTouchEvent");
        //if( oneTabOpen ) {
            return super.dispatchTouchEvent(event);
        //}
/*
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int xTouch = (int)event.getX(), yTouch = (int)event.getY();

            for( int i=0 ; i<missionTabsCount ; i++ ) {
                if( missionTabPositions[i].contains(xTouch, yTouch) ) {
                    isInSideClicked = true;
                }
            }

            for( int i=0 ; i<leaderBoardTabsCount ; i++ ) {
                if( leaderBoardTabPositions[i].contains(xTouch, yTouch) ) {
                    isInSideClicked = true;
                }
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
        */
    }

    @JavascriptInterface
    public void SetWebViewSize(int width, int height, float ratio ) {
        Log.e("FuelIgniteUI", "SetWebViewSize width: " + width + " - height: "+height+" - ratio: "+ratio);
        webViewWidth = width;
        webViewHeight = height;
        webViewRatio = ratio;

        if( localOrientation == Orientation.landscape ) {
            localRatio = (float)getHeight()/(float)height;
        }
        else {
            localRatio = (float)getWidth()/(float)width;
        }
        Log.e("FuelIgniteUI", "SetWebViewSize localRatio: " + localRatio );

    }

    @JavascriptInterface
    public void OpenCloseTabCallback(boolean open) {
        Log.e("FuelIgniteUI", "OpenCloseTabCallback open = " + open);
        oneTabOpen = open;
    }

    @JavascriptInterface
    public void CreateMissionTab( int tabPosition, float left, float top, float right , float bottom ) {
        Log.e("IgniteUIWebView", "CreateMissionTab tabPosition:"+tabPosition);
        Log.e("IgniteUIWebView", "CreateMissionTab left:"+left);
        Log.e("IgniteUIWebView", "CreateMissionTab top:"+top);
        Log.e("IgniteUIWebView", "CreateMissionTab right:"+right);
        Log.e("IgniteUIWebView", "CreateMissionTab bottom:"+bottom);
        missionTabPositions[tabPosition] = new Rect((int)(left*localRatio), (int)(top*localRatio), (int)(right*localRatio),(int)(bottom*localRatio));
        missionTabsCount++;
    }

    @JavascriptInterface
    public void CreateLeaderBoardTab( int tabPosition, float left, float top, float right , float bottom ) {
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab tabPosition:"+tabPosition);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab left:"+left);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab top:"+top);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab right:"+right);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab bottom:"+bottom);
        leaderBoardTabPositions[tabPosition] = new Rect((int)(left*localRatio), (int)(top*localRatio), (int)(right*localRatio),(int)(bottom*localRatio));
        leaderBoardTabsCount++;
    }
}
