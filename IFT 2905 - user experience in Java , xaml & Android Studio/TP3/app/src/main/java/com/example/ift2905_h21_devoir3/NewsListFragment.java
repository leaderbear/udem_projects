package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.android.material.tabs.TabLayout;

public class NewsListFragment extends Fragment {

    CategoryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mocking
        adapter = new CategoryAdapter(getContext(), new CategoryModel[] {
                new CategoryModel(R.drawable.school, "Université", false),
                new CategoryModel(R.drawable.law, "Falculté Droit", false),
                new CategoryModel(R.drawable.football, "Club Football", false),
                new CategoryModel(R.drawable.food, "Cafétéria", true),
                new CategoryModel(R.drawable.debats, "Débats", false),
                new CategoryModel(R.drawable.music, "Musique", true),
                new CategoryModel(R.drawable.pokemon, "Pokémon", false)
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.showBack(false);
        activity.showBottomNav(true);
        activity.showToolbarMenu(true);
        activity.setBottomNavChecked(1);
        
        TabLayout tablayout = getView().findViewById(R.id.tab_layout);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //
            }
        });

        recyclerView = getView().findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }
}