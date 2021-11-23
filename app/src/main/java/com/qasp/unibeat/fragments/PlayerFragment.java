package com.qasp.unibeat.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qasp.unibeat.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerFragment extends Fragment {

    ImageButton btnStop;
    ImageView ivPausePlay;
    boolean isPlaying = true;

    private static final String CLIENT_ID = "eadf0460915145b2b48616ffdd35476a";
    private static final String REDIRECT_URI = "com.qasp.unibeat://callback";
    public static final String TAG = "MainActivity";
    private static SpotifyAppRemote mSpotifyAppRemote;

    ImageView ivSongImage;
    TextView tvSongName;
    TextView tvArtistName;

    ImageButton btnLike;
    ImageButton btnDislike;
    ImageView ivBackground;

    Button btnDone;


    ArrayList<String> songs = new ArrayList<>();
    AppCompatSeekBar mSeekBar;
    TrackProgressBar mTrackProgressBar;

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

        mSeekBar = view.findViewById(R.id.seekBar);
        mSeekBar.setEnabled(false);
        //mSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.unibeatred), PorterDuff.Mode.SRC_ATOP);
        //mSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.unibeatred), PorterDuff.Mode.SRC_ATOP);
        mTrackProgressBar = new TrackProgressBar(mSeekBar);
        ivBackground = view.findViewById(R.id.ivBackground);


        btnStop = view.findViewById(R.id.btnStop);
        ivPausePlay = view.findViewById(R.id.ivPausePlay);

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
                    SpotifyAppRemote.disconnect(mSpotifyAppRemote);
                    getFragmentManager().beginTransaction().replace(R.id.flContainer, new HomeFragment()).commit();
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
                        // Now you can start interacting with App Remote
                        pickGenreAndThemes();
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

    private final Subscription.EventCallback<PlayerState> mPlayerStateEventCallback =
            new Subscription.EventCallback<PlayerState>() {
                @Override
                public void onEvent(PlayerState playerState) {
                    final Track track = playerState.track;
                    if (track != null) {
                        mSeekBar.setMax((int) playerState.track.duration);
                        Log.i(TAG, String.valueOf((int) playerState.track.duration));
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

                    // Update progressbar
                    if (playerState.playbackSpeed > 0) {
                        mTrackProgressBar.unpause();
                    } else {
                        mTrackProgressBar.pause();
                    }

                    // Invalidate play / pause
                    if (playerState.isPaused) {
                        ivPausePlay.setImageResource(R.drawable.playimage);
                    } else {
                        ivPausePlay.setImageResource(R.drawable.pauseimage);
                    }
                    if (playerState.track != null) {
                        // Invalidate seekbar length and position
                        mSeekBar.setMax((int) playerState.track.duration);
                        mTrackProgressBar.setDuration(playerState.track.duration);
                        mTrackProgressBar.update(playerState.playbackPosition);
                    }

                    mSeekBar.setEnabled(true);
                    mSeekBar.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });

                    btnLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.buttonanimation);
                            btnLike.startAnimation(shake);
                            songs.add(track.uri);
                            mSpotifyAppRemote.getPlayerApi().skipNext();
                        }
                    });
                    btnDislike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSpotifyAppRemote.getPlayerApi().skipNext();
                        }
                    });
                }
            };

    private void connected(String fileName) {

        InputStream inputStream = null;
        try {
            inputStream = getContext().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner s = new Scanner(inputStream).useDelimiter("\\n");
        String result = s.next();
        mSpotifyAppRemote.getPlayerApi().play(result);
        mSpotifyAppRemote.getPlayerApi().skipNext();
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(mPlayerStateEventCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
    public void pickGenreAndThemes(){
        String genre = getArguments().getString("Genre");
        switch (genre) {
            case "Country":
                changeColorsAndConnect(R.color.country, "country.txt");
                break;
            case "Rock":
                changeColorsAndConnect(R.color.rock, "rock.txt");
                break;
            case "Rnb":
                changeColorsAndConnect(R.color.rnb, "rnb.txt");
                break;
            case "Blues":
                changeColorsAndConnect(R.color.blues, "blues.txt");
                break;
            case "Decades":
                changeColorsAndConnect(R.color.pop, "decades.txt");
                break;
            case "HipHop":
                changeColorsAndConnect(R.color.hiphop, "hiphop.txt");
                break;
            case "Jazz":
                changeColorsAndConnect(R.color.jazz, "jazz.txt");
                break;
            case "Punk":
                changeColorsAndConnect(R.color.punk, "punk.txt");
                break;
            case "Mood":
                changeColorsAndConnect(R.color.mood, "mood.txt");
                break;
            case "Latino":
                changeColorsAndConnect(R.color.latino, "latino.txt");
                break;
        }
    }
    
    void changeColorsAndConnect(int colorVal, String textFile){
        int color = getResources().getColor(colorVal);
        ivBackground.setColorFilter(color);
        mSeekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mSeekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        btnDone.setTextColor(color);
        btnStop.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        connected(textFile.toString());
    }

    class TrackProgressBar {

        private static final int LOOP_DURATION = 500;
        private final SeekBar mSeekBar;
        private final Handler mHandler;

        private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener =
                new SeekBar.OnSeekBarChangeListener() {


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mSpotifyAppRemote
                                .getPlayerApi()
                                .seekTo(seekBar.getProgress());
                    }
                };

        protected final Runnable mSeekRunnable =
                new Runnable() {
                    @Override
                    public void run() {
                        int progress = mSeekBar.getProgress();
                        mSeekBar.setProgress(progress + LOOP_DURATION);
                        mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
                    }
                };

        protected TrackProgressBar(SeekBar seekBar) {
            mSeekBar = seekBar;
            mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
            mHandler = new Handler();
        }

        protected void setDuration(long duration) {
            mSeekBar.setMax((int) duration);
        }

        protected void update(long progress) {
            mSeekBar.setProgress((int) progress);
        }

        protected void pause() {
            mHandler.removeCallbacks(mSeekRunnable);
        }

        protected void unpause() {
            mHandler.removeCallbacks(mSeekRunnable);
            mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
        }
    }
}