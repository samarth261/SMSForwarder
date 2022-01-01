package net.freedynamicdns.samarths.smsforwarder;

import android.telephony.SmsManager;

public class SendSms {

    public static void sendSMS(String phoneNumber, String body) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, body, null, null);
    }
}
