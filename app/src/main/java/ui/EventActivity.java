package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import model.DataCache;
import model.Event;
import will.familymap.R;

public class EventActivity extends AppCompatActivity {
    private FragmentManager manager = getSupportFragmentManager();
    private Fragment fragment = manager.findFragmentById(R.id.fragment_container);

    DataCache data = DataCache.initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Event event = data.getEvent();
        fragment = new MapsFragment(event);

        manager.beginTransaction().add(R.id.map_fragment, fragment).commit();
    }
}