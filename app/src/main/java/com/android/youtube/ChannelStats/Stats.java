package com.android.youtube.ChannelStats;

public class Stats {

    private long videos;
    private long views;
    private long subscribers;

    Stats(long videos, long views, long subscribers)
    {
        this.videos = videos;
        this.views = views;
        this.subscribers = subscribers;
    }

    public long getVideos()
    {
        return videos;
    }
    public long getViews()
    {
        return views;
    }
    public long getSubscribers()
    {
        return subscribers;
    }

}