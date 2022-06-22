package com.svute.snakevenom;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class HomeFragment extends Fragment {
    Button btnImage, btnVideo;
    CameraFragment cameraFragment = new CameraFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        addControls(view);
        addEvents();
        replaceFragment(cameraFragment);
        return view;
    }


    private void addEvents() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(cameraFragment);
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RealtimeFragment());
            }
        });
    }

    private void addControls(View view) {
        btnImage = view.findViewById(R.id.buttonOpenImageFragment);
        btnVideo = view.findViewById(R.id.buttonOpenVideoFragment);
    }
    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout2, fragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

}