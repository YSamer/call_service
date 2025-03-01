class CallDurationModel {
  int totalCallDuration;
  int activeCallDuration;
  CallDurationModel(
      {this.totalCallDuration = 0, this.activeCallDuration = 0});

  int get ringDuration => totalCallDuration - activeCallDuration;
  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'totalCallDuration': totalCallDuration,
      'activeCallDuration': activeCallDuration,
    };
  }

  factory CallDurationModel.fromMap(Map<dynamic, dynamic> map) {
    return CallDurationModel(
      totalCallDuration: map['totalCallDuration'] != null
          ? map['totalCallDuration'] as int
          : 0,
      activeCallDuration: map['activeCallDuration'] != null
          ? map['activeCallDuration'] as int
          : 0,
    );
  }
}
