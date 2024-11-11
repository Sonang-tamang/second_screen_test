package com.example.second_screen_test

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import android.app.Presentation
import android.os.Bundle
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import android.view.Gravity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {
    private val CHANNEL = "dual_screen_control"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "showOnSecondaryScreen") {
                val widgetType = call.argument<String>("widgetType") ?: "Container"
                val widgetData = call.argument<Map<String, String>>("widgetData") ?: mapOf()
                showOnSecondaryScreen(widgetType, widgetData)
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun showOnSecondaryScreen(widgetType: String, widgetData: Map<String, String>) {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            val secondaryDisplay = displays[1]
            val presentation = MyPresentation(this, secondaryDisplay, widgetType, widgetData)
            presentation.show()
        }
    }
}

class MyPresentation(
    context: Context,
    display: Display,
    private val widgetType: String,
    private val widgetData: Map<String, String>
) : Presentation(context, display) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a vertical LinearLayout to hold content
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor(widgetData["backgroundColor"] ?: "#FFFFFF"))
        }

        // Create and configure a TextView for the text
        val textView = TextView(context).apply {
            text = widgetData["text"] ?: "Secondary Screen"
            setTextColor(Color.BLACK)
            textSize = 24f
            gravity = Gravity.CENTER
        }

        // Optionally add an icon if specified
        val iconView = ImageView(context).apply {
            setImageResource(android.R.drawable.ic_btn_speak_now) // Default "mic" icon
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 20
            }
        }

        // Add views to the layout based on widget type
        layout.addView(textView)
        if (widgetType == "Container" && widgetData["icon"] == "mic") {
            layout.addView(iconView)
        }

        // Set the layout as the content view for this presentation
        setContentView(layout)
    }
}
