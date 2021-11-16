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
        btnGenreCountry = view.findViewById(R.id.btnMood);
        btnGenreRock = view.findViewById(R.id.btnLatino);

        btnGenreCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment playerFragment = new PlayerFragment();
                Bundle args = new Bundle();
                args.putString("Genre", "Country");
                playerFragment.setArguments(args);
                //Inflate the fragment
                getFragmentManager().beginTransaction().replace(R.id.flContainer, playerFragment).commit();
            }
        });

        btnGenreRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment playerFragment = new PlayerFragment();
                Bundle args = new Bundle();
                args.putString("Genre", "Rock");
                playerFragment.setArguments(args);
                //Inflate the fragment
                getFragmentManager().beginTransaction().replace(R.id.flContainer, playerFragment).commit();
            }
        });
    }
}