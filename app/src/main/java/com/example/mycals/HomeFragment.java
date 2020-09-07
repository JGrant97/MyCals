package com.example.mycals;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference  myRef;

    private String userID;
    private String label;
    private String cals;
    private String carbs;
    private String fibre;
    private String salt;
    private String fat;
    private String protein;
    private String sugar;

    private Date dt;
    private Calendar c;
    private String Date;

    private ArrayList<Meal> detailList;
    private RecyclerView DetailList;
    private RecyclerView.Adapter adapter;

    private TextView textViewTotal, textDate;
    private Button buttonNextDay, buttonPrevDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container,false);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(View.VISIBLE);

        getActivity().setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users");
        myRef.keepSynced(true);

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        textViewTotal = view.findViewById(R.id.textViewTotal);
        textDate = view.findViewById(R.id.textDate);
        DetailList = (RecyclerView)view.findViewById(R.id.DetailList);


        buttonNextDay = view.findViewById(R.id.buttonNextDay);
        buttonPrevDay = view.findViewById(R.id.buttonPrevDay);

        detailList = new ArrayList<>();

        dt = new Date();
        c = Calendar.getInstance();
        c.setTime(dt);
        dt = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
        Date = df.format(dt);

        buttonPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailList.clear();

                dt = new Date();
                c.add(Calendar.DATE, -1);
                dt = c.getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
                Date = df.format(dt);

                textDate.setText(String.valueOf("Daily Meal Details : " + Date));

                getInfo();

                final Toast toast = Toast.makeText(getActivity(), "Previous Day!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        toast.cancel();
                    }
                }, 1000);

            }
        });

        buttonNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailList.clear();

                dt = new Date();
                c.add(Calendar.DATE, 1);
                dt = c.getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
                Date = df.format(dt);

                textDate.setText(String.valueOf("Daily Meal Details : " + Date));

                getInfo();

                final Toast toast = Toast.makeText(getActivity(), "Next Day!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();;

                Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            toast.cancel();
                        }
                    }, 1000);

            }
        });


        getInfo();

        return view;
    }

    public void getInfo ()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                if (dataSnapshot.child(userID).child(Date).exists()) {
                    for (DataSnapshot ds : dataSnapshot.child(userID).child(Date).getChildren()) {
                        String meals = ds.getKey();

                        label = dataSnapshot.child(userID).child(Date).child(meals).child("label").getValue().toString();
                        carbs = dataSnapshot.child(userID).child(Date).child(meals).child("carbs").getValue().toString();
                        cals = dataSnapshot.child(userID).child(Date).child(meals).child("cals").getValue().toString();
                        fat = dataSnapshot.child(userID).child(Date).child(meals).child("fat").getValue().toString();
                        protein = dataSnapshot.child(userID).child(Date).child(meals).child("protein").getValue().toString();
                        salt = dataSnapshot.child(userID).child(Date).child(meals).child("salt").getValue().toString();
                        fibre = dataSnapshot.child(userID).child(Date).child(meals).child("fibre").getValue().toString();
                        sugar = dataSnapshot.child(userID).child(Date).child(meals).child("sugar").getValue().toString();

                        Meal meal = new Meal(label,carbs,cals,fibre,salt,fat,protein, sugar);

                        detailList.add(meal);

                        int Tcals = Integer.parseInt(String.valueOf(dataSnapshot.child(userID).child(Date).child(meals).child("cals").getValue()));
                        count = count + Tcals;
                        textDate.setText(String.valueOf("Daily Meal Details : " + Date));
                        textViewTotal.setText(String.valueOf("Total Calories : " + count));
                    }
                }else
                {
                    textViewTotal.setText("No meal for this day");
                }

                DetailList.setHasFixedSize(true);
                DetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new RVAdapter(detailList, getContext());
                DetailList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
