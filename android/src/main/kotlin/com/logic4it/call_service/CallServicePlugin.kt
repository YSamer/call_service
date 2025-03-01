package com.logic4it.call_service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.telephony.TelephonyManager
import android.telephony.PhoneStateListener

class CallServicePlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private var callStartTime: Long = 0
    private var callHandled = false
    private lateinit var telephonyManager: TelephonyManager
    private var phoneStateListener: PhoneStateListener? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "call_service")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "makeCall" -> {
                val phoneNumber = call.argument<String>("phoneNumber")
                if (phoneNumber.isNullOrEmpty()) {
                    result.error("INVALID_ARGUMENT", "Phone number is required", null)
                } else {
                    makePhoneCall(phoneNumber, result)
                }
            }
            else -> result.notImplemented()
        }
    }

    private fun makePhoneCall(phoneNumber: String, result: Result) {
        callHandled = false
        callStartTime = 0

        // Register Phone State Listener
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        callStartTime = System.currentTimeMillis()
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        if (callStartTime > 0 && !callHandled) {
                            callHandled = true
                            val callDuration = (System.currentTimeMillis() - callStartTime) / 1000
                            val lastCallDuration = getLastCallDuration() / 1000

                            result.success(
                                mapOf(
                                    "callDuration" to callDuration,
                                    "lastCallDuration" to lastCallDuration
                                )
                            )

                            unregisterPhoneStateListener()
                        }
                    }
                }
            }
        }

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

        // Start Call Intent
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(callIntent)
        } catch (e: SecurityException) {
            unregisterPhoneStateListener()
            result.error("PERMISSION_DENIED", "Call permission denied", null)
        } catch (e: Exception) {
            unregisterPhoneStateListener()
            result.error("CALL_FAILED", "Failed to start call: ${e.localizedMessage}", null)
        }
    }

    private fun getLastCallDuration(): Long {
        return try {
            context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.DURATION),
                null, null, "${CallLog.Calls.DATE} DESC"
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)) * 1000
                } else {
                    0
                }
            } ?: 0
        } catch (e: SecurityException) {
            0
        }
    }

    private fun unregisterPhoneStateListener() {
        phoneStateListener?.let {
            telephonyManager.listen(it, PhoneStateListener.LISTEN_NONE)
            phoneStateListener = null
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        unregisterPhoneStateListener()
    }
}
