package net.freedynamicdns.samarths.smsforwarder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout);

        ScrapAndPopulateView(ll);

        findViewById(R.id.addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pat = ((TextInputEditText)findViewById(R.id.newpattern)).getText().toString();
                String ph = ((TextInputEditText) findViewById(R.id.newnumber)).getText().toString();
                Toast.makeText(getApplicationContext(), pat + " " + ph, Toast.LENGTH_SHORT).show();
                RulesManager.addRule(pat, ph, getApplicationContext());
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