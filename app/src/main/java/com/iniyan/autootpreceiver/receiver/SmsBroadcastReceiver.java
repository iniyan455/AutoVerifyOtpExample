package com.iniyan.autootpreceiver.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.iniyan.autootpreceiver.listener.OTPListener;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    /**
     * Constant TAG for logging key.
     */
    private static final String TAG = "OtpReader";


    public static OTPListener otpListener;

    private static String receiverString;

    public static void bind(OTPListener listener, String sender) {
        otpListener = listener;
        receiverString = sender;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("Otp")){

            Log.e(TAG,"onBroadCast Working"+intent.getAction());


            final Bundle bundle = intent.getExtras();
         //   otpListener.otpReceived("your otp is ; 787878");
            if (bundle != null) {
                final Object[] pdusArr = (Object[]) bundle.get("pdus");
              //  otpListener.otpReceived("Your otp is 213456");

                for (int i = 0; i < pdusArr.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i]);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i(TAG, "senderNum: " + senderNum + " message: " + message);


                    if (!TextUtils.isEmpty(receiverString) && senderNum.contains(receiverString)) { //If message received is from required number.
                        //If bound a listener interface, callback the overriden method.
                        if (otpListener != null) {
                            otpListener.otpReceived(message);
                        }
                    }
                }
            }
        } else  {
            Log.e(TAG,"onBroadCast Working failure"+intent.getAction());
            otpListener.onFailure();
        }

    }

    /**
     * Unbinds the sender string and listener for callback.
     */
    public static void unbind() {
        otpListener = null;
        receiverString = null;
    }
}