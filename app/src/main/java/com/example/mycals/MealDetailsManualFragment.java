package com.example.mycals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MealDetailsManualFragment extends AppCompatDialogFragment {

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

    private String Date;

    private EditText editTextLabel, editTextfat, EditTextfibre, EditTextsalt, EditTextprotein,EditTextcals, EditTextcarbs, EditTextsugar;

    private String userID;
    private long meal = 0;

    private Button buttonSubmit, buttonBack;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef, myRef2;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manualaddmeal, container,false);
        getActivity().setTitle("Add Meal Manually");

        buttonSubmit = (Button)view.findViewById(R.id.buttonSubmit);
        buttonBack = (Button)view.findViewById(R.id.buttonBack);

        editTextLabel = (EditText)view.findViewById(R.id.editTextLabel);
        editTextfat = (EditText)view.findViewById(R.id.editTextfat);
        EditTextfibre = (EditText)view.findViewById(R.id.EditTextfibre);
        EditTextsalt = (EditText)view.findViewById(R.id.EditTextsalt);
        EditTextprotein = (EditText)view.findViewById(R.id.EditTextprotein);
        EditTextcals = (EditText)view.findViewById(R.id.EditTextcals);
        EditTextcarbs = (EditText)view.findViewById(R.id.EditTextcarbs);
        EditTextsugar = (EditText)view.findViewById(R.id.EditTextsugar);

        mAuth = FirebaseAuth.getInstance();
        myRef2 = FirebaseDatabase.getInstance().getReference("users");
        myRef2.keepSynced(true);


        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
        Date = df.format(c);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fr.replace(R.id.fragment_container, new AddMealFragment());
                fr.commit();
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userID).child(Date).exists())
                {
                    meal = (dataSnapshot.child(userID).child(Date).getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMeal();

            }
        });
        return view;
    }

        private void registerMeal() {

            label = editTextLabel.getText().toString();
            fat = editTextfat.getText().toString();
            fibre = EditTextfibre.getText().toString();
            carbs = EditTextcarbs.getText().toString();
            salt = EditTextsalt.getText().toString();
            protein = EditTextprotein.getText().toString();
            cals = EditTextcals.getText().toString();
            sugar = EditTextsugar.getText().toString();

            if (TextUtils.isEmpty(label) || !label.matches("[a-zA-Z0-9 ]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid label!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(salt) || !salt.matches("[0-9.]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid fat value in grams!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(fibre) || !fibre.matches("[0-9.]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid fibre value in grams!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(carbs) || !carbs.matches("[0-9.]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid carbohydrate value in grams!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(sugar) || !sugar.matches("[0-9.]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid sugar value in grams!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(protein) || !protein.matches("[0-9.]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid protein value in grams!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }

            if (TextUtils.isEmpty(cals) || !cals.matches("[0-9]*")) {
                final Toast toast = Toast.makeText(getActivity(), "Please enter a valid calorie value!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return;
            }



                    Map<String, Object> details = new HashMap<>();
                    details.put("label", label);
                    details.put("cals", cals);
                    details.put("carbs", carbs+"g");
                    details.put("fat", fat+"g");
                    details.put("protein", protein+"g");
                    details.put("fibre", fibre+"g");
                    details.put("salt", salt+"g");
                    details.put("sugar", sugar+"g");

                    myRef2.child(userID).child(Date).child(String.valueOf(meal+1)).setValue(details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            fr.replace(R.id.fragment_container, new AddMealFragment());
                            fr.commit();
                        }
                    });

                    final Toast toast = Toast.makeText(getActivity(), "Meal successfully added!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();;

        }
    }
