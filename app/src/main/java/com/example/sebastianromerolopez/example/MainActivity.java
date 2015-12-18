package com.example.sebastianromerolopez.example;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.e("Button 1", "OnClick");
                CreateWebView();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.e("Button 2", "OnClick");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CreateWebView () {

        Context mContext = this.getBaseContext();

        LinearLayout view = new LinearLayout(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        view.setOrientation(LinearLayout.VERTICAL);

        WebView testWebView = new WebView(view.getContext());

        testWebView.getSettings().setJavaScriptEnabled(true);

        testWebView.setBackgroundColor(0);
        testWebView.setBackgroundColor(Color.TRANSPARENT);

        testWebView.setWebChromeClient(this.getWebChromeClient());
        testWebView.setWebViewClient(this.getWebViewClient());

        WebSettings webSettings = testWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        String engineScript =
                "<html>\n" +
                        "\t<head>\n" +
                        "\t\t<title>pixi.js example 2 loading a sprite sheet</title>\n" +
                        "\t\t<style>\n" +
                        "\t\t\tbody {\n" +
                        "\t\t\t\tmargin: 0;\n" +
                        "\t\t\t\tpadding: 0;\n" +
                        "\t\t\t\tbackground-color: transparent;\n"+
                        "\t\t\t}\n" +
                        "\t\t</style>\n" +
                        "\t\t<script src=\"http://www.goodboydigital.com/pixijs/examples/snake/pixi.dev.js\"></script>\n" +
                        "\t</head>\n" +
                        "\t<body>\n" +
                        "\t\t<script>\n" +
                        "\n" +
                        "\t\tvar count = 0;\n" +
                        "\t\tvar stage = new PIXI.Stage();\n" +
                        "\t\tvar renderOption = {\"transparent\": true};\n" +
                        "\t\tvar renderer = PIXI.autoDetectRenderer(window.innerWidth, window.innerHeight,null,true);\n" +
                        "\t\tvar target = new PIXI.Point();\n" +
                        "\t\tdocument.body.appendChild(renderer.view);\n" +
                        "\t\tcount = 0;\n" +
                        "\t\tvar length = 918 / 20;\n" +
                        "\t\tpoints = [];\n" +
                        "\t\tfor (var i = 0; i < 20; i++) {\n" +
                        "\t\t\tvar segSize = length;\n" +
                        "\t\t\tpoints.push(new PIXI.Point(i * length, 0));\n" +
                        "\t\t};\n" +
                        "\t\tstrip = new PIXI.Rope(PIXI.Texture.fromImage(\"http://www.goodboydigital.com/pixijs/examples/snake/snake.png\"), points);\n" +
                        "\t\tstrip.x = -918/2;\n" +
                        "\t\tvar snakeContainer = new PIXI.DisplayObjectContainer();\n" +
                        "\t\tsnakeContainer.position.x = window.innerWidth/2;\n" +
                        "\t\tsnakeContainer.position.y = window.innerHeight/2;\n" +
                        "\t\tsnakeContainer.scale.set( window.innerWidth / 1100);\n" +
                        "\t\tstage.addChild(snakeContainer);\n" +
                        "\t\tsnakeContainer.addChild(strip);\n" +
                        "\t\trequestAnimFrame(animate);\n" +
                        "\t\tfunction animate() {\n" +
                        "\t\t\tcount += 0.1;\t\t\n" +
                        "\t\t\tvar length = 918 / 20;\n" +
                        "\t\t\tfor (var i = 0; i < points.length; i++) {\n" +
                        "\t\t\t\tpoints[i].y = Math.sin(i *0.5  + count) * 30;\n" +
                        "\t\t\t\tpoints[i].x = i * length + Math.cos(i *0.3  + count) * 20;\n" +
                        "\t\t\t};\n" +
                        "\t\t    renderer.render(stage);\n" +
                        "\t\t    requestAnimFrame(animate);\n" +
                        "\t\t}\n" +
                        "\t\t</script>\n" +
                        "\t\t\n" +
                        "\t</body>\n" +
                        "</html>";

        testWebView.loadData(engineScript, "text/html", (String)null);
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
}
