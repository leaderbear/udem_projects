package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPasswordFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(R.string.password_reset_title);
        activity.findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        activity.showBack(true);

        // Button logic
        Button pwdResetButton = getActivity().findViewById(R.id.pwdResetButton);
        pwdResetButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // On retourne au login
                Toast.makeText(getActivity().getApplicationContext(), R.string.password_reset_email_sent_toast, Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).getNavController().navigate(R.id.loginFragment);
            }
        });
    }
}
