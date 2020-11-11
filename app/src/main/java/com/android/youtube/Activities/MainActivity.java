package com.android.youtube.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.youtube.Channel.Channel;
import com.android.youtube.Channel.ChannelListAdapter;
import com.android.youtube.ChannelStats.Stats;
import com.android.youtube.ChannelStats.StatsHelper;
import com.android.youtube.Network.NetworkConnectionHelper;
import com.android.youtube.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    TextView tvSearchResult;
    ListView lvChannels;

    ChannelListAdapter channelListAdapter;
    TextInputLayout etChannel;

    int channel = 0;
    Stats stats;

    List<Channel> channelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etChannel = findViewById(R.id.etChannel);
        btnSearch = findViewById(R.id.btnSearch);
        tvSearchResult = findViewById(R.id.tvSearchResults);
        tvSearchResult.setVisibility(View.GONE);
        lvChannels = findViewById(R.id.lvChannels);
        lvChannels.setVisibility(View.GONE);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = null;

                if (etChannel.getEditText().getText() != null)
                    data = etChannel.getEditText().getText().toString();

                BASE_URL = BASE + data + TYPE;
                final getChannelData asyncTask = new getChannelData();
                asyncTask.execute(BASE_URL);

                lvChannels.setVisibility(View.VISIBLE);
                tvSearchResult.setVisibility(View.VISIBLE);

                channelListAdapter = new ChannelListAdapter(MainActivity.this, new ArrayList<Channel>());
                lvChannels.setAdapter(channelListAdapter);

                lvChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                        Intent intent =new Intent(MainActivity.this, StatsActivity.class);

                        intent.putExtra("channelImage", channelList.get(pos).getUrl());
                        intent.putExtra("channelName", channelList.get(pos).getName());
                        intent.putExtra("channelVideos",""+ channelList.get(pos).getVideos());
                        intent.putExtra("channelViews",""+ channelList.get(pos).getViews());
                        intent.putExtra("channelSubs",""+ channelList.get(pos).getSubscribers());

                        startActivity(intent);

                    }
                });
            }
        });

    }

    public class getChannelData extends AsyncTask<String, Void, List<Channel>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected List<Channel> doInBackground(String... strings) {

            channelList = NetworkConnectionHelper.getChannelList(BASE_URL);
            return channelList;

        }

        @Override
        protected void onPostExecute(List<Channel> channels) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (channels != null) {
                channelListAdapter.addAll(channels);
            }

            getChannelStatsData data = new getChannelStatsData();
            STATS_URL = STATS_BASE + channelList.get(0).getId() + STATS_TYPE;
            data.execute(STATS_URL);

        }

    }

    public class getChannelStatsData extends AsyncTask<String, Void, Stats> {

        @Override
        protected Stats doInBackground(String... strings) {

            for(int i = 0; i < channelList.size(); i++)
            {
                channel = i;

                String channelId= channelList.get(i).getId();
                STATS_URL = STATS_BASE + channelId + STATS_TYPE;

                Stats channelStats = StatsHelper.getChannelStats(STATS_URL);

                Channel channelObj = new Channel(
                        channelList.get(channel).getName(),
                        channelList.get(channel).getUrl(),
                        channelList.get(channel).getId(),
                        channelStats.getSubscribers(),
                        channelStats.getVideos(),
                        channelStats.getViews()
                );

                channelList.set(channel,channelObj);

            }
            return stats;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Stats statistics) {
            super.onPostExecute(statistics);
        }

    }

    String KEY = "AIzaSyCWszj7sPAQtBb0I7WVMhH1UFMZt-xZC50";

    String BASE = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&q=";
    String TYPE = "&type=channel&key=" + KEY;
    String BASE_URL;

    String STATS_BASE = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails%2Cstatistics&id=";
    String STATS_TYPE = "&key=" + KEY;
    String STATS_URL;

}