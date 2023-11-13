package net.freedynamicdns.samarths.smsforwarder;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.GmailScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class EmailHelper {

    public static Credential GetCredentials(Context context) throws IOException, GeneralSecurityException {
        // TODO: Look into some saved state to get the credentials.
        Log.d("mylog", "Reached here");
        InputStream in = context.getResources().openRawResource(R.raw.gcpappcreds);
        if (in == null) {
            Log.d("mylog", "fk");
            return null;
        }
        AccountManager am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Bundle options = new Bundle();
        Account[] accs = am.getAccounts();
        Log.d("mylog", "length" + accs.length);
//        am.getAuthToken(
//                myAccount_,                     // Account retrieved using getAccountsByType()
//                "Manage your tasks",            // Auth scope
//                options,                        // Authenticator-specific options
//                context,                           // Your activity
//                new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//                new Handler(new OnError()));    // Callback called if an error occurs
        return null;
    }
}
