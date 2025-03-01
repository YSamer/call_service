# call_service

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/to/develop-plugins),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

to setup
1 - Add this packages

  permission_handler: ^11.4.0
  call_service:
    git:
      url: https://github.com/YSamer/call_service.git

2 - Add this permissions
    <uses-permission android:name="android.permission.CALL_PHONE"/>
    <uses-permission android:name="android.permission.READ_CALL_LOG"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>

3 - use example code

import 'package:call_service/call_service.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  await [Permission.phone, Permission.phone].request();
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _callServicePlugin = CallService();
  final TextEditingController _phoneController = TextEditingController();
  String callDuration = '0';
  String ringDuration = '0';
  String errorMessage = '';

  Future<void> makeCall() async {
    try {
      await requestPermissions();
      final durations = await _callServicePlugin.makeCall(
        _phoneController.text,
      );

      setState(() {
        callDuration = durations.activeCallDuration.toString();
        ringDuration = durations.ringDuration.toString();
        errorMessage = '';
      });
    } on PlatformException catch (e) {
      setState(() {
        errorMessage = "Error: ${e.message}";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Phone Call Service')),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              TextField(
                controller: _phoneController,
                keyboardType: TextInputType.phone,
                decoration: const InputDecoration(
                  labelText: "Enter Phone Number",
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.phone),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: makeCall,
                child: const Text("Make Call"),
              ),
              const SizedBox(height: 20),
              if (errorMessage.isNotEmpty)
                Text(
                  errorMessage,
                  style: const TextStyle(
                    color: Colors.red,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              Text("Call Duration: $callDuration sec"),
              Text("Ring Duration: $ringDuration sec"),
            ],
          ),
        ),
      ),
    );
  }
}

