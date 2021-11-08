package com.axel.otpretriver.helpers

import android.content.Intent

interface SmsReceiverListener {

    fun onSuccess(intent: Intent)

    fun onFailure()
}