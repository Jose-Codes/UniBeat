# UnBeat by QASP Inc.

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Unibeat is an app that allows users to pick a music 
genre and explore new songs. They can swipe left or right based on whether or not they like a song. Their liked songs are added to a playlist which they can listen to on their Spotify. People with similar music taste can connect with one another.

### App Evaluation
- **Category:** Social Networking / Music
- **Mobile:** This app is particularly focused on being developed for Android mobile devices. However it can be expanded to other platforms with later iterations.
- **Story:** Allows users to explore different music genres which they can add to a playlist. Other people can view yoour liked songs on your profile.
- **Market:** This application is suitable for any individual who wants to explore new music genres and meet new people.
- **Habit:** Users can use the app as often as they want depending on how any genres they want to explore and how many people they wish to connect to.
- **Scope:** It would start as a simple application connected with Spotify, but it can later be extended to other applications like SoundCloud, as well as expand on users music choices and their ability to communicate with people.
## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can register for an account 
* Users can log in to their account
* User must pick genre before entering the songfinder
* Users will see the songfinder *Main page*
* User can replay a song
* User can favorite a song
* Users can see their profile with a playlist, favorites, and connections(stretch) section
* Users can see related artists and songs information when they swipe up
* 30 second clip of a song plays for users and they can either swipe right or left



**Optional Nice-to-have Stories**

* Users can have groupchats and private messages
* Can see each other in maps
* Search for other users
* User can have a favorite songs on profile
* Current genre that they are exploring
* Change background color based on Genre **
* Users can explore other user's playlists and connect with other users
* Click on the song and listen to it
* User can see upcoming concerts in their area related to their music taste

### 2. Screen Archetypes

* Login
   * User can login to app

* Register
    * User can sign up

* Stream
    * Users will see the songfinder *Main page*
    * 30 second clip of a song plays for users and they can either swipe           right or left
    * User must pick genre before entering the songfinder

* Profile
    * Users can see their profile with a playlist section

* Detail
    * Users can see related artists and songs information when they swipe up
    * Users can explore other user's playlists and connect with other users
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Profile
* Playlists
* Chat (Stretch Story)
* Explore (Stretch Story)

**Flow Navigation** (Screen to Screen)

* Login / Register
* Home screen and swipe up for details
* Chat list / private chat (Stretch Story)
* Playlists list / Playlist details 

## Wireframes
<img src="/digitalwireframes/low-fidelity-wireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups
<img src="/digitalwireframes/app_login.jpg" width=300> <img src="/digitalwireframes/app_signup.jpg" width=300> <img src="/digitalwireframes/app_main.jpg" width=300> <img src="/digitalwireframes/app_songfinder.jpg" width=300>  <img src="/digitalwireframes/app_songfinder2.jpg" width=300> <img src="/digitalwireframes/app_chat.jpg" width=300>
<img src="/digitalwireframes/app_profile.jpg" width=300> <img src="/digitalwireframes/app_settings.jpg" width=300> <img src="/digitalwireframes/app_done.jpg" width=300> <img src="/digitalwireframes/app_songinfo.jpg" width=300>

### Schema: 

## Models

# *Song*

| Property | Type | Description|
|---------|---------| ---------|
| songID | String | Unique ID for the song |
| Author | String | The artist of the Song |
| Genre | String | Genre of the song | 
| Related Songs| List <song> | A list of songs related to the song played |
| Song Image | String | The thumbnail of the song | 
| Lyrics | String | If available, the lyrics of the song that is currently playing | 


# *User*
  | Property | Type | Description|
|---------|---------| ---------| 
| Name | String | Name of the user |
| Profile Pic URI | String | Picture uploaded by the user | 
| About Me | String | A brief description about the user | 
| Liked Songs | List<song> | A list of the user’s favorite songs |
| Location | String | Where the user is located |
| User matches | List<Users> | A list of users that are matched to the current user |
| Seen Songs | List<song> | List of songs that the user has already seen so it is not shown again |


# *Networking*

Spotify API:
Song Finder
- (Read/GET) Query songs from a specific genre.
- (GET) New song (URI, title, time, etc…)
- (POST) Create a playlist in Spotify from firebase
- (POST) Add song to a Spotify playlist 

_song Finder (more information view)_
- (GET) Related songs
- Note for the two above, the get request for “New Song” might already get the data.

_Profile_
(Read/GET) Image URI and Track Name From Favorite Songs 


_Firebase API_
  
Home Screen:
- (GET) List of Genres

Profile:
- (GET) Username, description, profile pic, favorite songs

Settings:
- (GET) Full name, email, social handle, profile pic

Song Finder: 
- (POST) Add song to favorites
- (POST) Add song to liked
  
Messages:
- (GET) Chatrooms with messages (including read/unread)
- (POST) Send messages to others
- (GET) New matches that are not chatted with yet


Base URL: https://api.spotify.com/v1
Existing API endpoints:

| HTTP Verb | Endpoint | Description|
|---------|---------| ---------|
|Get |/tracks/id | Gets the track|
| Get| /tracks/id | Gets the track with a different uri as the last one, after seeing if the user has already listened to it.| 
| Post | users/user_id/playlist | Creates a new playlist in spotify |
| Post | playlists/playlist_id/track | Add a song to a Spotify playlist |
| Get | /tracks/id | Get Image URL from the track information |
| GET | /tracks/id | Get track name from the track information |


