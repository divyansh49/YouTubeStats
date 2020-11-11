package com.android.youtube.ChannelStats;

import android.util.Log;

import com.android.youtube.Network.NetworkConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;


public class StatsHelper {

    public static Stats getChannelStats(String channelUrl)
    {
        URL url = NetworkConnectionHelper.getChannelUrl(channelUrl);
        String data = null;

        try {

            data = NetworkConnectionHelper.networkRequest(url);

        } catch (IOException e) {

            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();

        }

        return parseJsonToStats(data);
    }
    public static Stats parseJsonToStats(String data)
    {

        Stats channelStats = new Stats(0,0,0);

        try {

            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("items");
            JSONObject object = jsonArray.getJSONObject(0);

            JSONObject channelInfo = object.getJSONObject("statistics");
            String videos = channelInfo.getString("videoCount");
            String views = channelInfo.getString("viewCount");
            String subscribers = channelInfo.getString("subscriberCount");

            channelStats = new Stats(
                    Long.parseLong(videos),
                    Long.parseLong(views),
                    Long.parseLong(subscribers)
            );

        }catch (JSONException e){
            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));
        }

        return channelStats;
    }



}