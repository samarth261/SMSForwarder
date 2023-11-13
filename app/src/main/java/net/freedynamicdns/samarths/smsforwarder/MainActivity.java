package net.freedynamicdns.samarths.smsforwarder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.api.client.auth.oauth2.Credential;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuditLogManager.AddNewAuditLog(getApplicationContext(), "App opened", AuditLogManager.AuditTag.APP_LIFE_CYCLE);

        mExecutor = Executors.newSingleThreadExecutor();
        findViewById(R.id.vieweditrules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RulesActivity.class));
            }
        });

        // Set the onclick listener for viewing the Audits.
        findViewById(R.id.viewauditbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AuditLogActivity.class));
            }
        });

        // Set the on click listener for the email btn.
        findViewById(R.id.sendmailbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Credential cred = EmailHelper.GetCredentials(getApplicationContext());
                        } catch (Exception ex) {
                            Log.d("mylog exception:", ex.getMessage());
                        }
                    }
                };
                //mExecutor.execute(runnable);

            }
        });

        // Request permission to send sms and also to access the contacts.
        ActivityResultLauncher<String> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Log.d("mylog", "granted");
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Log.d("mylog", "failed to grant");
                        requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS}, 1000);
                    }
                });
        activityResultLauncher.launch(Manifest.permission.GET_ACCOUNTS);
        Log.d("mylog", "has perm: " + (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED));

        // Toggle switch for the feature.
        Switch forwarding_switch = (Switch) findViewById(R.id.forawarding_toggle_switch);
        forwarding_switch.setChecked(RulesManager.isSmsForwardingEnabled(this));
        forwarding_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final String toast_message = b ? "Forwarding enabled" : "Forwarding disabled";
                Toast.makeText(MainActivity.this, toast_message , Toast.LENGTH_SHORT).show();
                RulesManager.setSmsForwardingEnabled(getApplicationContext(), b);
            }
        });

        // The experimental features being tested.
        findViewById(R.id.write_to_file_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Buttin clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ExecutorService mExecutor;
}