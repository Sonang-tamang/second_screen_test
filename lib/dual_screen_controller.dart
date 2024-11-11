import 'package:flutter/services.dart';

class DualScreenController {
  static const platform = MethodChannel('dual_screen_control');

  Future<void> showOnSecondaryScreen(
      String widgetType, Map<String, dynamic> widgetData) async {
    try {
      await platform.invokeMethod('showOnSecondaryScreen', {
        'widgetType': widgetType,
        'widgetData': widgetData,
      });
    } on PlatformException catch (e) {
      print("Failed to display on secondary screen: '${e.message}'.");
    }
  }
}
