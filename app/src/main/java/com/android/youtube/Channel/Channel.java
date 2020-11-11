package com.android.youtube.Channel;

public class Channel {

    private String id;
    private String name;
    private String url;
    private long videos;
    private long views;
    private long subscribers;


    public Channel(String name, String url, String id, long sub, long videos, long views) {

        this.id = id;
        this.name = name;
        this.url = url;
        this.videos = videos;
        this.views = views;
        this.subscribers = sub;

    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public long getVideos() {
        return videos;
    }
    public long getViews() {
        return views;
    }
    public long getSubscribers() {
        return subscribers;
    }

}