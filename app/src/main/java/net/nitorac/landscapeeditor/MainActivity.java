package net.nitorac.landscapeeditor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements InputFragment.OnFragmentInteractionListener, ResultsFragment.OnFragmentInteractionListener {

    private InputFragment inputFragment;
    private ResultsFragment resultsFragment;

    public Bitmap inputImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    if(inputFragment == null){
                        inputFragment = InputFragment.newInstance();
                    }
                    loadFragment(inputFragment);
                    return true;
                case R.id.navigation_results:
                    if(resultsFragment == null){
                        resultsFragment = ResultsFragment.newInstance();
                    }
                    loadFragment(resultsFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_input);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
