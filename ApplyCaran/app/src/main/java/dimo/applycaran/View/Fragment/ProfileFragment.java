package dimo.applycaran.View.Fragment;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import de.hdodenhof.circleimageview.CircleImageView;
import dimo.applycaran.R;
import dimo.applycaran.View.Activity.Main.LoginActivity;
import dimo.applycaran.View.Activity.Main.MainActivity;
import dimo.applycaran.View.Activity.Main.SignupActivity;

public class ProfileFragment extends Fragment {

    Activity mActivity;
    View mView;
    CircleImageView mProfileCircleImage;
    TextView mFragmentProfileName,mFragmentProfileAge,mFragmentProfileGender,mFragmentProfileLookingFor;
    AppCompatButton mFragmentProfileLogOut;
    FirebaseAuth mFirebaseAuth;
    String mUID;

    public ProfileFragment(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUID = mFirebaseAuth.getCurrentUser().getUid();
        //        mUID = "nXXdDMy1DpVtQYjdeOJYXb7wAqw2";

        FirebaseDatabase.getInstance().getReference().child("Users").child(mUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    int age = snapshot.child("age").getValue(Integer.class);
                    String looking_for = snapshot.child("looking_for").getValue(String.class);

                    mFragmentProfileName.setText(name);
                    mFragmentProfileAge.setText(age+"");
                    mFragmentProfileGender.setText(gender);
                    mFragmentProfileLookingFor.setText(looking_for);

                    if(!imageUrl.isEmpty()){
                        Glide.with(mActivity).load(imageUrl).into(mProfileCircleImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }

        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        DefineVariable();
        return mView;
    }

    private void DefineVariable() {
        mProfileCircleImage = mView.findViewById(R.id.FragmentProfileCircleImage);
        mFragmentProfileName = mView.findViewById(R.id.FragmentProfileName);
        mFragmentProfileAge = mView.findViewById(R.id.FragmentProfileAgeTV);
        mFragmentProfileGender = mView.findViewById(R.id.FragmentProfileGender);
        mFragmentProfileLookingFor = mView.findViewById(R.id.FragmentProfileLookingFor);
        mFragmentProfileLogOut = mView.findViewById(R.id.FragmentProfileLogOut);
        mFragmentProfileLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        });
    }

    private void CorrectFancyToast(String message) {
        FancyToast.makeText(mActivity, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
    }
}