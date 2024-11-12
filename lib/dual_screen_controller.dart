import 'package:flutter/services.dart';

class DualScreenController {
  static const platform = MethodChannel('dual_screen_control');

  // Method to show content on the secondary screen
  Future<void> showOnSecondaryScreen(
      String widgetType, Map<String, dynamic> widgetData) async {
    try {
      await platform.invokeMethod('showOnSecondaryScreen', {
        'widgetType': widgetType,
        'widgetData': widgetData,
      });
    } on PlatformException catch (e) {
      print("Failed to display on secondary screen: '${e.message}'");
    }
  }

  // Method to show video on the secondary screen
  Future<void> showVideoOnSecondaryScreen(String videoAsset) async {
    try {
      await platform.invokeMethod('showVideoOnSecondaryScreen', {
        'videoAsset': videoAsset,
      });
    } on PlatformException catch (e) {
      print("Failed to display video on secondary screen: '${e.message}'");
    }
  }
}
