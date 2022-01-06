package net.freedynamicdns.samarths.smsforwarder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AuditLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_log);

        PopulateAuditLogs();

        findViewById(R.id.clearallauditbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuditLogManager.ClearAll(getApplicationContext());
                PopulateAuditLogs();
            }
        });
    }

    private void PopulateAuditLogs() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.auditloglinearlayout);
        ll.removeAllViews();

        Comparator<AuditLogManager.AuditLog> c = new Comparator<AuditLogManager.AuditLog>() {
            @Override
            public int compare(AuditLogManager.AuditLog auditLog, AuditLogManager.AuditLog t1) {
                return (int) (t1.time - auditLog.time);
            }
        };
        List<AuditLogManager.AuditLog> all = AuditLogManager.GetAllAuditLogs(getApplicationContext());
        Collections.sort(all, c);

        SimpleDateFormat df = new SimpleDateFormat("k:m:s dd/MM/yyyy");
        for (final AuditLogManager.AuditLog log : all) {
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.auditlogitem, null);
            ((TextView)v.findViewById(R.id.auditmessage)).setText(log.logMessage);
            ((TextView)v.findViewById(R.id.auditdate)).setText(df.format(new Date(log.time)));
            View b = ((Button) findViewById(R.id.auditlogitemdeletebtn));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ll.addView(v);
        }
    }
}