/*
 * This class will help manage the audit logs.
 * Add new audit log message.
 * Delete a particular audit log.
 * Clear all audit logs.
 * Fetch all audit logs.
 *
 * The audit log manager uses shared preferences to store the audit logs.
 *
 */

package net.freedynamicdns.samarths.smsforwarder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Timer;
import java.util.UUID;

public class AuditLogManager {

    public static String AUDIT_LOG_SHARED_PREFERENCES = "audit_logs_sp";
    public static String ALL_KEYS = "all_keys"; // This will be used to store the set of all other keys in shared preferences.
    public static String LOG_MESSAGE = "log_message";
    public static String TAG = "tag";
    public static String TIME = "time";

    public enum AuditTag{
        SMS_RECEIVED_TAG,
        APP_LIFE_CYCLE,
        RULES_CHANGED,
        SMS_FORWARD,
        SMS_MATCHED,
        BOOT
    }

    private AuditLogManager(){}

    public static void AddNewAuditLog(Context context, AuditLog auditLog) {
        SharedPreferences sp = context.getSharedPreferences(AUDIT_LOG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (keys == null) {
            keys = new HashSet<String>();
        }
        String uuid = UUID.randomUUID().toString();
        keys.add(uuid);

        // Now store the audit log object into editor.
        editor.putString(uuid+LOG_MESSAGE, auditLog.logMessage);
        editor.putString(uuid+TAG, auditLog.auditTag.toString());
        editor.putLong(uuid+TIME, auditLog.time);
        editor.putStringSet(ALL_KEYS, keys);
        editor.commit();
    }

    public static void AddNewAuditLog(Context context, String log_message, AuditTag tag){
        AddNewAuditLog(context, new AuditLog(log_message, tag, System.currentTimeMillis()));
    }

    public static List<AuditLog> GetAllAuditLogs(Context context) {
        List<AuditLog> ret = new ArrayList<>();

        SharedPreferences sp = context.getSharedPreferences(AUDIT_LOG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (keys == null) {
            return ret;
        }
        for(String key : keys){
            AuditLog log = new AuditLog(
              sp.getString(key+LOG_MESSAGE, ""),
              AuditTag.valueOf(sp.getString(key+TAG,"" )),
              sp.getLong(key+TIME, 0)
            );
            log.uuid = key;
            ret.add(log);
        }

        return ret;
    }

    public static void DeleteAuditLog(Context context, String uuid) {
        SharedPreferences sp = context.getSharedPreferences(AUDIT_LOG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (keys == null) {
            return;
        }
        if (!keys.contains(uuid)) {
            return;
        }

        keys.remove(uuid);
        editor.remove(uuid + LOG_MESSAGE);
        editor.remove(uuid + TAG);
        editor.remove(uuid + TIME);
        editor.putStringSet(ALL_KEYS, keys);

        editor.commit();
    }

    public static void ClearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUDIT_LOG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    public static class AuditLog{
        public String logMessage;
        public AuditTag auditTag;
        public Long time;
        public String uuid;

        public AuditLog(String log_message, AuditTag tag, Long time) {
            logMessage = log_message;
            auditTag = tag;
            this.time = time;
        }
    }
}
