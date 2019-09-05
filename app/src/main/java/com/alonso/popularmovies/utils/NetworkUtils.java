package com.alonso.popularmovies.utils;

import android.util.Log;

import  com.alonso.popularmovies.utils.Constants;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aaton on 3/13/2018.
 */

public class NetworkUtils {

    private OkHttpClient mOkHttpClient;

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_URL = "https://api.themoviedb.org/3/movie";
    final static String  API_KEY = "api_key";
    final static String API_VALUE= Constants.API_KEY;

    public NetworkUtils(){
        mOkHttpClient = new OkHttpClient();
    }

    public static String buildUrl(String sortParameter){

        HttpUrl.Builder builder = HttpUrl.parse(MOVIES_URL).newBuilder();
        builder.addPathSegment(sortParameter);
        builder.addQueryParameter(API_KEY, API_VALUE);

        String url = builder.build().toString();

        return url;
    }

    public Response getResponseFromUrl(String url){

        Request request = new Request.Builder()
                            .url(url)
                            .build();

        Response response = null;

        try{
            response = mOkHttpClient.newCall(request).execute();
        }catch (IOException ex){
            Log.d(TAG, ex.getMessage());
        }

        return response;
    }

}
