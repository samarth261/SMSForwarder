package net.freedynamicdns.samarths.smsforwarder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class RulesManager {

    public static final String ALL_KEYS = "all_keys";
    public static final String PATTERN = "pattern";
    public static final String PHONE = "phone";
    public static class Constants {
        public static final String SETTINGS_SHARED_PREFERENCES_NAME = "settings_sp";
        public static final String SMS_FORWARDING_ON = "sms_forwarding";
    }

    public static void addRule(String pattern, String phone, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (keys == null) {
            keys = new HashSet<String>();
        }
        String uuid = UUID.randomUUID().toString();
        keys.add(uuid);
        e.putString(uuid + PATTERN, pattern);
        e.putString(uuid + PHONE, phone);
        e.putStringSet(ALL_KEYS, keys);
        e.commit();
        AuditLogManager.AddNewAuditLog(context, "new Rule added.\n---------\n" + pattern + "\n---------\n" + phone, AuditLogManager.AuditTag.RULES_CHANGED);
    }

    public static void deleteRule(String uuid, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (!keys.contains(uuid)) {
            return;
        }

        String pat = sp.getString(uuid + PATTERN, "");
        String ph = sp.getString(uuid + PHONE, "");
        e.remove(uuid + PATTERN).remove(uuid + PHONE);
        keys.remove(uuid);
        e.putStringSet(ALL_KEYS, keys);
        e.commit();
        AuditLogManager.AddNewAuditLog(context, "Delete rule:\n------\n" + pat + "\n------\n" + ph, AuditLogManager.AuditTag.RULES_CHANGED);
    }

    public static HashSet<Rule> getAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        HashSet<Rule> ret = new HashSet<>();
        for (String uuid : sp.getStringSet(ALL_KEYS, new HashSet<>())) {
            Log.d("mylog", uuid);
            ret.add(new Rule(sp.getString(uuid + PATTERN, ""), sp.getString(uuid + PHONE, ""), uuid));
        }
        return ret;
    }

    public static class Rule {
        public String pattern;
        public String phone;
        public String uuid;

        public Rule(String pat, String ph, String id) {
            pattern = pat;
            phone = ph;
            uuid = id;
        }
    }

    public static boolean isSmsForwardingEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(Constants.SMS_FORWARDING_ON, false);
    }

    public static void setSmsForwardingEnabled(Context context, boolean target_value) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Constants.SMS_FORWARDING_ON, target_value).commit();
        if (target_value) {
            AuditLogManager.AddNewAuditLog(context, "Enable SMS forwarding", AuditLogManager.AuditTag.SMS_FORWARDING_SETTING);
        } else {
            AuditLogManager.AddNewAuditLog(context, "Disable SMS forwarding", AuditLogManager.AuditTag.SMS_FORWARDING_SETTING);
        }
    }
}
