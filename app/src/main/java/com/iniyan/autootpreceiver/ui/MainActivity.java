package com.iniyan.autootpreceiver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.iniyan.autootpreceiver.R;
import com.iniyan.autootpreceiver.listener.OTPListener;
import com.iniyan.autootpreceiver.receiver.SmsBroadcastReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OTPListener {
    SmsBroadcastReceiver smsBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmsBroadcastReceiver.bind(this,"9894591650");
      //  sendSMS("9894591650","your otp is 787878");
    }

    @Override
    public void otpReceived(String messageText) {
        //Do whatever you want to do with the text
        Toast.makeText(this,"Got "+messageText, Toast.LENGTH_LONG).show();
        Log.d("Otp",messageText);

        getOtpFromMessage(messageText);
    }

    @Override
    public void onFailure() {
        SmsBroadcastReceiver.unbind();
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            EditText otp= findViewById(R.id.editTextOTP);
            otp.setText(matcher.group(0));
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter("Otp");
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SmsBroadcastReceiver.unbind();
        unregisterReceiver(smsBroadcastReceiver);

    }
}