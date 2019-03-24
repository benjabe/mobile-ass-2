package com.example.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class ItemActivity extends AppCompatActivity {

    private HtmlTextView mHtmlView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        new UrlHtmlReader().execute(this, getIntent().getStringExtra("url"));
    }

    public void displayContent(String string) {
        mHtmlView = findViewById(R.id.html_view);
        mHtmlView.setHtml(string, new HtmlHttpImageGetter(mHtmlView));
    }
}
