package com.example.pazzly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.example.pazzly.R;

public class FragmentGameInfo extends Fragment {

    public static FragmentGameInfo newInstance() {
        Bundle args = new Bundle();
        args.putString("SOME_PARAM_KEY", "Petar Petrovic");

        FragmentGameInfo fragment = new FragmentGameInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_game_info, container, false);
    }
}
