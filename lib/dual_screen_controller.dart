import 'package:flutter/services.dart';

class DualScreenController {
  static const platform = MethodChannel('dual_screen_control');

  Future<void> showOnSecondaryScreen() async {
    try {
      await platform.invokeMethod('showOnSecondaryScreen');
    } on PlatformException catch (e) {
      print("Failed to display on secondary screen: '${e.message}'.");
    }
  }
}
