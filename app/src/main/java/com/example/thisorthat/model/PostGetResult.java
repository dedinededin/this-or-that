package com.example.thisorthat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostGetResult {

    @SerializedName("results")
    @Expose
    private ArrayList<Post> results;

    public ArrayList<Post> getResults() {
        return results;
    }

    public void setResults(ArrayList<Post> results) {
        this.results = results;
    }
}
