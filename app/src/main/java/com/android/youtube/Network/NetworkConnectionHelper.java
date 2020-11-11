package com.android.youtube.Network;

import android.util.Log;

import com.android.youtube.Channel.Channel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NetworkConnectionHelper {

    public static String channelId;
    public static String channelUrl;
    public static String channelName;
    public static ArrayList<Channel> channelList;

    public static URL getChannelUrl(String data){

        URL url = null;
        try
        {
            url = new URL(data);

        }catch (MalformedURLException e){

            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));

        }
        return url;
    }

    public static List<Channel> parseJsonData(String data)
    {
        if (data.isEmpty()) {
            return null;
        }

        channelList = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object =jsonArray.getJSONObject(i);
                JSONObject snippet = object.getJSONObject("snippet");

                channelName = snippet.getString("channelTitle");
                channelId = snippet.getString("channelId");

                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                JSONObject defaults = thumbnails.getJSONObject("default");
                channelUrl = defaults.getString("url");

                Channel channel = new Channel(channelName, channelUrl, channelId,-1,-1,-1);
                channelList.add(channel);

            }

        }catch (JSONException e){
            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));
        }

        return channelList;
    }

    public static String networkRequest(URL url) throws IOException
    {
        String channelData = "";

        if(url==null) {
            return channelData;
        }

        HttpURLConnection connection = null;
        InputStream iStream = null;

        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {

                iStream = connection.getInputStream();
                channelData = getDataFromInputStream(iStream);

            }

        } catch (IOException e){

            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));

        } finally {

            if(connection != null){
                connection.disconnect();
            }

            if(iStream != null){
                iStream.close();
            }

        }

        return channelData;
    }

    private static String getDataFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }

        return output.toString();
    }

    public static List<Channel> getChannelList(String data)
    {
        URL url = getChannelUrl(data);
        String channelData = null;

        try {

            channelData = networkRequest(url);

        } catch (IOException e) {

            Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();

        }

        return parseJsonData(channelData);
    }

}