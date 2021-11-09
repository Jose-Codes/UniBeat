package com.qasp.unibeat.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qasp.unibeat.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.HelloDetails;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerFragment extends Fragment {

    ImageButton btnStop;
    boolean isPlaying = true;

    private static final String CLIENT_ID = "eadf0460915145b2b48616ffdd35476a";
    private static final String REDIRECT_URI = "com.qasp.unibeat://callback";
    public static final String TAG = "MainActivity";
    private SpotifyAppRemote mSpotifyAppRemote;

    ImageView ivSongImage;
    TextView tvSongName;
    TextView tvArtistName;

    ImageButton btnLike;
    ImageButton btnDislike;

    Button btnDone;


    ArrayList<String> songs = new ArrayList<>();


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStop = view.findViewById(R.id.btnStop);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    mSpotifyAppRemote.getPlayerApi().pause();
                    isPlaying = false;
                } else if (!(isPlaying)) {
                    mSpotifyAppRemote.getPlayerApi().resume();
                    isPlaying = true;
                }

            }
        });
        ivSongImage = view.findViewById(R.id.ivSongImage);
        tvSongName = view.findViewById(R.id.tvSongName);
        tvArtistName = view.findViewById(R.id.tvArtistName);


        btnLike = view.findViewById(R.id.btnLike);
        btnDislike = view.findViewById(R.id.btnDislike);

        btnDone = view.findViewById(R.id.btnDone);
        

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < songs.size(); i++) {
                    mSpotifyAppRemote.getUserApi().addToLibrary(songs.get(i));
                    Toast.makeText(getContext(), "Songs Added to Spotify!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        // Now you can start interacting with App Remote
                        String genre = getArguments().getString("Genre");
                        switch(genre) {
                            case "Country":
                                connected("country.txt");
                                break;
                            case "Rock":
                                connected("rock.txt");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", "" + throwable.getMessage());

                        // Something went wrong when attempting to connect! Handle errors here
                        final String appPackageName = "com.spotify.music";
                        final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";

                        try {
                            Uri uri = Uri.parse("market://details")
                                    .buildUpon()
                                    .appendQueryParameter("id", appPackageName)
                                    .appendQueryParameter("referrer", referrer)
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        } catch (android.content.ActivityNotFoundException ignored) {
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                                    .buildUpon()
                                    .appendQueryParameter("id", appPackageName)
                                    .appendQueryParameter("referrer", referrer)
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }
                    }
                });
    }

    private void connected(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = getContext().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner s = new Scanner(inputStream).useDelimiter("\\n");
        String result = s.next();
        Log.i(TAG, result);

        mSpotifyAppRemote.getPlayerApi().play(result);

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name + " Song Image URI: " + track.imageUri);
                        mSpotifyAppRemote
                                .getImagesApi()
                                .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
                                .setResultCallback(
                                        bitmap -> {
                                            ivSongImage.setImageBitmap(bitmap);

                                        });
                    }
                    tvSongName.setText(track.name);
                    tvArtistName.setText("by: " + track.artist.name);

                    btnLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.buttonanimation);
                            btnLike.startAnimation(shake);

                            songs.add(track.uri);
                            String nextSong;
                            if((nextSong = s.next()) != null){
                                Log.i("MainActivity", nextSong);
                                mSpotifyAppRemote.getPlayerApi().play(nextSong);
                            }

                        }
                    });
                    btnDislike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nextSong;
                            if((nextSong = s.next()) != null){
                                Log.i("MainActivity", nextSong);
                                mSpotifyAppRemote.getPlayerApi().play(nextSong);
                            }
                        }
                    });
                });



    }

    @Override
    public void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}