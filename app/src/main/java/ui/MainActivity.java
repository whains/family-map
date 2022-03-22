package ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;

import will.familymap.R;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager = getSupportFragmentManager();
    private Fragment fragment = manager.findFragmentById(R.id.fragment_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (fragment == null) {
            fragment = new LoginFragment();
        }
        else {
            fragment = new MapsFragment();
        }

        manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    public void loggedIn() {
        fragment = new MapsFragment();
        manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    //Found on stack overflow. Just restarts the app
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loggedOut() {
        Intent mStartActivity = new Intent(MainActivity.this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}