package com.example.glondhe.instagramclient;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    InstagramPhotoAdapter aPhotos;
    SwipeRefreshLayout swipeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        aPhotos.clear();
                        fetchPopularPhoto();
                    }
                }, 5000);
            }
        });

        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        fetchPopularPhoto();
        aPhotos = new InstagramPhotoAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
    }
    public void fetchPopularPhoto(){

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        Log.i("DEBUG", url.toString());
        //Create network client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        Log.i("DEBUG_username", photo.username.toString());

                        if (!photoJSON.isNull("caption"))
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        else
                            photo.caption = "";

                        Log.i("DEBUG_caption", photo.caption.toString());
                        photo.imgUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        Log.i("DEBUG_imgUrl", photo.imgUrl.toString());
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        Log.i("DEBUG_imageHeight", String.valueOf(photo.imageHeight));
                        photo.likecount = photoJSON.getJSONObject("likes").getInt("count");
                        Log.i("DEBUG_likecount", String.valueOf(photo.likecount));
                        photo.createdTime = photoJSON.getLong("created_time");
                        Log.i("DEBUG_createdTime", String.valueOf(photoJSON.getLong("created_time")));
                        photo.profileUrl = photoJSON.getJSONObject("user").optString("profile_picture");
                        Log.i("DEBUG_profileUrl", photo.profileUrl.toString());
                        JSONArray commentsJSONArray = null;
                        commentsJSONArray =  photoJSON.getJSONObject("comments").getJSONArray("data");
//                        Log.i("DEBUG_commentsJSONArray", String.valueOf(commentsJSONArray.length()));
                    //    Log.i("DEBUG_commentsJSONArray", String.valueOf(commentsJSONArray.get(2)));
                        int k=0;
                        for (int j = commentsJSONArray.length()-1; j > commentsJSONArray.length()-3; j--) {

                            photo.comment[k]="";
                            photo.commentUsername[k]="";
                            if (commentsJSONArray.getJSONObject(j) != null)
                            {
                                JSONObject commentsJSON = commentsJSONArray.getJSONObject(j);
                                Log.i("DEBUG_comment",  photo.comment[k]);
                                Log.i("DEBUG_commentUsername", photo.commentUsername[k]);
                                Log.i("DEBUG_comment_text",  commentsJSON.getString("text"));
                                if ( commentsJSON.getString("text") != null || !commentsJSON.getString("text").isEmpty() || !commentsJSON.getJSONObject("from").getString("username").isEmpty() || commentsJSON.getJSONObject("from").getString("username") != null) {
                                    photo.comment[k] = commentsJSON.getString("text");
                                    photo.commentUsername[k] = commentsJSON.getJSONObject("from").getString("username");
                                    Log.i("DEBUG_comment",  photo.comment[k]);
                                    Log.i("DEBUG_commentUsername", photo.commentUsername[k]);
                                }
                            }

                            k++;
                        }

                        photos.add(photo);
                        swipeLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
