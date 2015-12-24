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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setBackgroundColor(Color.BLUE);

        setContentView(relativeLayout, rlp);
        /*
        setContentView(R.layout.activity_main);
        */

        Button button1 = new Button(this);
        button1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button1.setText("Button 1");
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.e("Button 1", "OnClick");
                CreateWebView();
            }
        });
        button1.setWidth(300);
        button1.setHeight(50);
        button1.setX(300);
        button1.setY(0);

        //rlp.setMargins(25, 0, 0, 0);

        Button button2 = new Button(this);
        button2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button2.setText("Button 2");
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.e("Button 2", "OnClick");
            }
        });
        button2.setWidth(300);
        button2.setHeight(50);
        button2.setX(300);
        button2.setY(300);

        relativeLayout.addView(button1);
        relativeLayout.addView(button2);
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

        /*
        final IgniteUIWebView webView = new IgniteUIWebView(this);

        */

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);

        IgniteUILoyout igniteUILoyout = new IgniteUILoyout(this);
        igniteUILoyout.loadContent();
        igniteUILoyout.setZ(10);
        igniteUILoyout.setLayoutParams(rlp);
        relativeLayout.addView(igniteUILoyout);


    }
}
