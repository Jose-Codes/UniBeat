package com.qasp.unibeat.firebase;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String AboutMe = "";
    private List<String> LikedSongs = new ArrayList<>();
    private String Location = "";
    private List <String> Matches = new ArrayList<>();
    private String Name = "";
    private List <String> ViewedSongs = new ArrayList<>();
    private String ImageUri = "";

    public User(){

    }
    public User(String aboutMe, List<String> likedSongs, String location,
                List <String> matches, String name, List <String> viewedSongs,
                String imageUri){

        this.AboutMe = aboutMe;
        this.LikedSongs = likedSongs;
        this.Location = location;
        this.Matches = matches;
        this.Name = name;
        this.ViewedSongs = viewedSongs;
        this.ImageUri = imageUri;

    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.AboutMe = aboutMe;
    }

    public List<String> getLikedSongs() {
        return LikedSongs;
    }

    public void setLikedSongs(List<String> likedSongs) {
        this.LikedSongs = likedSongs;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public List<String> getMatches() {
        return Matches;
    }

    public void setMatches(List<String> matches) {
        this.Matches = matches;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public List<String> getViewedSongs() {
        return ViewedSongs;
    }

    public void setViewedSongs(List<String> viewedSongs) {
        this.ViewedSongs = viewedSongs;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        this.ImageUri = imageUri;
    }
}
