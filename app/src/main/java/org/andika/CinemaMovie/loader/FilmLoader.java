package org.andika.CinemaMovie.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.andika.CinemaMovie.model.FilmModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by andika on 03/02/18.
 */

public class FilmLoader extends AsyncTaskLoader<ArrayList<FilmModel>> {
    public static String cari = "";
    public ArrayList<FilmModel> mData;
    public boolean hasResult = false;

    public FilmLoader(Context context) {
        super(context);
        onContentChanged();
    }

    public ArrayList<FilmModel> getmData() {
        return mData;
    }

    public void setmData(ArrayList<FilmModel> mData) {
        this.mData = mData;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(ArrayList<FilmModel> data) {
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if((hasResult)) {
            onReleaseResource(mData);
            mData = null;
            hasResult = false;
        }
    }

    private void onReleaseResource(ArrayList<FilmModel> mData) {
    }

    @Override
    public ArrayList<FilmModel> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<FilmModel> filmItemses = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/movie?api_key=f4fed984ab994840249e05415d977df6&language=en-US&query="+cari;
        String nowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=f4fed984ab994840249e05415d977df6&language=en-US&page=1";
        String mostPopularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=f4fed984ab994840249e05415d977df6&language=en-US&page=1";
        String comingSoonUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=f4fed984ab994840249e05415d977df6&language=en-US&page=1";
        client.get(url,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               try {
                   String results = new String(responseBody);
                   JSONObject responseObject = new JSONObject(results);
                   JSONArray list = responseObject.getJSONArray("results");

                   for (int i = 0 ;i < list.length();i++) {
                       JSONObject film = list.getJSONObject(i);
                       FilmModel filmModel = new FilmModel(film);
                       filmItemses.add(filmModel);
                   }
               }catch (Exception e) {
                   e.printStackTrace();
               }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return filmItemses;
    }
}
