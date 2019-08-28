package org.andika.CinemaMovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.loadbar)ProgressBar loadbar;

    @BindView(R.id.title_heading)TextView title_heading;
    @BindView(R.id.title_movie)TextView title_movie;

    @BindView(R.id.overview_heading)TextView overview_heading;
    @BindView(R.id.overview_movie)TextView overview_movie;

    @BindView(R.id.runtime_heading)TextView runtime_heading;
    @BindView(R.id.runtime_movie)TextView runtime_movie;

    @BindView(R.id.vote_heading)TextView vote_heading;
    @BindView(R.id.vote_movie)TextView vote_movie;

    @BindView(R.id.status_heading)TextView status_heading;
    @BindView(R.id.status_movie)TextView status_movie;

    @BindView(R.id.release_heading)TextView release_heading;
    @BindView(R.id.release_movie)TextView release_movie;

    @BindView(R.id.app_bar)AppBarLayout foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        String id_movie = i.getStringExtra("idmovie");

        final String url = "https://api.themoviedb.org/3/movie/"+id_movie+"?api_key=f4fed984ab994840249e05415d977df6&language=en-US";
        new GetDetailTask().execute(url);

        FloatingActionButton openInBrowser = (FloatingActionButton) findViewById(R.id.openInBrowser);
        openInBrowser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "THIS SHOULD OPEN TO BROWSER", Snackbar.LENGTH_LONG)
                        // .setAction("Action", null).show();

                try {
                    String TrailerUrl = "https://www.youtube.com/results?search_query="+title_movie.getText()+" trailer";
                    Uri myURI = Uri.parse(TrailerUrl);
                    Intent launchbrowser = new Intent(Intent.ACTION_VIEW, myURI);
                    startActivity(launchbrowser);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class GetDetailTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            title_heading.setVisibility(View.GONE);
            title_movie.setVisibility(View.GONE);

            overview_heading.setVisibility(View.GONE);
            overview_movie.setVisibility(View.GONE);

            runtime_heading.setVisibility(View.GONE);
            runtime_movie.setVisibility(View.GONE);

            vote_heading.setVisibility(View.GONE);
            vote_movie.setVisibility(View.GONE);

            status_heading.setVisibility(View.GONE);
            status_movie.setVisibility(View.GONE);

            release_heading.setVisibility(View.GONE);
            release_movie.setVisibility(View.GONE);

            loadbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> detailist = new ArrayList<>();
            String backdrop;
            String title;
            String overview;
            String runtime;
            String vote;
            String status;
            String release;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;

                while ((inputString = bufferedReader.readLine())!= null) {
                    builder.append(inputString);
                }

                JSONObject object = new JSONObject(builder.toString());
                backdrop = String.valueOf(object.getString("backdrop_path"));
                title = String.valueOf(object.getString("title"));
                overview = String.valueOf(object.getString("overview"));
                runtime = String.valueOf(object.getString("runtime"));
                vote = String.valueOf(object.getString("vote_average"));
                status = String.valueOf(object.getString("status"));
                release = String.valueOf(object.getString("release_date"));

                detailist.add(backdrop);
                detailist.add(title);
                detailist.add(overview);
                detailist.add(runtime);
                detailist.add(vote);
                detailist.add(status);
                detailist.add(release);

            }catch (Exception e) {
                e.printStackTrace();
            }
            return detailist;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            String backdrop = result.get(0);
            String title = result.get(1);
            String overview = result.get(2);
            String runtime = result.get(3);
            String vote = result.get(4);
            String status = result.get(5);
            String release = result.get(6);

            Glide.with(DetailActivity.this)
                    .load("http://image.tmdb.org/t/p/original"+backdrop)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                foto.setBackground(drawable);
                            }
                        }
                    });

            title_movie.setText(title);
            overview_movie.setText(overview);
            if (runtime == String.valueOf(0)) {
                runtime_movie.setText("Unknown");
            } else {
                runtime_movie.setText(runtime+" Minutes");
            } if (vote == String.valueOf(0.0)) {
                vote_movie.setText("Unknown");
            } else {
                vote_movie.setText(vote);
            } if (release == String.valueOf("")) {
                release_movie.setText("Unknown");
            } else {
                release_movie.setText(release);
            }
            status_movie.setText(status);

            loadbar.setVisibility(View.GONE);

            title_heading.setVisibility(View.VISIBLE);
            title_movie.setVisibility(View.VISIBLE);

            overview_heading.setVisibility(View.VISIBLE);
            overview_movie.setVisibility(View.VISIBLE);

            runtime_heading.setVisibility(View.VISIBLE);
            runtime_movie.setVisibility(View.VISIBLE);

            vote_heading.setVisibility(View.VISIBLE);
            vote_movie.setVisibility(View.VISIBLE);

            status_heading.setVisibility(View.VISIBLE);
            status_movie.setVisibility(View.VISIBLE);

            release_heading.setVisibility(View.VISIBLE);
            release_movie.setVisibility(View.VISIBLE);
        }
    }
}
