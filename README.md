# Call Service Plugin for Flutter

A Flutter plugin that provides phone call functionality with call duration tracking.

## Installation

Add the following dependencies to your `pubspec.yaml`:

```yaml
dependencies:
  permission_handler: ^11.4.0
  call_service:
    git:
      url: git@github.com:YSamer/call_service.git
```

## Permissions

Ensure the following permissions are added to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CALL_PHONE"/>
<uses-permission android:name="android.permission.READ_CALL_LOG"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```

## Usage

### Request Permissions

Before making a call, request the necessary permissions:

```dart
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  await [Permission.phone, Permission.phone].request();
}
```

### Making a Call

Below is an example Flutter application using `call_service`:

```dart
import 'dart:async';
import 'package:call_service/call_service.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

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
                decoration: InputDecoration(
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
                  style: TextStyle(
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
```

## Generating an SSH Key and Sending It via WhatsApp

### Step 1: Generate an SSH Key
Run the following command in your terminal:

```sh
ssh-keygen -t rsa -b 4096 -C "your-email@example.com"
```
Press **Enter** to save it in the default location (`~/.ssh/id_rsa`).

### Step 2: Copy Your Public Key
Run:

```sh
cat ~/.ssh/id_rsa.pub
```
Copy the output, which starts with `ssh-rsa`.

### Step 3: Send the SSH Key via WhatsApp
Send the copied SSH key to the repository owner via WhatsApp at:
📞 **+201097816172**

Once the owner adds your key to GitHub, you will be able to access the repository securely.

## Notes
- Ensure that your app has permission to make phone calls.
- Call logs access might require additional setup on some Android versions.

## License
Copyright (c) 2025 Logic4IT by Yahya Samir.

