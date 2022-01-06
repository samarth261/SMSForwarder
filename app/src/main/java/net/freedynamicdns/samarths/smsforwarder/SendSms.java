package net.freedynamicdns.samarths.smsforwarder;

import android.content.Context;
import android.telephony.SmsManager;

public class SendSms {

    public static void sendSMS(String phoneNumber, String body, Context context) {
        AuditLogManager.AddNewAuditLog(context, "Forwarding:\n" + body + "\nto " + phoneNumber, AuditLogManager.AuditTag.SMS_FORWARD);
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, body, null, null);
    }
}
