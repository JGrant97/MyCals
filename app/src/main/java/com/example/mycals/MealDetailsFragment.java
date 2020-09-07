package com.example.mycals;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MealDetailsFragment extends AppCompatDialogFragment {

    private TextView textViewResult;

    private String id;
    private String label;
    private String cals;
    private String carbs;
    private String fibre;
    private String salt;
    private String fat;
    private String protein;
    private String sugar;

    private String userID;
    private long meals = 0;

    private ArrayList<Meal> resultlist;
    private RecyclerView ResultList;
    private RecyclerView.Adapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef, myRef2;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_mealdetails, null);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("food");
        myRef2 = FirebaseDatabase.getInstance().getReference("users");

        myRef.keepSynced(true);
        myRef2.keepSynced(true);


        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
        final String Date = df.format(c);

        ResultList = (RecyclerView)view.findViewById(R.id.ResultList);
        resultlist = new ArrayList<>();

        id = getArguments().getString("id");
        label = getArguments().getString("Label");
        cals = getArguments().getString("Calories");
        carbs = getArguments().getString("Carbohydrates");
        fat = getArguments().getString("Fat");
        protein = getArguments().getString("Protein");
        fibre = getArguments().getString("Fibre");
        salt = getArguments().getString("Salt");
        sugar = getArguments().getString("Sugar");




        Meal meal = new Meal(label,carbs,cals,fibre,salt,fat,protein, sugar);

        resultlist.add(meal);



        ResultList.setHasFixedSize(true);
        ResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RVAdapter(resultlist, getContext());
        ResultList.setAdapter(adapter);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userID).child(Date).exists())
                {
                    meals = (dataSnapshot.child(userID).child(Date).getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setView(view)
                .setTitle("Meal Details")
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Add Meal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> details = new HashMap<>();
                        details.put("label", label);
                        details.put("cals", cals);
                        details.put("carbs", carbs);
                        details.put("fat", fat);
                        details.put("protein", protein);
                        details.put("fibre", fibre);
                        details.put("salt", salt);
                        details.put("sugar", sugar);


                        myRef2.child(userID).child(Date).child(String.valueOf(meals+1)).setValue(details);

                        final Toast toast = Toast.makeText(getActivity(), "Meal successfully added!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();;
                    }
                });
        return builder.create();
    }
}
