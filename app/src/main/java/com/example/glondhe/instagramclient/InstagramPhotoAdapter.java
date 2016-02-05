package com.example.glondhe.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by glondhe on 2/3/16.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotoAdapter(Context context, ArrayList<InstagramPhoto> objects) {
        super(context,android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView cnNum = (TextView) convertView.findViewById(R.id.cnNum);
        tvCaption.setText(photo.caption);
        String cnt = String.valueOf(photo.likecount);
        cnNum.setText(cnt + " Likes");
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imgUrl).into(ivPhoto);
        return convertView;
    }
}
