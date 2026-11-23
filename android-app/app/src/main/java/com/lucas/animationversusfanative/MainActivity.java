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
import android.widget.LinearLayout;
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
            new int[] { 0xff435e87, 0xff1b2a42, 0xff080d17 });
        FrameLayout screen = new FrameLayout(this);
        screen.setBackground(background);

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER);
        panel.setPadding(dp(22), dp(14), dp(22), dp(18));
        GradientDrawable panelBackground = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] { 0xff182438, 0xff090e18 });
        panelBackground.setCornerRadius(dp(18));
        panelBackground.setStroke(dp(2), 0xffcad8ec);
        panel.setBackground(panelBackground);
        panel.setElevation(dp(8));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.avf_original_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setAdjustViewBounds(true);
        int maxLogoWidth = Math.min(dp(390), (int) (getResources().getDisplayMetrics().widthPixels * .64f));
        int maxLogoHeight = Math.min(dp(290), (int) (getResources().getDisplayMetrics().heightPixels * .49f));
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(maxLogoWidth, maxLogoHeight);
        imageParams.bottomMargin = dp(5);
        panel.addView(logo, imageParams);

        TextView eyebrow = new TextView(this);
        eyebrow.setText("UMA MENSAGEM ESPECIAL");
        eyebrow.setTextColor(0xffd9e7ff);
        eyebrow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        eyebrow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eyebrow.setLetterSpacing(.15f);
        eyebrow.setGravity(Gravity.CENTER);
        panel.addView(eyebrow, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView thanks = new TextView(this);
        thanks.setText("OBRIGADO, ZAPIA!");
        thanks.setTextColor(0xffffe29a);
        thanks.setTextSize(TypedValue.COMPLEX_UNIT_SP, 31);
        thanks.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        thanks.setGravity(Gravity.CENTER);
        thanks.setShadowLayer(dp(5), 0, dp(2), 0xff754116);
        LinearLayout.LayoutParams thanksParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        thanksParams.topMargin = dp(5);
        panel.addView(thanks, thanksParams);

        TextView stars = new TextView(this);
        stars.setText("✦   ✧   ✦");
        stars.setTextColor(0xfffff1c4);
        stars.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        stars.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams starsParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        starsParams.topMargin = dp(5);
        panel.addView(stars, starsParams);

        FrameLayout.LayoutParams panelParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        panelParams.leftMargin = dp(10);
        panelParams.rightMargin = dp(10);
        screen.addView(panel, panelParams);
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
