package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final private String TAG = "MainActivity";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ArrayList<RSSItem> items = new ArrayList<>();
    private String mUrl;

    private DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;

    private int mItemLimit;
    private int mRefreshTime;   // ms


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        final Handler refreshHandler = new Handler();

        Runnable refreshCode = new Runnable() {
            @Override
            public void run() {
                update();
                refreshHandler.postDelayed(this, mRefreshTime);
            }
        };
        refreshHandler.post(refreshCode);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean
                onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();

                    switch(menuItem.getItemId()) {
                        case R.id.nav_preferences:
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    PreferenceActivity.class
                            );
                            startActivityForResult(intent, 0);
                            break;
                        default:
                            break;
                    }

                    return true;
                }
            }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        checkSharedPreferences();
        mEditor.apply();

        new UrlXmlReader().execute(this, mUrl);
    }

    public void update() {
        Log.d(TAG, "onActivityReenter: hey");
        checkSharedPreferences();
        mEditor.apply();

        items.clear();
        new UrlXmlReader().execute(this, mUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return MainActivity.super.onOptionsItemSelected(item);
    }

    /**
     * Parses RSS and displays the content.
     * @param content The content to parse.
     */
    public void displayContent(String content) {
        Log.d(TAG, "displayContent: " + content);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            // go through feed
            // for each <item>, add its title, description, link, pubData
            int eventType = parser.getEventType();
            String title = "";
            String description = "";
            String link = "";
            String pubDate = "";
            String currentTag = "";

            int count = 0;

            while (eventType != XmlPullParser.END_DOCUMENT
                    && count < mItemLimit) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        switch (parser.getName()) {
                            case "item":
                                Log.d(TAG, "displayContent: clear item values");
                                title = "";
                                description = "";
                                link = "";
                                pubDate = "";
                                break;
                            default:
                                currentTag = parser.getName();
                                //Log.d(TAG, "onCreate: " + currentTag);
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        switch (parser.getName()) {
                            case "item":
                                count++;
                                Log.d(TAG, "displayContent: adding RSSItem");
                                items.add(new RSSItem(
                                        title,
                                        description,
                                        link,
                                        pubDate
                                ));
                                break;
                            default:
                                break;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        Log.d(TAG, "displayContent: " +
                                currentTag + ":" + parser.getText());

                        switch (currentTag) {
                            case "title":
                                if (title.isEmpty()) {
                                    title = parser.getText();
                                }
                                break;
                            case "description":
                                if (description.isEmpty()) {
                                    description = parser.getText();
                                }
                                break;
                            case "link":
                                if (link.isEmpty()) {
                                    link = parser.getText();
                                }
                                break;
                            case "pubDate":
                                if (pubDate.isEmpty()) {
                                    pubDate = parser.getText();
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "displayContent: displaying stuff");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RSSItemAdapter(this, items));
    }

    @Override
    protected void
    onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 0) {
            return;
        }
        //mUrl = data.getStringExtra("url");
        update();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Checks if preferences are set and sets up the app accordingly.
     */
    private void checkSharedPreferences() {
        String url = mPreferences.getString(
                "url",
                "http://www.engadget.com/rss.xml"
        );
        int itemLimit = mPreferences.getInt("itemLimit", 100);
        mUrl = url;
        mItemLimit = itemLimit;
        mRefreshTime = mPreferences.getInt("refreshTime", 600000);
        Log.d(TAG, "refreshOption: " + mRefreshTime);
        //"http://www.engadget.com/rss.xml";
    }
}
