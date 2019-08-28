package org.andika.CinemaMovie;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.andika.CinemaMovie.adapter.FilmAdapter;
import org.andika.CinemaMovie.loader.FilmLoader;
import org.andika.CinemaMovie.model.FilmModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<FilmModel>> {

    @BindView(R.id.rv_film)RecyclerView rv_film;

    @BindView(R.id.edit_cari)EditText edit_cari;
    @BindView(R.id.progressbar)ProgressBar progressBar;

    @BindView(R.id.nowplaying_card)CardView nowplaying_card;
    @BindView(R.id.mostpopular_card)CardView mostpopular_card;
    @BindView(R.id.comingsoon_card)CardView comingsoon_card;

    @BindView(R.id.sv_movie_home)ScrollView sv_movie_home;

    FilmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        nowplaying_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String NowPlayingUrl = "https://www.themoviedb.org/movie/now-playing";
                    Uri myURI = Uri.parse(NowPlayingUrl);
                    Intent launchbrowser = new Intent(Intent.ACTION_VIEW, myURI);
                    startActivity(launchbrowser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mostpopular_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String MostPopularUrl = "https://www.themoviedb.org/movie";
                    Uri myURI = Uri.parse(MostPopularUrl);
                    Intent launchbrowser = new Intent(Intent.ACTION_VIEW, myURI);
                    startActivity(launchbrowser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        comingsoon_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String ComingSoonUrl = "https://www.themoviedb.org/movie/upcoming";
                    Uri myURI = Uri.parse(ComingSoonUrl);
                    Intent launchbrowser = new Intent(Intent.ACTION_VIEW, myURI);
                    startActivity(launchbrowser);
                } catch (Exception e) {

                }
            }
        });

        // Inisialisasi Adapter
        adapter = new FilmAdapter(this);

        // Inisialisasi Recycler View
        rv_film.setLayoutManager(new LinearLayoutManager(this));
        rv_film.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        edit_cari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String keyword = edit_cari.getText().toString();
                    FilmLoader.cari = keyword.toString();
                    progressBar.setVisibility(View.VISIBLE);
                    rv_film.setVisibility(View.GONE);
                    sv_movie_home.setVisibility(View.GONE);
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public Loader<ArrayList<FilmModel>> onCreateLoader(int i, Bundle bundle) {
        return new FilmLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FilmModel>> loader, ArrayList<FilmModel> data) {
        adapter.setListfilm(data);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        rv_film.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FilmModel>> loader) {

    }
}
