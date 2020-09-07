package com.example.mycals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {

    private EditText editTextCPassword, editTextPassword, editTextEmail;
    private TextView textViewLogin;
    private Button buttonRegister;

    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container,false);

        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        textViewLogin = (TextView) view.findViewById(R.id.textViewLogin);
        editTextCPassword = (EditText) view.findViewById(R.id.editTextCPassword);

        mAuth = FirebaseAuth.getInstance();

        getActivity().setTitle("Register");


        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(View.GONE);

        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fr.replace(R.id.fragment_container, new LoginFragment());
                fr.commit();            }
        });
        return view;
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cpassword = editTextCPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            final Toast toast = Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            final Toast toast = Toast.makeText(getActivity(), "Please Enter password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        if (TextUtils.isEmpty(cpassword)) {
            final Toast toast = Toast.makeText(getActivity(), "Please confirm passwords", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        if (!password.equals(cpassword)) {
            final Toast toast = Toast.makeText(getActivity(), "Please ensure passwords match", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                final Toast toast = Toast.makeText(getActivity(), "User registered successfully! Check your emails to confirm account", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0,0);
                                                toast.show();;

                                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                                fr.replace(R.id.fragment_container, new LoginFragment());
                                                fr.commit();
                                            }else
                                            {
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                    }
                });
    }
}
