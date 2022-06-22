package com.svute.snakevenom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private final int ID_HOME = 1;
    private final int ID_DATA = 2;
    private final int ID_ABOUT = 3;
    TextView txtLable;
    MeowBottomNavigation naviBottom;

    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        setUpNavi();
        events();
        checkPermissionCamera();
        checkData();
    }

    private void checkData() {
        Intent intent = getIntent();
        if(intent != null){
            try {
                Log.d("TAG", "checkData: " + Integer.parseInt(intent.getStringExtra("key")));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LibraryFragment libraryFragment = new LibraryFragment();
                naviBottom.show(ID_DATA,true);
                Bundle bundle = new Bundle();
                bundle.putInt("key", Integer.parseInt(intent.getStringExtra("key")) );
                libraryFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameLayout,libraryFragment);
                fragmentTransaction.commit();
            }catch (Exception e){
                naviBottom.show(ID_HOME,true);
                replaceFragment(new HomeFragment());
            }

        }else{
            naviBottom.show(ID_HOME,true);
            replaceFragment(new HomeFragment());
        }
    }


    private void checkPermissionCamera() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
    }

    private void events() {
        naviBottom.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case ID_HOME:
                        replaceFragment(new HomeFragment());
                        break;
                    case ID_DATA:
                        replaceFragment(new LibraryFragment());
                        break;

                }
            }
        });
        naviBottom.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name = "";
                switch (item.getId()){
                    case ID_HOME:
                        name = "Home";
                        break;
                    case ID_DATA:
                        name = "Library";
                        break;
                }
                txtLable.setText(name);
            }
        });
        naviBottom.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case ID_HOME:
                        replaceFragment(new HomeFragment());
                        break;
                    case ID_DATA:
                        replaceFragment(new LibraryFragment());
                        break;
                }
            }
        });
    }

    private void setUpNavi() {
        naviBottom.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.ic_baseline_home_24));
        naviBottom.add(new MeowBottomNavigation.Model(ID_DATA,R.drawable.ic_baseline_menu_book_24));
    }

    private void addControls() {
        txtLable = findViewById(R.id.textViewLabel);
        naviBottom = findViewById(R.id.navigationBottom);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    public void openSnakeDetailFragment(SnakeModel snakeModel){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailSnakeFragment snakeDetailFragment = new DetailSnakeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objectSnake", snakeModel);
        snakeDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout,snakeDetailFragment);
        fragmentTransaction.addToBackStack(DetailSnakeFragment.TAG);
        fragmentTransaction.commit();
    }


}