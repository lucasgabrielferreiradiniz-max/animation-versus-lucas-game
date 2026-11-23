package com.lucas.animationversusfanative;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public final class MainActivity extends Activity {
    private static final String GAME_URL = "https://lucasgabrielferreiradiniz-max.github.io/animation-versus-lucas-game/?version=2.17.1&source=android-app";
    private WebView game;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        game = new WebView(this);
        game.setOverScrollMode(View.OVER_SCROLL_NEVER);
        game.setBackgroundColor(0xff10141c);
        WebSettings settings = game.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        game.setWebViewClient(new WebViewClient());
        game.setWebChromeClient(new WebChromeClient());
        setContentView(game);
        if (state == null) game.loadUrl(GAME_URL);
        else game.restoreState(state);
        enterImmersiveMode();
    }

    private void enterImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) enterImmersiveMode();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (game != null) game.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override public void onBackPressed() {
        if (game != null && game.canGoBack()) game.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (game != null) game.destroy();
        super.onDestroy();
    }
}
