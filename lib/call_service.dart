import 'package:call_service/call_duration_model.dart';
import 'package:flutter/services.dart';

class CallService {
  static const MethodChannel _channel = MethodChannel('call_service');

  Future<CallDurationModel> makeCall(String phoneNumber) async {
    try {
      final Map<dynamic, dynamic> result = await _channel.invokeMethod(
        'makeCall',
        {'phoneNumber': phoneNumber},
      );
      return CallDurationModel.fromMap(result);
    } on PlatformException catch (e) {
      print("Error: ${e.message}");
      return CallDurationModel();
    }
  }
}
