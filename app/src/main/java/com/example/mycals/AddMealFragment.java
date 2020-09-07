package com.example.mycals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddMealFragment extends Fragment {

    private SurfaceView cameraPreview;
    private Button buttonReset, buttonManual;
    private TextView textViewResult;
    private boolean firstDetecton = true;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String id;
    private String label;
    private String cals;
    private String carbs;
    private String fibre;
    private String salt;
    private String fat;
    private String protein;
    private String sugar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_addmeal, container,false);
        getActivity().setTitle("Add Meal");

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("food");
        myRef.keepSynced(true);

        buttonReset = (Button)view.findViewById(R.id.buttonReset);
        buttonManual = (Button)view.findViewById(R.id.buttonManual);
        cameraPreview = (SurfaceView) view.findViewById(R.id.cameraPreview);
        textViewResult = (TextView) view.findViewById(R.id.textViewResult) ;

        createCameraSoucre();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label ="";
                cals = "";
                carbs = "";
                salt = "";
                protein = "";
                fibre = "";
                fat = "";
                sugar = "";
                textViewResult.setText("");
                firstDetecton = true;

                final Toast toast = Toast.makeText(getActivity(), "Scanner reset!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();;

            }
        });

        buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fr.replace(R.id.fragment_container, new MealDetailsManualFragment());
                fr.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createCameraSoucre();
    }

    public void createCameraSoucre(){
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity()).build();
        final CameraSource cameraSource = new CameraSource.Builder(getActivity(),barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(700,700)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},1);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
                cameraSource.release();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode>barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0 && firstDetecton)
                {
                    Intent intent=new Intent();
                    final Intent code =  intent.putExtra("barcode", barcodes.valueAt(0));
                    final Barcode barcode = code.getParcelableExtra("barcode");
                    id = barcode.displayValue;

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id).exists()){
                                firstDetecton = false;

                                label =dataSnapshot.child(id).child("label").getValue().toString();
                                cals = String.valueOf(dataSnapshot.child(id).child("cals").getValue());
                                carbs = dataSnapshot.child(id).child("carbs").getValue().toString();
                                fat = dataSnapshot.child(id).child("fat").getValue().toString();
                                protein = dataSnapshot.child(id).child("protein").getValue().toString();
                                salt = dataSnapshot.child(id).child("salt").getValue().toString();
                                fibre = dataSnapshot.child(id).child("fibre").getValue().toString();
                                sugar = dataSnapshot.child(id).child("sugar").getValue().toString();


                                textViewResult.setText(id);

                                MealDetailsFragment mealDetails = new MealDetailsFragment();
                                Bundle args = new Bundle();
                                args.putString("id", id);
                                args.putString("Label", label);
                                args.putString("Calories", cals);
                                args.putString("Carbohydrates", carbs);
                                args.putString("Fat", fat);
                                args.putString("Protein", protein);
                                args.putString("Fibre", fibre);
                                args.putString("Salt", salt);
                                args.putString("Sugar", sugar);


                                mealDetails.setArguments(args);

                                mealDetails.show(getFragmentManager(),"Meal Details Dialog");


                            }else
                            {
                                textViewResult.setText("Barcode not found! Try entering meal details manually!");

                                firstDetecton = false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
