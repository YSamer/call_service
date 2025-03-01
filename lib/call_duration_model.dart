class CallDurationModel {
  int callDuration;
  int lastCallDuration;
  CallDurationModel({this.callDuration = 0, this.lastCallDuration = 0});

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'callDuration': callDuration,
      'lastCallDuration': lastCallDuration,
    };
  }

  factory CallDurationModel.fromMap(Map<dynamic, dynamic> map) {
    return CallDurationModel(
      callDuration:
          map['callDuration'] != null ? map['callDuration'] as int : 0,
      lastCallDuration:
          map['lastCallDuration'] != null ? map['lastCallDuration'] as int : 0,
    );
  }
}
