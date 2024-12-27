package com.example.tokyo_adult_guide

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.ComponentActivity
import com.example.tokyo_adult_guide.R

data class WebsiteOption(val placeholder: String, val url: String)

class MainActivity : ComponentActivity() {
    private val websiteOptions = listOf(
        WebsiteOption("Website 1 (https://example.com)", "https://example.com"),
        WebsiteOption("Website 2 (https://google.com)", "https://google.com")
    )

    private lateinit var webView: WebView
    private var initialUrl: String? = null
    private var isOnInitialScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val submitButton: Button = findViewById(R.id.submitButton)

        // Dynamically add RadioButtons
        websiteOptions.forEachIndexed { index, option ->
            val radioButton = RadioButton(this).apply {
                id = ViewGroup.generateViewId()
                text = option.placeholder
            }
            radioGroup.addView(radioButton)
        }

        submitButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedRadioButton: RadioButton = findViewById(selectedId)
            val selectedOption = websiteOptions.find { it.placeholder == selectedRadioButton.text }
            selectedOption?.let {
                initialUrl = it.url
                loadWebView(it.url)
                isOnInitialScreen = false
            }
        }
    }

    private fun loadWebView(url: String) {
        setContentView(R.layout.webview_layout)
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (isOnInitialScreen) {
            super.onBackPressed()
        } else if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            setContentView(R.layout.activity_main)
            recreate()
            isOnInitialScreen = true
        }
    }
}