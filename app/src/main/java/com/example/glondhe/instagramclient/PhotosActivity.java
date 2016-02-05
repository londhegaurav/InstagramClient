package com.example.glondhe.instagramclient;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        fetchPopularPhoto();
        aPhotos = new InstagramPhotoAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
    }
    public void fetchPopularPhoto(){

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        Log.i("DEBUG", url.toString());
        //Create network client
        AsyncHttpClient client =  new AsyncHttpClient();
        client.get(url,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++){
                        JSONObject photoJSON  = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        Log.i("DEBUG", photo.username.toString());
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        if(photo.caption.equals(null)){
                            photo.caption = "Unknown";
                        }
                        Log.i("DEBUG", photo.caption.toString());
                        photo.imgUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        Log.i("DEBUG", photo.imgUrl.toString());
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        Log.i("DEBUG", String.valueOf(photo.imageHeight));
                        photo.likecount = photoJSON.getJSONObject("likes").getInt("count");
                        Log.i("DEBUG", String.valueOf(photo.likecount));
                        photos.add(photo);
                    }

                } catch (JSONException e){
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
