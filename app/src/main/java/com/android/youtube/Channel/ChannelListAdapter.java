package com.android.youtube.Channel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.youtube.R;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;


public class ChannelListAdapter extends ArrayAdapter<Channel> {

    public ChannelListAdapter(Context context, List<Channel> channels) {
        super(context,0, channels);
    }

    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.channel, parent, false);
        }

        Channel channel = getItem(position);
        TextView channelName = itemView.findViewById(R.id.tvChannel);

        assert channel != null;
        channelName.setText(channel.getName());

        new setUpChannelImage((ImageView) itemView.findViewById(R.id.ivChannelImage))
                .execute(channel.getUrl());

        return itemView;

    }
    public class setUpChannelImage extends AsyncTask<String, Void, Bitmap> {

        ImageView channelImage;

        public setUpChannelImage(ImageView channelImage) {
            this.channelImage = channelImage;
        }

        protected Bitmap doInBackground(String... urls) {

            String channelUrl = urls[0];
            Bitmap bitmap = null;

            try {

                InputStream in = new java.net.URL(channelUrl).openStream();
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
}