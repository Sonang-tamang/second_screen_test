package com.example.second_screen_test

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import android.app.Presentation
import android.os.Bundle
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
                showOnSecondaryScreen()
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun showOnSecondaryScreen() {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            val secondaryDisplay = displays[1]
            val presentation = MyPresentation(this, secondaryDisplay)
            presentation.show()
        }
    }
}

class MyPresentation(context: Context, display: Display) : Presentation(context, display) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_normal) // Use the layout specified by the SDK for secondary display
    }
}

