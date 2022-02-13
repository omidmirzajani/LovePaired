package dimo.applycaran.View.Activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.loadingview.LoadingView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import dimo.applycaran.Models.User;
import dimo.applycaran.R;

public class SignupActivity extends AppCompatActivity {
    AppCompatButton mLoginButton;
    TextView mSignIn;
    LoadingView mLoadingView;
    EditText mFullName,mPassword,mEmail;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        DefineVariables();

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(FullNameText().isEmpty()){
                    ErrorFancyToast(getString(R.string.FullIsEmpty));
                }
                else if(PasswordText().isEmpty()){
                    ErrorFancyToast(getString(R.string.PasswordIsEmpty));
                }
                else{
                    StartLoadingView();
                    mFirebaseAuth.createUserWithEmailAndPassword(EmailText(),PasswordText()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            StopLoadingView();
                            Log.i("Dimo",EmailText());
                            Log.i("Dimo",PasswordText());
                            if(task.isSuccessful()){
//                                mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>(){
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()){

                                            String my_uid = mFirebaseAuth.getCurrentUser().getUid();
                                            Log.i("DimoDimo",my_uid);
                                            User u = new User();
                                            u.setName(FullNameText());
                                            u.setEmail(EmailText());
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(my_uid).push().setValue(u);
                                            FirebaseDatabase.getInstance().getReference().child("Accepted").child(my_uid).child("empty").push().setValue(".");

                                            FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot ds:snapshot.getChildren()) {
                                                        if(!ds.getKey().equals(my_uid)) {
                                                            FirebaseDatabase.getInstance().getReference().child("Unvisited").child(ds.getKey()).child(my_uid).setValue("");
                                                            FirebaseDatabase.getInstance().getReference().child("Unvisited").child(my_uid).child(ds.getKey()).setValue("");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            CorrectFancyToast(getString(R.string.CheckEmail));
                                            Intent intent = new Intent(SignupActivity.this, MenuActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
//                                        else{
//                                            ErrorFancyToast(task.getException().getMessage());
//                                        }
//                                    }
//                                });
//                            }
                            else{
                                ErrorFancyToast(task.getException().getMessage()+"***");
                            }
                        }
                    });
                }
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void CorrectFancyToast(String message) {
        FancyToast.makeText(SignupActivity.this, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

    }

    private void ErrorFancyToast(String message) {
        FancyToast.makeText(SignupActivity.this, message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
    }

    private void StartLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.start();
    }
    private void StopLoadingView() {
        mLoadingView.setVisibility(View.GONE);
        mLoadingView.stop();
    }

    private void DefineVariables() {
        mFullName=findViewById(R.id.atvUsernameReg);
        mPassword=findViewById(R.id.atvPasswordReg);
        mEmail=findViewById(R.id.atvEmailReg);
        mLoginButton=findViewById(R.id.btnSignUp);
        mSignIn = findViewById(R.id.tvSignIn);
        mLoadingView = findViewById(R.id.SignUpLoadingView);
        mFirebaseAuth=FirebaseAuth.getInstance();

    }
    private String EmailText(){
        return mEmail.getText()+"";
    }
    private String PasswordText() {
        return mPassword.getText()+"";
    }
    private String FullNameText() {
        return mFullName.getText()+"";
    }
}