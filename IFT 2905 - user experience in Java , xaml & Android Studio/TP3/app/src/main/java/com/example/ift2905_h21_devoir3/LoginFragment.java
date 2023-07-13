package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(R.string.login_fragment_label);
        activity.showBottomNav(false);
        activity.showBack(false);
        activity.showToolbarMenu(false);

        // Login button logic
        Button loginButton = getActivity().findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Normalement on fait le login ici (authentification)
                ((MainActivity) getActivity()).getNavController().navigate(R.id.newsListFragment);
            }
        });


        // First connection button logic
        Button firstConnectionButton = (Button) getActivity().findViewById(R.id.loginFirstConnectionButton);
        firstConnectionButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Normalement on fait le login ici (authentification)
                ((MainActivity) getActivity()).getNavController().navigate(R.id.action_loginFragment_to_createProfileFragment);
            }
        });

        // Forgot password button logic
        Button forgotPasswordButton = (Button) getActivity().findViewById(R.id.loginForgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Normalement on fait le login ici (authentification)
                ((MainActivity) getActivity()).getNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });

    }
}
