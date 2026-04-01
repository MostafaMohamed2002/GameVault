package com.mostafadevo.freegames.ui.screens.privacy_policy

import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyPolicyScreen() {
    val context = LocalContext.current
    var html by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        html = context.assets.open("privacy-policy.md").bufferedReader().use { it.readText() }
            .let { markdownToSimpleHtml(it) }
    }

    if (html == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = false
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    loadDataWithBaseURL(
                        null,
                        html ?: "",
                        "text/html",
                        "utf-8",
                        null
                    )
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, html ?: "", "text/html", "utf-8", null)
            }
        )
    }
}

private fun markdownToSimpleHtml(markdown: String): String {
    val body = markdown
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace(Regex("\\[(.+?)\\]\\((.+?)\\)"), "<a href=\"$2\">$1</a>")
        .replace(Regex("^# (.+)$", RegexOption.MULTILINE), "<h1>$1</h1>")
        .replace(Regex("^## (.+)$", RegexOption.MULTILINE), "<h2>$1</h2>")
        .replace("\n", "<br/>")

    return """
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1" />
          <style>
            body { font-family: sans-serif; padding: 16px; line-height: 1.5; }
            h1, h2 { color: #1f1f1f; }
            a { color: #2962ff; }
          </style>
        </head>
        <body>$body</body>
        </html>
    """.trimIndent()
}
