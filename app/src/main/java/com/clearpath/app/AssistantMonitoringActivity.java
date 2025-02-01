package com.clearpath.app;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AssistantMonitoringActivity extends AppCompatActivity {

    private final double destLat = 1.2966;
    private final double destLon = 103.8530; // Set destination coordinates
    private WebView webView;
    private LocationManager locationManager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_monitoring);

        // WebView setup
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.loadUrl("file:///android_asset/map.html");

        // Set up GPS tracking
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double currentLat = location.getLatitude();
                double currentLon = location.getLongitude();

                // Update WebView with real-time location
                runOnUiThread(() -> webView.evaluateJavascript(
                        "updateMap(" + currentLat + ", " + currentLon + ", " + destLat + ", " + destLon + ");", null
                ));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        });
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void updateLocation(double currentLat, double currentLon, double destLat, double destLon) {
            runOnUiThread(() -> webView.evaluateJavascript(
                    "updateMap(" + currentLat + ", " + currentLon + ", " + destLat + ", " + destLon + ");", null
            ));
        }
    }
}
