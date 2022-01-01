package net.freedynamicdns.samarths.smsforwarder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;


public class RulesManager {

    public static final String ALL_KEYS = "all_keys";
    public static final String PATTERN = "pattern";
    public static final String PHONE = "phone";

    public static void addRule(String pattern, String phone, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (keys == null) {
            keys = new HashSet<String>();
        }
        String uuid = UUID.randomUUID().toString();
        keys.add(uuid);
        e.putString(uuid+PATTERN, pattern);
        e.putString(uuid+PHONE, phone);
        e.putStringSet(ALL_KEYS, keys);
        e.commit();
    }

    public static void deleteRule(String uuid, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        Set<String> keys = sp.getStringSet(ALL_KEYS, null);
        if (!keys.contains(uuid)) {
            return;
        }

        e.remove(uuid+PATTERN).remove(uuid+PHONE);
        keys.remove(uuid);
        e.putStringSet(ALL_KEYS, keys);
        e.commit();
    }

    public static HashSet<Rule> getAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        HashSet<Rule> ret = new HashSet<>();
        for(String uuid : sp.getStringSet(ALL_KEYS, new HashSet<>())){
            Log.d("mylog", uuid);
            ret.add(new Rule(sp.getString(uuid+PATTERN,""),sp.getString(uuid+PHONE, ""), uuid));
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
}
