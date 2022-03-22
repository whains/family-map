package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DataCache;
import model.Event;
import model.Person;
import will.familymap.R;

public class SearchActivity extends AppCompatActivity {
    private DataCache data = DataCache.initialize();

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;

    private EditText searchBar;

    private Context context = this;

    Map<String, Event> eventMap = data.getEvents();
    Map<String, Event> filteredEvents = new HashMap<>();

    Map<String, Person> people = data.getPeople();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            filteredEvents.put(entry.getKey(), entry.getValue());
            if (data.isFatherFilterOn()) {
                if (!data.getFatherSide().contains(entry.getValue().getPersonID())) {
                    filteredEvents.remove(entry.getKey());
                }
            }
            if (data.isMotherFilterOn()) {
                if (!data.getMotherSide().contains(entry.getValue().getPersonID())) {
                    filteredEvents.remove(entry.getKey());
                }
            }
            if (data.isMaleFilterOn()) {
                if (!data.getMales().contains(entry.getValue().getPersonID())) {
                    filteredEvents.remove(entry.getKey());
                }
            }
            if (data.isFemaleFilterOn()) {
                if (!data.getFemales().contains(entry.getValue().getPersonID())) {
                    filteredEvents.remove(entry.getKey());
                }
            }
        }

        searchBar = (EditText) findViewById(R.id.searchBox);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searched = s.toString();
                List<Object> searchResults = new ArrayList<>();

                for (Person person : people.values()) {
                    if (person.getFirstName().toLowerCase().contains(searched.toLowerCase())
                    || person.getLastName().toLowerCase().contains(searched.toLowerCase())) {
                        searchResults.add(person);
                    }
                }

                for (Event event : filteredEvents.values()) {
                    String year = event.getYear() + "";
                    if (event.getCountry().toLowerCase().contains(searched.toLowerCase())
                    || event.getCity().toLowerCase().contains(searched.toLowerCase())
                    || event.getEventType().toLowerCase().contains(searched.toLowerCase())
                    || year.contains(searched.toLowerCase())) {
                        searchResults.add(event);
                    }
                }

                if (searchResults.size() != 0) {
                    search(searchResults);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        recycler = findViewById(R.id.searchRecycle);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void search(List<Object> searchResults) {
        adapter = new SearchAdapter(searchResults, this);
        recycler.setAdapter(adapter);
    }
}