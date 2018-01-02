package com.ac.example.dataAnalysis.model;

import java.util.Objects;

/**
 * @author Alex Carvalho
 */
public class Genre {

    private long id;
    private String name;

    public Genre() {
    }

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Genre{" + name + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Genre && Objects.equals(id, ((Genre) obj).id);
    }
}
