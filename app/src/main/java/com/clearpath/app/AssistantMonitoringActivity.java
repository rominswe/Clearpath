package com.clearpath.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class AssistantMonitoringActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the WebView
        setContentView(R.layout.activity_assistant_monitoring); // Set layout from XML
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/map.html");

        // Set up WebViewClient to handle navigation
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        // Optionally, handle JavaScript communication with Android
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Remove database reference logic
        // You can manually update the coordinates here or integrate other location update methods
        // Example coordinates (replace with dynamic updates as needed):
        double currentLat = 1.3521;
        double currentLon = 103.8198;
        double destLat = 1.2966;
        double destLon = 103.8530;

        // Update WebView with example coordinates
        WebAppInterface webAppInterface = new WebAppInterface(this);
        webAppInterface.updateLocation(currentLat, currentLon, destLat, destLon);
    }

    // JavaScript interface to communicate with the WebView
    public static class WebAppInterface {
        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void updateLocation(double currentLat, double currentLon, double destLat, double destLon) {
            WebView webView = ((Activity) context).findViewById(R.id.webView);
            webView.evaluateJavascript(
                    "updateMap(" + currentLat + ", " + currentLon + ", " + destLat + ", " + destLon + ");", null
            );
        }
    }
}