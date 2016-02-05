package com.example.glondhe.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by glondhe on 2/3/16.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {


    Transformation transformation = new RoundedTransformationBuilder()
            .oval(true).cornerRadius(1)
            .borderColor(Color.WHITE)
            .borderWidth(3)
            .build();

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
        ImageView prfImg = (ImageView) convertView.findViewById(R.id.prfPhoto);
        TextView cTime = (TextView) convertView.findViewById(R.id.ctime);
        TextView usrName = (TextView) convertView.findViewById(R.id.usrName);

        tvCaption.setText(photo.caption);
        String cnt = String.valueOf(photo.likecount);
        cnNum.setText(cnt + " likes");
        String ct = String.valueOf(photo.createdTime);
        cTime.setText(ct);
        usrName.setText(photo.username);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imgUrl).into(ivPhoto);
        Picasso.with(getContext())
                .load(photo.profileUrl)
                .transform(transformation)
                .into(prfImg);
        return convertView;
    }
}
