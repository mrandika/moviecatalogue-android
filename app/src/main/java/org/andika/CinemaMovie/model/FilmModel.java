package org.andika.CinemaMovie.model;

import org.json.JSONObject;

/**
 * Created by andika on 03/02/18.
 */

public class FilmModel {

    private String title, overview, cover, release;
    private String id;

    public FilmModel(JSONObject object) {
        try {

            // Mulai pemanggilan JSON Object dari API
            String id = String.valueOf(object.getInt("id"));
            String title = object.getString("title");
            String overview = object.getString("overview");
            String cover = object.getString("poster_path");
            String release = object.getString("release_date");

            // Mulai mengirim data variabel local ke global
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.cover = cover;
            this.release = release;

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
