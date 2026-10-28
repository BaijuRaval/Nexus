package com.baijuraval.nexus

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    private lateinit var webView: WebView

    private val hostedHtmlUrl =
        "https://raw.githubusercontent.com/BaijuRaval/Nexus/main/app/src/main/assets/index.html"
    private val htmlBaseUrl =
        "https://raw.githubusercontent.com/BaijuRaval/Nexus/main/app/src/main/assets/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(3, 7, 17)
        window.navigationBarColor = Color.rgb(3, 7, 17)
        if (android.os.Build.VERSION.SDK_INT >= 30) window.setDecorFitsSystemWindows(true)

        webView = WebView(this).apply {
            setBackgroundColor(Color.rgb(3, 7, 17))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mediaPlaybackRequiresUserGesture = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }

        setContentView(webView)
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            loadHostedHtml()
        }
    }

    /**
     * Fetch HTML as text, then explicitly tell WebView to parse it as HTML.
     * This avoids showing the source code if the hosting server returns text/plain.
     * The bundled page is used as an offline fallback.
     */
    private fun loadHostedHtml() {
        Thread {
            val html = try {
                val requestUrl = URL("$hostedHtmlUrl?nocache=${System.currentTimeMillis()}")
                val connection = requestUrl.openConnection() as HttpURLConnection
                connection.connectTimeout = 12000
                connection.readTimeout = 12000
                connection.setRequestProperty("Cache-Control", "no-cache")
                connection.setRequestProperty("Pragma", "no-cache")
                try {
                    if (connection.responseCode !in 200..299) {
                        throw IllegalStateException("HTML download failed: HTTP ${connection.responseCode}")
                    }
                    connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                } finally {
                    connection.disconnect()
                }
            } catch (_: Exception) {
                assets.open("index.html").bufferedReader(Charsets.UTF_8).use { it.readText() }
            }

            runOnUiThread {
                if (!isFinishing && !isDestroyed) {
                    webView.loadDataWithBaseURL(
                        htmlBaseUrl,
                        html,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        }.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android SDK")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.removeAllViews()
            webView.destroy()
        }
        super.onDestroy()
    }
}
