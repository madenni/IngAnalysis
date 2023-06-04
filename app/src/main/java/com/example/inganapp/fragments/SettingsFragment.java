package com.example.inganapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.inganapp.R;


public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ImageView pic=(ImageView) view.findViewById(R.id.image);
        Uri strtext = Uri.parse(getArguments().getString("picture"));
        pic.setImageURI(null);
        pic.setImageURI(strtext);
        return view;
    }
}