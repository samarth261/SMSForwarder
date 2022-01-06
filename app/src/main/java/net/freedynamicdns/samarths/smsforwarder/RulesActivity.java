package net.freedynamicdns.samarths.smsforwarder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout);

        ScrapAndPopulateView(ll);

        findViewById(R.id.addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pat = ((TextInputEditText)findViewById(R.id.newpattern)).getText().toString();
                String ph = ((TextInputEditText) findViewById(R.id.newnumber)).getText().toString();
                Toast.makeText(getApplicationContext(), pat + " " + ph, Toast.LENGTH_SHORT).show();
                RulesManager.addRule(pat, ph, getApplicationContext());
                ((EditText)findViewById(R.id.newpattern)).setText("");
                ((EditText)findViewById(R.id.newnumber)).setText("");
                ScrapAndPopulateView(ll);
            }
        });

    }

    private void ScrapAndPopulateView(LinearLayout ll) {
        ll.removeAllViews();
        Context context = getApplicationContext();
        HashSet<RulesManager.Rule> all = RulesManager.getAll(context);
        for(RulesManager.Rule rule : all){
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.ruleitem,null);
            ((TextView)v.findViewById(R.id.pattern)).setText(rule.pattern);
            v.findViewById(R.id.pattern).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pattern", rule.pattern);
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied pattern to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            ((TextView)v.findViewById(R.id.phone)).setText(rule.phone);
            v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RulesManager.deleteRule(rule.uuid, context);
                    ScrapAndPopulateView(ll);
                }
            });
            ll.addView(v);
        }
    }
}