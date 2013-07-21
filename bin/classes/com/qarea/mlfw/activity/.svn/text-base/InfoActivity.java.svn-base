package com.qarea.mlfw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.SelectedDictionary;

public class InfoActivity extends BaseActivity {
    ProgressBar progressBar;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        webview = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webview.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new myWebClient());

        webview.loadUrl(getString(R.string.web_for_info));

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }

    // To handle "Back" key press event for WebView to go back to previous
    // screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem settings = menu.add(R.string.menu_home).setIcon(R.drawable.ic_home);
        settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent homeIntent = new Intent(InfoActivity.this, MainMenuActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                return false;
            }
        });
        MenuItem translator = menu.add(R.string.menu_translator).setIcon(R.drawable.ic_translate);
        translator.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (SelectedDictionary.getDictionaryID() == -1)
                    Toast.makeText(InfoActivity.this, "Select dictionary", Toast.LENGTH_SHORT)
                                    .show();
                else {
                    Intent translator = new Intent(InfoActivity.this, TranslatorActivity.class);
                    startActivity(translator);
                }
                return false;
            }
        });
        return true;
    }
}
