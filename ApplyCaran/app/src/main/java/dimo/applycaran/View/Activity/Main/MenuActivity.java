package dimo.applycaran.View.Activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dimo.applycaran.R;
import dimo.applycaran.View.Fragment.ChatsFragment;
import dimo.applycaran.View.Fragment.HomeFragment;
import dimo.applycaran.View.Fragment.ProfileFragment;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new ChatsFragment()).commit();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new HomeFragment( MenuActivity.this)).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
//                SetNameAndWelcome();
                fragment = new HomeFragment(MenuActivity.this);
                break;
            case R.id.nav_profile:
//                SetProfileBox();
                fragment = new ProfileFragment(MenuActivity.this);
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();

        return true;
    }
}