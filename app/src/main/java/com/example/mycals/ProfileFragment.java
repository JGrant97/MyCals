package com.example.mycals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private Button buttonLogout;
    private List<String> userlist;
    private ListView UserList;
    ArrayAdapter<String> adapter;
    private String userID;

    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container,false);

        UserList = (ListView)view.findViewById(R.id.UserList);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        userlist = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        getActivity().setTitle("My Profile");

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(View.VISIBLE);

        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        getinfo();


        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fr.replace(R.id.fragment_container, new LoginFragment());
        fr.commit();
    }

    public void getinfo(){
        final FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();

        userlist.add("E-mail: "+ user.getEmail());

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,userlist);
        UserList.setAdapter(adapter);



    }
}


