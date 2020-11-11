package com.android.youtube.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.youtube.R;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;


public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        channelImageUrl = getIntent().getStringExtra("channelImage");
        channelName = getIntent().getStringExtra("channelName");
        videos = getIntent().getStringExtra("channelVideos");
        views = getIntent().getStringExtra("channelViews");
        subscribers = getIntent().getStringExtra("channelSubs");


        tvSubscribers = findViewById(R.id.tvSubscribers);
        tvVideos = findViewById(R.id.tvVideos);
        tvViews = findViewById(R.id.tvViews);
        ivChannelImage = findViewById(R.id.ivChannelImage);

        String compressSubscriber = "";
        if(subscribers.length()>6)
            compressSubscriber = subscribers.substring(0, subscribers.length()-6) + "M";
        else
            compressSubscriber = subscribers;


        String compressVideos = "";
        if(videos.length()>3)
            compressVideos = videos.substring(0,videos.length()-3) + "K";
        else
            compressVideos = videos;


        String compressViews = "";
        if(views.length()>9)
            compressViews = views.substring(0, views.length()-9) + "B";
        else if(views.length()>6)
            compressViews = views.substring(0, views.length()-6) + "M";
        else
            compressVideos = videos;

        tvSubscribers.setText(compressSubscriber);
        tvVideos.setText(compressVideos);
        tvViews.setText(compressViews);

        new getChannelStatsData((ImageView) findViewById(R.id.ivChannelImage))
                .execute(channelImageUrl);

    }


    public class getChannelStatsData extends AsyncTask<String, Void, Bitmap> {

        ImageView channelImage;

        public getChannelStatsData(ImageView bmImage) {
            this.channelImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

            String url = urls[0];
            Bitmap bitmap = null;

            try {

                InputStream in = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {

                Log.e("Exception : ", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();

            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap result) {
            channelImage.setImageBitmap(result);
        }
    }

    TextView tvSubscribers, tvVideos, tvViews;
    String channelName, channelImageUrl, subscribers, videos, views;
    com.mikhaellopez.circularimageview.CircularImageView ivChannelImage;

}



