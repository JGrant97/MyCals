package com.example.mycals;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private  FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if (currentUser  != null)
        {
            if (currentUser.isEmailVerified())
            {
                if(savedInstanceState == null)
                {
                    bottomNav.setVisibility(View.VISIBLE);

                    FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
                    fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    fr.replace(R.id.fragment_container, new HomeFragment());
                    fr.commit();
                }else {

                }

            }else {
                bottomNav.setVisibility(View.INVISIBLE);
                FirebaseAuth.getInstance().signOut();

                FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fr.replace(R.id.fragment_container, new LoginFragment());
                fr.commit();

                final Toast toast = Toast.makeText(this, "Please verify your email before logging in!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
            }
        }else {
            bottomNav.setVisibility(View.GONE);

            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fr.replace(R.id.fragment_container, new LoginFragment());
            fr.commit();
        }



    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);



    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectFragment = null;

                    switch (item.getItemId()){
                        case R.id.home:
                            selectFragment = new HomeFragment();
                            break;
                        case R.id.add:
                            selectFragment = new AddMealFragment();
                            break;
                        case R.id.profile:
                            selectFragment = new ProfileFragment();
                            break;
                    }
                   // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                   //         selectFragment).commit();

                    FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
                    fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    fr.replace(R.id.fragment_container, selectFragment);
                    fr.commit();
                    return true;
                }
            };
}
