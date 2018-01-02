package com.ac.example.dataAnalysis.model;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author José Paumard
 */
public class Movie {
    private String title;
    private LocalDate releaseDate;
    private int voteAverage;
    private int voteCount;
    private Set<Genre> genres;

    public Movie(String title, LocalDate releaseDate, int voteAverage, int voteCount, Set<Genre> genres) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", genres=" + genres +
                '}';
    }
}
