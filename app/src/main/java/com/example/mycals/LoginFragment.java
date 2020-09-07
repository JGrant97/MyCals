package com.example.mycals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment {

    private EditText editTextPassword, editTextEmail;
    private TextView textViewRegister;
    private Button buttonLogin;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        textViewRegister = (TextView) view.findViewById(R.id.textViewRegister);

        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Login");


        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fr.replace(R.id.fragment_container, new RegisterFragment());
                fr.commit();
            }
        });

        return view;
    }

    private void login() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified())
                            {
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                fr.replace(R.id.fragment_container, new HomeFragment());
                                fr.commit();
                            }else {
                                Toast.makeText(getActivity(), "Please verify your email before logging in!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
