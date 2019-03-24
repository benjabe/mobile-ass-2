package com.example.lab2;

public class RSSItem {
    private String mTitle;
    private String mDescription;
    private String mLink;
    private String mPubDate;

    RSSItem(String title, String description, String link, String pubDate)
    {
        mTitle = title;
        mDescription = description;
        mLink = link;;
        mPubDate = pubDate;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLink() {
        return mLink;
    }

    public String getPubDate() {
        return mPubDate;
    }
}
