package com.example.inganapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.inganapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class TextResultFragment extends Fragment {

    SendMessage SM;
    ConstraintLayout layout;
    public TextResultFragment() {
        // Required empty public constructor
    }
    /**
     * Create a new instance of this fragment
     *
     * @return A new instance of fragment FirstFragment.
     */
    public static TextResultFragment newInstance() {
        return new TextResultFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_text_result, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText nameEditText = view.findViewById(R.id.textInputTextName);
        TextView result = view.findViewById(R.id.textViewResult);
        Button passdata = view.findViewById(R.id.passdata);
        layout = view.findViewById(R.id.layoutFR);
        String strtext = getArguments().getString("result");
        result.setText(strtext);
        nameEditText.setText(strtext);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setVisibility(View.INVISIBLE);
                nameEditText.setVisibility(View.VISIBLE);
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passdata.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                result.setText(nameEditText.getText());
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passdata.setVisibility(View.INVISIBLE);
                nameEditText.setVisibility(View.INVISIBLE);
                result.setVisibility(View.VISIBLE);
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });
        passdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.sendData(Objects.requireNonNull(nameEditText.getText()).toString().trim());
                passdata.setVisibility(View.INVISIBLE);
                nameEditText.setVisibility(View.INVISIBLE);
                result.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }


    public interface SendMessage
    {
       void sendData(String message);
    }
}
