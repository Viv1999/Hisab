package com.vivek.hisab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupFragment extends Fragment {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private EditText etphone;
    private TextView tvLogin;
    private Button bSignup;

    private FirebaseAuth mAuth;
    private DatabaseReference dref;
    private OnFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_signup, container, false);
        // Inflate the layout for this fragment

        etEmail = v.findViewById(R.id.etEmailSignup);
        etPassword = v.findViewById(R.id.etPasswordSignup);
        etphone = v.findViewById(R.id.etPhoneSignup);
        etName = v.findViewById(R.id.etNameSignup);
        tvLogin = v.findViewById(R.id.tvGoToLogin);
        bSignup = v.findViewById(R.id.bSignup);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.switchFragment();
            }
        });

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final Long phone = Long.parseLong(etphone.getText().toString());
                final String name = etName.getText().toString();
                if(email==null || email.length()==0){
                    etEmail.setError("Enter an email");
                    etEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Enter a valid email address");
                    etEmail.requestFocus();
                }
                else if(password==""){
                    etPassword.setError("Please enter a password");
                    etPassword.requestFocus();
                }
                else if (password.length()<6){
                    etPassword.setError("Password length should be greater than 6");
                    etPassword.requestFocus();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"User Created",Toast.LENGTH_SHORT).show();
                                User user = new User(name,email,phone);
                                dref.setValue(user);
                                startActivity(new Intent(getContext(), MainActivity.class));

                            }
                            else{
                                Toast.makeText(getContext(),"Something happened",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                User user = new User(name,email,phone);
                dref.setValue(user);
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void switchFragment();
    }
}
