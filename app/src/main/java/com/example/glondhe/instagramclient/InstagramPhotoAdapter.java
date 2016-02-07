package com.example.glondhe.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
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
        TextView commentText = (TextView) convertView.findViewById(R.id.commentText);

        tvCaption.setText(photo.caption);
        String cnt = String.valueOf(photo.likecount);
        String cntLikes = "<font color='#3b5998'>" + cnt + " likes</font>";

        SpannableStringBuilder builder = new SpannableStringBuilder();
        cnNum.setText(Html.fromHtml(cntLikes));

        String ct = String.valueOf(DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000));

       // String ct = String.valueOf(photo.createdTime);
        String comment = "<font color='#FFFFFF'>COMMENT:</font>";

        cTime.setText(ct);
        usrName.setText(photo.username);
        commentText.setText(Html.fromHtml("<font color='#3b5998'>COMMENT:" +  "<br><br>" + photo.commentUsername[0] +":</font>"+ photo.comment[0]+"<br><br> <font color='#3b5998'>"+photo.commentUsername[1]+":</font>"+photo.comment[1]));
                //commentText.setText(Html.fromHtml("<font color='#000000'>COMMENT:</font>")+"\n\n"+photo.commentUsername[0]+": "+photo.comment[0]+"\n\n"+photo.commentUsername[1]+": "+photo.comment[1]);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imgUrl).placeholder(R.drawable.orange_load).into(ivPhoto);
        Picasso.with(getContext())
                .load(photo.profileUrl)
                .transform(transformation)
                .into(prfImg);
        return convertView;
    }
}
