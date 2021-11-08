package com.axel.otpretriver

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.axel.otpretriver.helpers.SmsBroadcastReceiver
import com.axel.otpretriver.helpers.SmsReceiverListener
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private val REQUEST_USER_CONSENT = 200
    var smsReceiverListener:SmsReceiverListener? = null
    var smsBroadcastReceiver:SmsBroadcastReceiver? = null
    var etOTP: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etOTP = findViewById(R.id.etOtp)
        startSmartUserConsent()
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_USER_CONSENT){

            if (resultCode == RESULT_OK && data != null){

                val message =data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMesage(message)
            }
        }
    }

    private fun getOtpFromMesage(message: String?) {

        val otpPatter = Pattern.compile("(|^) One Time Password:\\d(4)")
        val matcher = otpPatter.matcher(message)
        if (matcher.find()){
            etOTP!!.setText(matcher.group(0))
        }

    }

    private fun registerBroadcastReceiver(){
      smsReceiverListener = object : SmsReceiverListener{
            override fun onSuccess(intent: Intent) {
                startActivityForResult(intent,REQUEST_USER_CONSENT)
            }

            override fun onFailure() {

            }

        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver,intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }
}