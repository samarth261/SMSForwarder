package net.freedynamicdns.samarths.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.regex.Pattern;

public class NewSmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("mylog", "got the intent");

        if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }
        AuditLogManager.AddNewAuditLog(context, "Received sms", AuditLogManager.AuditTag.SMS_RECEIVED_TAG);

        if(!RulesManager.isSmsForwardingEnabled(context)) {
            AuditLogManager.AddNewAuditLog(context, "Skip forwarding", AuditLogManager.AuditTag.SMS_FORWARD);
            return;
        }

        // The intent is a new SMS received.
        Bundle bundle = intent.getExtras();
        for (String s : bundle.keySet()) {
            Log.d("mylog-", s);
            Log.d("mylog", bundle.get(s).toString());
        }

        if (bundle == null) {
            Log.d("mylog", "bundle is null?");
        }

        // Bundle is not null. So follow the normal execution.
        SmsMessage msg = null;
        String msg_from;
        String subcriptionIndex = bundle.get("android.telephony.extra.SUBSCRIPTION_INDEX").toString();
        try {
            Object pdus[] = (Object[])  bundle.get("pdus");
            for(int i = 0; i < pdus.length; i++){
                msg = SmsMessage.createFromPdu((byte[])pdus[i]);
                String msgBody = msg.getMessageBody();
                Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();
                HashSet<RulesManager.Rule> all = RulesManager.getAll(context);
                for (RulesManager.Rule rule : all) {
                    if (Pattern.matches(rule.pattern, msgBody)) {
                        AuditLogManager.AddNewAuditLog(context, "SMS matches pattern:\n" + rule.pattern, AuditLogManager.AuditTag.SMS_MATCHED);
                        SendSms.sendSMS(rule.phone, msgBody, context);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("mylog", e.getMessage());
        }

    }
}