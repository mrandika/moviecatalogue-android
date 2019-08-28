package org.andika.CinemaMovie.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.andika.CinemaMovie.DetailActivity;
import org.andika.CinemaMovie.R;
import org.andika.CinemaMovie.model.FilmModel;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by andika on 03/02/18.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private String cover;

    public FilmAdapter(JSONObject object) {

        try {
            String cover = String.valueOf(object.getString("poster_path"));

            this.cover = cover;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Deklarasi variabel
    private Context context;
    private List<FilmModel> listfilm = new ArrayList<>();

    public List<FilmModel> getListfilm() {
        return listfilm;
    }

    public FilmAdapter(Context context) {
        this.context = context;
    }

    public void setListfilm(List<FilmModel> listfilm) {
        this.listfilm = listfilm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilmModel m = getListfilm().get(position);
        holder.txt_judul.setText(m.getTitle());
        holder.txt_deskripsi.setText(m.getOverview());
        holder.txt_release.setText("Released on "+m.getRelease());

        final String id = m.getId();
        holder.mrootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(), DetailActivity.class);
                i.putExtra("idmovie",id);
                context.startActivity(i);
                // Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(context)
                .load("http://image.tmdb.org/t/p/original"+m.getCover())
                .into(holder.img_poster);
    }

    @Override
    public int getItemCount() {
        return getListfilm().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_judul)TextView txt_judul;
        @BindView(R.id.txt_deskripsi)TextView txt_deskripsi;
        @BindView(R.id.txt_release)TextView txt_release;
        @BindView(R.id.img_poster)ImageView img_poster;

        View mrootview;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mrootview = itemView;
        }
    }
}
