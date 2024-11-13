import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'dual_screen_controller.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final DualScreenController dualScreenController = DualScreenController();

  @override
  void initState() {
    super.initState();
    // Start the video automatically when the app launches
    WidgetsBinding.instance.addPostFrameCallback((_) {
      dualScreenController.showVideoOnSecondaryScreen("sample_video");
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Dual Screen Test')),
      body: Center(
        child: InkWell(
          onTap: () {
            Navigator.of(context).push(
              MaterialPageRoute(builder: (context) => Webview()),
            );
          },
          child: Card(
            color: Colors.red,
            child: SizedBox(
              width: 200,
              height: 200,
              child: Center(
                child: Text(
                  'go to web',
                  style: TextStyle(color: Colors.white),
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class Webview extends StatefulWidget {
  const Webview({super.key});

  @override
  State<Webview> createState() => _WebviewState();
}

class _WebviewState extends State<Webview> {
  final controller = WebViewController()
    ..setJavaScriptMode(JavaScriptMode.unrestricted)
    ..loadRequest(Uri.parse("https://www.youtube.com/"));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("WebView")),
      body: SafeArea(child: WebViewWidget(controller: controller)),
    );
  }
}
