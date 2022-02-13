package dimo.applycaran.View.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import dimo.applycaran.Models.Contacts;
import dimo.applycaran.Models.UnvisitedUser;
import dimo.applycaran.Models.User;
import dimo.applycaran.R;
import dimo.applycaran.View.Activity.Main.SignupActivity;
import me.thanel.swipeactionview.SwipeActionView;
import me.thanel.swipeactionview.SwipeGestureListener;

public class HomeFragment extends Fragment {
    Activity mActivity;
    ArrayList<UnvisitedUser> mUnvisitedUsers;
    FirebaseAuth mFirebaseAuth;
    String mUID;
    ImageView mUserImageView;
    TextView mIdUsername;
    RelativeLayout mAllSeen,mAllNotSeen;
    AppCompatButton mAllSeenBtn;

    public HomeFragment(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DimoDimo", "Unvisited");

        mUnvisitedUsers = new ArrayList<UnvisitedUser>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUID = mFirebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Unvisited").child(mUID).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("DimoDimo",snapshot.getKey() + "Unvisited");

                for(DataSnapshot ds:snapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(ds.getKey()).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue(String.class);
                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                            UnvisitedUser user = new UnvisitedUser(name,imageUrl);
                            user.uid = snapshot.getKey();
                            Log.i("DimoDimo",snapshot.getKey());
                            if(!mUnvisitedUsers.contains(user))
                                mUnvisitedUsers.add(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DefineVariables(view);

        mAllSeenBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoadUnvisitedUser();
            }
        });

        LoadUnvisitedUser();
        SwipeGestureListener swipeGestureListener = new SwipeGestureListener() {
            @Override
            public void onSwipeRightComplete(SwipeActionView swipeActionView) {
                // do nothing
            }

            @Override
            public void onSwipeLeftComplete(SwipeActionView swipeActionView) {
                // do nothing
            }

            @Override
            public boolean onSwipedLeft(@NonNull SwipeActionView swipeActionView) {
                RemoveUser();
                return true;
            }

            @Override
            public boolean onSwipedRight(@NonNull SwipeActionView swipeActionView) {
                String userUID = mUnvisitedUsers.get(0).uid;

                FirebaseDatabase.getInstance().getReference().child("Accepted")
                        .child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(mUID)){
                            FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String myName = snapshot.child(mUID).child("name").getValue(String.class);
                                    String otherName = snapshot.child(userUID).child("name").getValue(String.class);
                                    String myImage = snapshot.child(mUID).child("image").getValue(String.class);
                                    String otherImage = snapshot.child(userUID).child("image").getValue(String.class);
                                    Contacts me = new Contacts(myName,myImage);
                                    Contacts other = new Contacts(otherName,otherImage);

                                    FirebaseDatabase.getInstance().getReference().child("Accepted")
                                            .child(userUID).child(mUID).removeValue();

                                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(mUID).child(userUID).setValue(other);
                                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(userUID).child(mUID).setValue(me);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Accepted")
                                    .child(mUID).child(userUID).setValue("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                RemoveUser();
                return true;
            }
        };
        SwipeActionView swipeCardView = view.findViewById(R.id.swipe_card_view);
        swipeCardView.setSwipeGestureListener(swipeGestureListener);

        return view;
    }

    private void DefineVariables(View view) {
        mUserImageView = view.findViewById(R.id.UserImageView);
        mIdUsername = view.findViewById(R.id.IdUsername);
        mAllSeen = view.findViewById(R.id.AllSeen);
        mAllNotSeen = view.findViewById(R.id.AllNotSeen);
        mAllSeenBtn = view.findViewById(R.id.AllSeenBtn);
    }

    private void RemoveUser() {
        String userUID = mUnvisitedUsers.get(0).uid;
        FirebaseDatabase.getInstance().getReference().child("Unvisited")
                .child(mUID).child(userUID).removeValue();
        mUnvisitedUsers.remove(0);
        LoadUnvisitedUser();
    }

    private void LoadUnvisitedUser() {
        if(mUnvisitedUsers.size()>0) {
            mAllSeen.setVisibility(View.GONE);
            mAllNotSeen.setVisibility(View.VISIBLE);

            mIdUsername.setText(mUnvisitedUsers.get(0).name);
            String imageUrl = mUnvisitedUsers.get(0).imageUrl;

            if (!imageUrl.isEmpty()) {
                Glide.with(mActivity).load(imageUrl).into(mUserImageView);
            }
        } else {
            mAllSeen.setVisibility(View.VISIBLE);
            mAllNotSeen.setVisibility(View.GONE);
            InfoFancyToast("There are no new user to show.");
        }
    }


    private void InfoFancyToast(String message) {
        FancyToast.makeText(mActivity, message, FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
    }
}