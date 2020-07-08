package com.iniyan.autootpreceiver.listener;

import android.content.Intent;

public interface OTPListener {

    public void otpReceived(String messageText);


    void onFailure();
}
