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
import android.widget.Button;

import com.qasp.unibeat.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class HomeFragment extends Fragment {

    Button btnStop;
    boolean isPlaying = true;

    private static final String CLIENT_ID = "eadf0460915145b2b48616ffdd35476a";
    private static final String REDIRECT_URI = "com.qasp.unibeat://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    public HomeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
                        connected();
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

    private void connected() {
        InputStream inputStream = null;
        try {
            inputStream = getContext().getAssets().open("songs.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";


        mSpotifyAppRemote.getPlayerApi().play(result);

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}