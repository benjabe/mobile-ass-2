package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PreferenceActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    EditText mTxtUrl;
    Spinner mSpnItemLimit;
    Spinner mSpnRefreshTime;
    Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        mTxtUrl = findViewById(R.id.txt_url);
        mBtnSave = findViewById(R.id.btn_save);

        // populate spinner
        mSpnItemLimit = findViewById(R.id.spn_item_limit);
        ArrayAdapter<CharSequence> limitAdapter
                = ArrayAdapter.createFromResource(
                        this,
                        R.array.item_limit_array,
                        android.R.layout.simple_spinner_item
        );
        limitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mSpnItemLimit.setAdapter(limitAdapter);

        // populate other spinner
        mSpnRefreshTime = findViewById(R.id.spn_refresh_time);
        ArrayAdapter<CharSequence> refreshAdapter
                = ArrayAdapter.createFromResource(
                        this,
                        R.array.refresh_time_array,
                        android.R.layout.simple_spinner_item
        );
        refreshAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mSpnRefreshTime.setAdapter(refreshAdapter);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String url = mPreferences.getString("url", "");
        mTxtUrl.setText(url);
        mEditor.apply();

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString("url", mTxtUrl.getText().toString());
                mEditor.putInt(
                        "itemLimit",
                        Integer.parseInt(
                                mSpnItemLimit.getSelectedItem().toString()
                        )
                );
                mEditor.putInt(
                        "refreshTime",
                        refreshOptionToMilliseconds(
                                mSpnRefreshTime.getSelectedItem().toString()
                        )
                );
                mEditor.apply();
                Intent intent = new Intent(
                        PreferenceActivity.this,
                        MainActivity.class
                );
                intent.putExtra("url", mTxtUrl.getText().toString().trim());
                setResult(0, intent);
                finish();
            }
        });
    }

    private int refreshOptionToMilliseconds(String option) {
        Log.d("refresh shit", "refreshOptionToMilliseconds: " + option.trim());
        switch (option.trim()) {
            case "10 min":
                return 600000;
            case "60 min":
                return 3600000;
            case "Once a day":
                return 86400000;
            default:
                return 600000;
        }
    }
}
