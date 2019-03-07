package com.vivek.hisab;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SpashActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    SignupFragment.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;
    FirebaseUser curUser;
    TextView tvLabel;
    DatabaseReference curUserRef;

    private TextView tvAppName;
    private ImageView imgLogo;
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        tvAppName = findViewById(R.id.tvAppName);
        imgLogo = findViewById(R.id.imgLogo);

        Intent intent = getIntent();
        int classId = intent.getIntExtra("CLASS_NAME", -1);

        FirebaseUser user= mAuth.getCurrentUser();

        if(user!=null){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    tvAppName.setVisibility(View.GONE);
                    imgLogo.setVisibility(View.GONE);

                }
            }, SPLASH_TIME_OUT);

            startActivity(new Intent(SpashActivity.this, MainActivity.class));
        }
        else{
            final LoginFragment loginFragment = LoginFragment.newInstance();
            final FragmentManager fragmentManager = getSupportFragmentManager();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    tvAppName.setVisibility(View.GONE);
                    imgLogo.setVisibility(View.GONE);

                }
            }, SPLASH_TIME_OUT);

            fragmentManager.beginTransaction().replace(R.id.content_splash, loginFragment).commit();
        }



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void switchFragment() {

        Fragment curFragment = getSupportFragmentManager().findFragmentById(R.id.content_splash);
        if(curFragment instanceof LoginFragment){
            Fragment fragment = SignupFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_splash, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (curFragment instanceof SignupFragment){
            Fragment fragment = LoginFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_splash, fragment).addToBackStack(null).commit();
        }
    }


}
