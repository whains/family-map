package ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.DataCache;
import model.Event;
import model.Person;
import will.familymap.R;

public class PersonActivity extends AppCompatActivity {
    Person person;
    private TextView first;
    private TextView last;
    private TextView gender;
    private ExpandableListView view;
    private ExpandableListAdapter adapter;
    private DataCache data = DataCache.initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        first = findViewById(R.id.firstName);
        last = findViewById(R.id.lastName);
        gender = findViewById(R.id.gender);
        view = findViewById(R.id.expandableView);

        person = data.getPerson();

        first.setText(person.getFirstName());
        last.setText(person.getLastName());
        if (person.getGender().equals("m")) {
            gender.setText("Male");
        }
        else if (person.getGender().equals("f")) {
            gender.setText("Female");
        }

        view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = null;
                if (groupPosition == 0) {
                    intent = new Intent(PersonActivity.this, PersonActivity.class);
                    data.setPerson((Person) adapter.getChild(groupPosition, childPosition));
                }
                else {
                    intent = new Intent(PersonActivity.this, EventActivity.class);
                    data.setEvent((Event) adapter.getChild(groupPosition, childPosition));
                }
                startActivity(intent);
                return false;
            }
        });

        List<Person> family = new ArrayList<>();
        Map<String, Person> allPeople = data.getPeople();

        //Find father and mother
        if (person.getFatherID() != null) {
            for (Map.Entry<String, Person> entry : allPeople.entrySet()) {
                if (person.getFatherID().equals(entry.getValue().getPersonID())) {
                    family.add(entry.getValue());
                }
            }
        }
        if (person.getMotherID() != null) {
            for (Map.Entry<String, Person> entry : allPeople.entrySet()) {
                if (person.getMotherID().equals(entry.getValue().getPersonID())) {
                    family.add(entry.getValue());
                }
            }
        }

        //Find spouse
        if (person.getSpouseID() != null) {
            for (Map.Entry<String, Person> entry : allPeople.entrySet()) {
                if (person.getSpouseID().equals(entry.getValue().getPersonID())) {
                    family.add(entry.getValue());
                }
            }
        }

        //Find children
        for (Map.Entry<String, Person> entry : allPeople.entrySet()) {
            if (entry.getValue().getFatherID() != null && entry.getValue().getMotherID() != null) {
                if (person.getPersonID().equals(entry.getValue().getFatherID())
                        || person.getPersonID().equals(entry.getValue().getMotherID())) {
                    family.add(entry.getValue());
                }
            }
        }

        Map<String, List<Event>> allEvents = data.getPersonEvents();
        List<Event> lifeEvents = allEvents.get(person.getPersonID());
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : lifeEvents) {
            filteredEvents.add(event);
            if (data.isFatherFilterOn()) {
                if (!data.getFatherSide().contains(event.getPersonID())) {
                    filteredEvents.remove(event);
                }
            }
            if (data.isMotherFilterOn()) {
                if (!data.getMotherSide().contains(event.getPersonID())) {
                    filteredEvents.remove(event);
                }
            }
            if (data.isMaleFilterOn()) {
                if (!data.getMales().contains(event.getPersonID())) {
                    filteredEvents.remove(event);
                }
            }
            if (data.isFemaleFilterOn()) {
                if (!data.getFemales().contains(event.getPersonID())) {
                    filteredEvents.remove(event);
                }
            }
        }

        adapter = new PersonAdapter(person, family, filteredEvents, this);
        view.setAdapter(adapter);
    }
}