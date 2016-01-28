package com.example.sebastianromerolopez.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

        this.addJavascriptInterface( this , "FuelHostInterface");

        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

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
     * onSizeChanged.
     */
    @Override
    public void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        Log.e("FuelIgniteUI", "onSizeChanged w = " + w + " - h = " + h);
    }

    /***************************************************************************
     * onDraw.
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false);
        }

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
        Log.e("IgniteUIWebView", "onTouchEvent: " + event.getAction());

        if( oneTabOpen ) {
            isInSideClicked = true;
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN ) {
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
    }

    @JavascriptInterface
    public void message(String query) {
        Log.e("FuelIgniteUIWebView", "javascript message:"+query );
        Bundle parameters = new Bundle();
        for (String queryPair : query.split("&")) {
            String[] keyValue = queryPair.split("=");

            if (keyValue.length != 2) {
                Log.e("FuelIgniteUIWebView","The query key-pair format is invalid for query parameter " + queryPair);
                continue;
            }

            String key = keyValue[0];
            String value = keyValue[1];

            try {
                value = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                Log.e("FuelIgniteUIWebView","The given encoding is invalid for query parameter " + value);
                continue;
            }

            parameters.putString(key, value);
        }

        String action = parameters.getString("sdkAction");

        switch (action) {
            case "Log":
                String message = parameters.getString("message");
                WebViewLog(message);
                break;
            case "SetWebViewSize":
                int width = Integer.parseInt(parameters.getString("width"));
                int height = Integer.parseInt(parameters.getString("height"));
                float ratio = Float.parseFloat(parameters.getString("ratio"));
                SetWebViewSize(width,height,ratio);
                break;
            case "OpenCloseTabCallback":
                boolean open = Boolean.parseBoolean(parameters.getString("open"));
                OpenCloseTabCallback(open);
                break;
            case "CreateMissionTab":
                int tabPositionM = Integer.parseInt(parameters.getString("tabPosition"));
                float leftM = Float.parseFloat(parameters.getString("left"));
                float topM = Float.parseFloat(parameters.getString("top"));
                float rightM = Float.parseFloat(parameters.getString("right"));
                float bottomM = Float.parseFloat(parameters.getString("bottom"));
                CreateMissionTab(tabPositionM,leftM,topM,rightM,bottomM);
                break;
            case "CreateLeaderBoardTab":
                int tabPositionL = Integer.parseInt(parameters.getString("tabPosition"));
                float leftL = Float.parseFloat(parameters.getString("left"));
                float topL = Float.parseFloat(parameters.getString("top"));
                float rightL = Float.parseFloat(parameters.getString("right"));
                float bottomL = Float.parseFloat(parameters.getString("bottom"));
                CreateLeaderBoardTab(tabPositionL, leftL, topL, rightL, bottomL);
                break;
            default:
                Log.e("FuelIgniteUIWebView","Encountered unhandled protocol action: " + action);
                break;
        }
    }

    private void WebViewLog(String message) {
        Log.e("FuelIgniteUIWebView"," WebViewLog | " + message);
    }

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

    public void OpenCloseTabCallback(boolean open) {
        Log.e("FuelIgniteUI", "OpenCloseTabCallback open = " + open);
        oneTabOpen = open;
    }

    public void CreateMissionTab( int tabPosition, float left, float top, float right , float bottom ) {
        Log.e("IgniteUIWebView", "CreateMissionTab tabPosition:"+tabPosition);
        Log.e("IgniteUIWebView", "CreateMissionTab left:"+left);
        Log.e("IgniteUIWebView", "CreateMissionTab top:"+top);
        Log.e("IgniteUIWebView", "CreateMissionTab right:"+right);
        Log.e("IgniteUIWebView", "CreateMissionTab bottom:"+bottom);
        missionTabPositions[tabPosition] = new Rect((int)(left*localRatio), (int)(top*localRatio), (int)(right*localRatio),(int)(bottom*localRatio));
        missionTabsCount++;
    }

    public void CreateLeaderBoardTab( int tabPosition, float left, float top, float right , float bottom ) {
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab tabPosition:"+tabPosition);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab left:"+left);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab top:"+top);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab right:"+right);
        Log.e("IgniteUIWebView", "CreateLeaderBoardTab bottom:"+bottom);
        leaderBoardTabPositions[tabPosition] = new Rect((int)(left*localRatio), (int)(top*localRatio), (int)(right*localRatio),(int)(bottom*localRatio));
        leaderBoardTabsCount++;
    }

    public boolean execMethod(String method, String... args) {
        if (TextUtils.isEmpty(method)) {
            return false;
        }

        final StringBuilder stringBuilder = new StringBuilder()
                .append("javascript:ExecMethod(\"" + method + "\"");
        if (args != null) {
            for (String arg : args) {
                if (TextUtils.isEmpty(arg)) {
                    continue;
                }
                stringBuilder.append(", " + arg);
            }
        }

        stringBuilder.append(")");
        this.loadUrl(stringBuilder.toString());
        return true;
    }
}
