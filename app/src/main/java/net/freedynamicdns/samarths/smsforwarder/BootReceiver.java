package net.freedynamicdns.samarths.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AuditLogManager.AddNewAuditLog(context, "System Boot broadcast received", AuditLogManager.AuditTag.BOOT);
    }
}