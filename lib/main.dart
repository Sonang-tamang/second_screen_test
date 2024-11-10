import 'package:flutter/material.dart';
import 'dual_screen_controller.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final DualScreenController dualScreenController = DualScreenController();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Dual Screen Test')),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              await dualScreenController.showOnSecondaryScreen();
            },
            child: Text('Show on Secondary Screen'),
          ),
        ),
      ),
    );
  }
}
