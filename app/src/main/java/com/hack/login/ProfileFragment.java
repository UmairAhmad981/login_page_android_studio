package com.hack.login;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar pb;
    private String st;
    private DocumentReference userdata;
    ScrollView sc;
    // Declare TextViews
    private TextView name, about, username, gender, age, dob, mob, email;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profilefragmented, container, false);

        // Initialize ProgressBar
        pb = rootView.findViewById(R.id.progressBar4);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            st = "email/" + user.getEmail();
        }

        userdata = FirebaseFirestore.getInstance().document(st);

        // Initialize TextViews
        name = rootView.findViewById(R.id.name);
        about = rootView.findViewById(R.id.about);
        username = rootView.findViewById(R.id.username);
        gender = rootView.findViewById(R.id.gender);
        age = rootView.findViewById(R.id.age);
        dob = rootView.findViewById(R.id.dob);
        mob = rootView.findViewById(R.id.mob);
        email = rootView.findViewById(R.id.email);

        // Load user data
        loadUserProfile();

        return rootView;
    }

    private void loadUserProfile() {
        // Show progress bar while loading data
        pb.setVisibility(VISIBLE);

        userdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                // Hide progress bar after data is loaded
                pb.setVisibility(View.GONE);

                if (documentSnapshot.exists()) {
                    name.setText(documentSnapshot.getString("name"));
                    about.setText(documentSnapshot.getString("about"));
                    username.setText(documentSnapshot.getString("username"));
                    gender.setText(documentSnapshot.getString("gender"));
                    age.setText(documentSnapshot.getString("age"));
                    dob.setText(documentSnapshot.getString("dob"));
                    mob.setText(documentSnapshot.getString("mob"));
                    email.setText(documentSnapshot.getString("email"));
                }
            }
        }).addOnFailureListener(e -> {
            // Hide progress bar on failure
            pb.setVisibility(View.GONE);
        });
    }

    public String getmParam1() {
        return mParam1;
    }

    public String getmParam2() {
        return mParam2;
    }
}
