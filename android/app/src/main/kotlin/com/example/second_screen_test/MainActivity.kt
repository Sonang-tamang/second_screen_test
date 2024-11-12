package com.example.second_screen_test

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import android.app.Presentation
import android.os.Bundle
import android.net.Uri
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import android.view.Gravity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.FlutterEngine
import android.widget.RelativeLayout
import android.view.ViewGroup
import android.media.MediaPlayer

class MainActivity : FlutterActivity() {
    private val CHANNEL = "dual_screen_control"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "showOnSecondaryScreen" -> {
                    val widgetType = call.argument<String>("widgetType") ?: "Container"
                    val widgetData = call.argument<Map<String, String>>("widgetData") ?: mapOf()
                    showOnSecondaryScreen(widgetType, widgetData)
                    result.success(null)
                }
                "showVideoOnSecondaryScreen" -> {
                    val videoAsset = call.argument<String>("videoAsset") ?: ""
                    showVideoOnSecondaryScreen(videoAsset)
                    result.success(null)
                }
                else -> result.notImplemented()
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

    private fun showVideoOnSecondaryScreen(videoAsset: String) {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            val secondaryDisplay = displays[1]
            val presentation = VideoPresentation(this, secondaryDisplay, videoAsset)
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

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor(widgetData["backgroundColor"] ?: "#FFFFFF"))
        }

        val textView = TextView(context).apply {
            text = widgetData["text"] ?: "Secondary Screen"
            setTextColor(Color.BLACK)
            textSize = 24f
            gravity = Gravity.CENTER
        }

        layout.addView(textView)
        setContentView(layout)
    }
}

class VideoPresentation(
    context: Context,
    display: Display,
    private val videoAsset: String
) : Presentation(context, display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create VideoView to play the video
        val videoView = VideoView(context).apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/$videoAsset")
            setVideoURI(videoUri)
            setBackgroundColor(Color.BLACK)
            layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Start the video automatically
        videoView.setOnPreparedListener { mediaPlayer ->
            adjustVideoAspect(videoView, mediaPlayer.videoWidth, mediaPlayer.videoHeight)
            videoView.start()
        }

        // Use RelativeLayout as the parent container
        val layout = RelativeLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.BLACK)
        }

        // Add the VideoView to the layout
        layout.addView(videoView)
        setContentView(layout)
    }

    // Function to maintain the video aspect ratio
    private fun adjustVideoAspect(videoView: VideoView, videoWidth: Int, videoHeight: Int) {
        val containerWidth = videoView.width
        val containerHeight = videoView.height

        if (videoWidth > 0 && videoHeight > 0 && containerWidth > 0 && containerHeight > 0) {
            val videoAspectRatio = videoWidth.toFloat() / videoHeight
            val containerAspectRatio = containerWidth.toFloat() / containerHeight

            val layoutParams = videoView.layoutParams as RelativeLayout.LayoutParams
            if (videoAspectRatio > containerAspectRatio) {
                layoutParams.width = containerWidth
                layoutParams.height = (containerWidth / videoAspectRatio).toInt()
            } else {
                layoutParams.width = (containerHeight * videoAspectRatio).toInt()
                layoutParams.height = containerHeight
            }
            videoView.layoutParams = layoutParams
        }
    }
}
