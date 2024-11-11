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
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Card(
                color: Colors.red,
                child: SizedBox(
                  width: 200,
                  height: 200,
                  child: Center(
                    child: Text(
                      'Primary Screen - Red Card',
                      style: TextStyle(color: Colors.white),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: () async {
                  // Define widget data for secondary screen
                  await dualScreenController.showOnSecondaryScreen(
                    "Container",
                    {
                      "backgroundColor": "#FFFF00",
                      "text": "Secondary Screen - Yellow Container",
                      "icon": "mic",
                    },
                  );
                },
                child: Text('Show on Secondary Screen'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
