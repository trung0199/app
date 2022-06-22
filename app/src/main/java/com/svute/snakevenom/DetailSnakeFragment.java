package com.svute.snakevenom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailSnakeFragment extends Fragment {
    public static final String TAG = DetailSnakeFragment.class.getName();
    ImageButton imageButton;
    ImageView imageView;
    TextView txtName, txtVenom, txtFeature, txtLive;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_snake, container, false);
        addControls(view);
        getDataFragment();
        addEvents();
        return view;
    }

    private void addControls(View view) {
        imageButton = view.findViewById(R.id.imageView);
        imageView = view.findViewById(R.id.imageViewSnakeDetail);
        txtName = view.findViewById(R.id.textViewNameDetail);
        txtVenom = view.findViewById(R.id.textViewVeNomDetail);
    }

    private void getDataFragment() {
        Bundle bundle = getArguments();
        if(bundle != null){
            SnakeModel snakeModel = (SnakeModel) bundle.get("objectSnake");
            if(snakeModel != null){
                Log.d("TAG", snakeModel.getGarbageName());
                imageView.setImageResource(snakeModel.getGarbageImg());
                txtName.setText(snakeModel.getGarbageName());
                txtVenom.setText(snakeModel.getGarbageInf());

            }
        }
    }
    private void addEvents() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }

            }
        });
    }
}