package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ResearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_research, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        getActivity().setTitle(R.string.search_title);
        activity.showBack(false);
        activity.showBottomNav(true);
        activity.showToolbarMenu(true);
        activity.setBottomNavChecked(0);

        Button loginButton = getActivity().findViewById(R.id.searchButton);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).getNavController().navigate(R.id.action_researchFragment_to_searchResultFragment);
            }
        });
    }
}