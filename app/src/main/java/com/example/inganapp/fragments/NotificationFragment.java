package com.example.inganapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.inganapp.R;


public class NotificationFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        TextView tes=(TextView) view.findViewById(R.id.result1);
        String strtext = getArguments().getString("result");

        tes.setText(strtext);
        return view;
    }
}