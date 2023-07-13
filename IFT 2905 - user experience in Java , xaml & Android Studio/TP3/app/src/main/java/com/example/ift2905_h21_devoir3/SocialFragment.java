package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SocialFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(R.string.message_list_title);
        activity.showBack(false);
        activity.showBottomNav(true);
        activity.showToolbarMenu(true);
        activity.setBottomNavChecked(2);
        getView().findViewById(R.id.card_person_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getNavController().navigate(R.id.action_socialFragment_to_messageFragment);
            }
        });
        getView().findViewById(R.id.card_person_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getNavController().navigate(R.id.action_socialFragment_to_messageFragment);
            }
        });

    }
}