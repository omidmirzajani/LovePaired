package dimo.applycaran.View.Activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.github.loadingview.LoadingView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import dimo.applycaran.R;

public class LoginActivity extends AppCompatActivity {
    AppCompatButton mLoginButton;
    TextView mSignUp;
    AutoCompleteTextView mUsername, mPassword;
    LoadingView mLoadingView;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DefineVariables();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UsernameText().isEmpty()) {
                    if (!PasswordText().isEmpty()) {
                        StartLoadingView();
                        mFirebaseAuth.signInWithEmailAndPassword(UsernameText(), PasswordText()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                StopLoadingView();
                                if (task.isSuccessful()) {
//                                    if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        String my_uid = mFirebaseAuth.getCurrentUser().getUid();
                                        myEdit.putString("uid", my_uid);
                                        myEdit.commit();

                                        Intent loginIntent = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(loginIntent);
                                        finish();
//                                    } else {
//                                        ErrorFancyToast("Please verify your email address.");
//                                    }
                                } else {
                                    Log.i("DimoDimo",task.getException().getMessage().toString());
                                    ErrorFancyToast(task.getException().getMessage());
                                }
                            }
                        });

                    } else {
                        ErrorFancyToast(getString(R.string.passtost));
                    }
                } else {
                    ErrorFancyToast(getString(R.string.usernametost));
                }
            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    private void DefineVariables() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mLoginButton = findViewById(R.id.btnSignIn);
        mLoadingView = findViewById(R.id.SignInLoadingView);
        mSignUp = findViewById(R.id.tvSignIn);
        mUsername = findViewById(R.id.atvEmailLog);
        mPassword = findViewById(R.id.atvPasswordLog);
    }

    private void CorrectFancyToast(String message) {
        FancyToast.makeText(LoginActivity.this, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

    }

    private void ErrorFancyToast(String message) {
        FancyToast.makeText(LoginActivity.this, message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
    }

    private void StartLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.start();
    }

    private void StopLoadingView() {
        mLoadingView.setVisibility(View.GONE);
        mLoadingView.stop();
    }

    private String UsernameText() {
        return mUsername.getText() + "";
    }

    private String PasswordText() {
        return mPassword.getText() + "";
    }
}