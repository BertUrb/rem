package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;

import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private final FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    private FirebaseUser mAgent;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            mAgent = FirebaseAuth.getInstance().getCurrentUser();
            startMainActivity();
        } else {
           checkInternet();

            }
        }

    private void checkInternet() {
        if(Utils.isInternetAvailable(getApplicationContext())) {
            launchSignInIntent();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppTheme);
            builder.setMessage(R.string.internet_is_required_first_run)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        // Fermer l'application
                        System.exit(0);
                    });
            AlertDialog alert = builder.create();
            alert.show();
    }

}


    @Override
    protected void onStart() {
        super.onStart();
        mAgent = mAuth.getCurrentUser();

        if(mAgent == null) {
            checkInternet();
        }
        else {
            startMainActivity();
        }
    }

    private void launchSignInIntent() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        signInLauncher.launch(signInIntent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}