package com.lucas.animationversusfanative;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final String GAME_URL = "https://lucasgabrielferreiradiniz-max.github.io/animation-versus-lucas-game/?version=2.17.1&source=android-app&native-splash=1";
    private static final long THANKS_SCREEN_MS = 5_000L;
    private WebView game;
    private FrameLayout root;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(8, 13, 23));
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
        root.addView(game, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(root);

        if (state == null) {
            View thanks = makeNativeThanksScreen();
            root.addView(thanks, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            game.loadUrl(GAME_URL);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (thanks.getParent() == root) root.removeView(thanks);
            }, THANKS_SCREEN_MS);
        } else {
            game.restoreState(state);
        }
        enterImmersiveMode();
    }

    private View makeNativeThanksScreen() {
        GradientDrawable background = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] { 0xffffd900, 0xffff8b12, 0xffea0050 });
        FrameLayout screen = new FrameLayout(this);
        screen.setBackground(background);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.avf_original_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setAdjustViewBounds(true);
        int maxLogoWidth = (int) (getResources().getDisplayMetrics().widthPixels * .62f);
        int maxLogoHeight = (int) (getResources().getDisplayMetrics().heightPixels * .68f);
        FrameLayout.LayoutParams logoParams = new FrameLayout.LayoutParams(
            Math.min(dp(540), maxLogoWidth), Math.min(dp(500), maxLogoHeight), Gravity.CENTER);
        screen.addView(logo, logoParams);

        TextView thanks = new TextView(this);
        thanks.setText("AGRADECEMOS ZAPIA 🤩");
        thanks.setTextColor(Color.BLACK);
        thanks.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        thanks.setTypeface(Typeface.create("casual", Typeface.BOLD));
        thanks.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        thanks.setSingleLine(true);
        thanks.setShadowLayer(dp(1), 0, dp(1), 0x66fff4c7);
        FrameLayout.LayoutParams thanksParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.TOP | Gravity.START);
        thanksParams.leftMargin = dp(16);
        thanksParams.rightMargin = dp(8);
        thanksParams.topMargin = dp(15);
        screen.addView(thanks, thanksParams);
        return screen;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
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
