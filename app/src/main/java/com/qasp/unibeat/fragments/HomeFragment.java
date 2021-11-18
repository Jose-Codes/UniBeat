package com.qasp.unibeat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.qasp.unibeat.R;

public class HomeFragment extends Fragment {

    ImageButton btnGenreRock;
    ImageButton btnGenreCountry;
    ImageButton btnGenreRnb;
    ImageButton btnGenreBlues;
    ImageButton btnGenreDecades;
    ImageButton btnGenreHipHop;
    ImageButton btnGenreJazz;
    ImageButton btnGenrePunk;
    ImageButton btnGenreMood;
    ImageButton btnGenreLatino;

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
        btnGenreCountry = view.findViewById(R.id.btnCountry);
        btnGenreRock = view.findViewById(R.id.btnRock);
        ImageButton btnGenreRnb = view.findViewById(R.id.btnRnb);
        ImageButton btnGenreBlues = view.findViewById(R.id.btnBlues);
        ImageButton btnGenreDecades = view.findViewById(R.id.btnDecades);
        ImageButton btnGenreHipHop = view.findViewById(R.id.btnHipHop);
        ImageButton btnGenreJazz = view.findViewById(R.id.btnJazz);
        ImageButton btnGenrePunk = view.findViewById(R.id.btnPunk);
        ImageButton btnGenreMood = view.findViewById(R.id.btnMood);
        ImageButton btnGenreLatino = view.findViewById(R.id.btnLatino);


        btnGenreCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               selectGenre("Country");
            }
        });
        btnGenreRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Rock");
            }
        });
        btnGenreRnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Rnb");
            }
        });
        btnGenreBlues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Blues");
            }
        });
        btnGenreDecades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Decades");
            }
        });
        btnGenreHipHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("HipHop");
            }
        });
        btnGenreJazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Jazz");
            }
        });
        btnGenrePunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Punk");
            }
        });
        btnGenreMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Mood");
            }
        });
        btnGenreLatino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGenre("Latino");
            }
        });

    }

    private void selectGenre(String genre){
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString("Genre", genre);
        playerFragment.setArguments(args);
        //Inflate the fragment
        getFragmentManager().beginTransaction().replace(R.id.flContainer, playerFragment).commit();
    }
}