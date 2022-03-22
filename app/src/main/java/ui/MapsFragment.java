package ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DataCache;
import model.Event;
import model.Person;
import will.familymap.R;

public class MapsFragment extends Fragment {
    private GoogleMap map;
    private Map<Marker, Event> markerMap = new HashMap<>();
    private List<Polyline> lines = new ArrayList<>();
    private Marker currMarker;
    private ImageView icon;
    private TextView name;
    private TextView eventInfo;
    private TextView year;
    private Event currEvent;

    private DataCache data = DataCache.initialize();

    public MapsFragment() {}

    public MapsFragment(Event event) {
        this.currEvent = event;
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            Event event = markerMap.get(currMarker);
            Person person = data.getPeople().get(event.getPersonID());
            data.setPerson(person);
            startActivity(intent);
        }
    };

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.clear();

            placeMarkers();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if (itemID == R.id.settings_) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        if (currEvent == null) { setHasOptionsMenu(true); }
        else { setHasOptionsMenu(false); }

        name = v.findViewById(R.id.name);
        name.setOnClickListener(click);
        eventInfo = v.findViewById(R.id.eventInfo);
        eventInfo.setOnClickListener(click);
        year = v.findViewById(R.id.year);
        year.setOnClickListener(click);
        icon = v.findViewById(R.id.mapIcon);
        icon.setOnClickListener(click);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();

        if (data.isFinish()) {
            Context context = getActivity();
            MainActivity activity = (MainActivity) context;
            activity.loggedOut();
        }

        if (map != null && markerMap != null) {
            map.clear();
            markerMap = new HashMap<>();
            placeMarkers();
        }

        name.setText("");
        eventInfo.setText("");
        year.setText("");
        icon.setVisibility(View.INVISIBLE);

    }

    private void placeMarkers() {
        Map<String, Event> eventMap = data.getEvents();
        Map<String, Event> filteredEvents = new HashMap<>();

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

        for (Map.Entry<String, Event> entry : filteredEvents.entrySet()) {
            Event event = entry.getValue();
            LatLng position = new LatLng(event.getLatitude(), event.getLongitude());

            generateMarker(event.getEventType(), position, event);
        }

        if (currEvent != null) {
            for (Map.Entry<Marker, Event> entry : markerMap.entrySet()) {
                if (entry.getValue().getEventID().equals(currEvent.getEventID())) {
                    currMarker = entry.getKey();
                }
            }

            map.moveCamera(CameraUpdateFactory.newLatLng(currMarker.getPosition()));
            markerClick(currMarker);
            currEvent = null;
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currMarker = marker;
                markerClick(currMarker);
                return true;
            }
        });
    }

    private void generateMarker(String eventType, LatLng position, Event event) {
        Marker marker = null;

        if (eventType.toLowerCase().equals("birth")) {
            marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(eventType)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        else if (eventType.toLowerCase().equals("death")) {
            marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(eventType)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        else if (eventType.toLowerCase().equals("marriage")) {
            marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(eventType)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        else {
            marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(eventType)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }

        markerMap.put(marker, event);
    }

    private void markerClick(Marker marker) {
        for (Polyline line : lines) { line.remove(); }
        lines = new ArrayList<Polyline>();

        Event event = markerMap.get(marker);
        Person person = data.getPeople().get(event.getPersonID());

        name.setText(person.getFirstName() + " " + person.getLastName());
        eventInfo.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry());
        year.setText(Integer.toString(event.getYear()));
        icon.setVisibility(View.VISIBLE);

        if (person.getGender().toLowerCase().equals("m")) {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
        else {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.female));
        }

        if ((data.isSpouseLinesOn() && !data.isMaleFilterOn() && !data.isFemaleFilterOn())
        || (data.isSpouseLinesOn() && data.isMaleFilterOn() && data.isFemaleFilterOn())) {
            spouseLines(event);
        }
        if (data.isFamilyTreeLinesOn()) {
            familyLines(person, event, 10);
        }
        if (data.isLifeStoryLinesOn()) {
            storyLines(event);
        }
    }

    private void spouseLines(Event event) {
        LatLng position = new LatLng(event.getLatitude(), event.getLongitude());

        Map<String, Event> eventMap = data.getEvents();
        Person clicked = data.getPeople().get(event.getPersonID());

        String priorityEvent = "";
        int minYear = 9999;
        if (clicked.getSpouseID() != null) {

            for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
                Event priority = entry.getValue();
                Person spouse = data.getPeople().get(priority.getPersonID());

                if (clicked.getSpouseID().equals(spouse.getPersonID())) {

                    if (minYear > priority.getYear()) {
                        minYear = priority.getYear();
                        priorityEvent = priority.getEventType();
                    }
                }
            }

            for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
                Event spouseEvent = entry.getValue();

                if (spouseEvent.getEventType().equals(priorityEvent)) {
                    Person spouse = data.getPeople().get(spouseEvent.getPersonID());

                    if (spouse.getSpouseID() != null && clicked.getSpouseID() != null) {

                        if (clicked.getSpouseID().equals(spouse.getPersonID())) {

                            if (data.isFatherFilterOn() && !data.isMotherFilterOn()) {

                                for (String fatherSideID : data.getFatherSide()) {

                                    if (fatherSideID.equals(spouse.getPersonID())) {
                                        LatLng spousePosition = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                                        Polyline newLine = map.addPolyline(new PolylineOptions().add(position, spousePosition)
                                                .color(Color.BLUE).width(7));
                                        lines.add(newLine);
                                    }
                                }
                            }

                            else if (data.isMotherFilterOn() && !data.isFatherFilterOn()) {

                                for (String motherSideID : data.getMotherSide()) {

                                    if (motherSideID.equals(spouse.getPersonID())) {
                                        LatLng spousePosition = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                                        Polyline newLine = map.addPolyline(new PolylineOptions().add(position, spousePosition)
                                                .color(Color.BLUE).width(7));
                                        lines.add(newLine);
                                    }
                                }
                            }

                            else {
                                LatLng spousePosition = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                                Polyline newLine = map.addPolyline(new PolylineOptions().add(position, spousePosition)
                                        .color(Color.BLUE).width(7));
                                lines.add(newLine);
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    private void familyLines(Person person, Event event, int generation) {
        if (person.equals(data.getUser())) {
            if (data.isFatherFilterOn() && !data.isMotherFilterOn()) {
                if (person.getFatherID() != null) { fatherRecursion(person, event, generation); }
            }
            else if (data.isMotherFilterOn() && !data.isFatherFilterOn()) {
                if (person.getMotherID() != null) { motherRecursion(person, event, generation); }
            }
            else {
                if (person.getFatherID() != null) { fatherRecursion(person, event, generation); }
                if (person.getMotherID() != null) { motherRecursion(person, event, generation); }
            }
        }

        else {
            if (person.getFatherID() != null) { fatherRecursion(person, event, generation); }
            if (person.getMotherID() != null) { motherRecursion(person, event, generation); }
        }
    }

    private void fatherRecursion(Person person, Event event, int generation) {
        Map<String, Event> eventMap = data.getEvents();

        LatLng childPosition = new LatLng(event.getLatitude(), event.getLongitude());
        Person father = data.getPeople().get(person.getFatherID());

        String priorityEvent = "";
        int minYear = 9999;

        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event priority = entry.getValue();

            if (person.getFatherID().equals(priority.getPersonID())) {
                if (minYear > priority.getYear()) {
                    minYear = priority.getYear();
                    priorityEvent = priority.getEventType();
                }
            }
        }

        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event fatherEvent = entry.getValue();

            if (fatherEvent.getEventType().equals(priorityEvent)
                    && fatherEvent.getPersonID().equals(father.getPersonID())) {
                if ((!data.isFemaleFilterOn()) || (data.isFemaleFilterOn() && data.isMaleFilterOn())) {

                    LatLng fatherPosition = new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude());
                    Polyline newLine = map.addPolyline(new PolylineOptions().add(childPosition, fatherPosition)
                            .color(Color.RED).width(7));

                    lines.add(newLine);

                    familyLines(father, fatherEvent, generation / 2);
                }

                return;
            }
        }

    }

    private void motherRecursion(Person person, Event event, int generation) {
        Map<String, Event> eventMap = data.getEvents();

        LatLng childPosition = new LatLng(event.getLatitude(), event.getLongitude());
        Person mother = data.getPeople().get(person.getMotherID());

        String priorityEvent = "";
        int minYear = 9999;

        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event priority = entry.getValue();

            if (person.getMotherID().equals(priority.getPersonID())) {
                if (minYear > priority.getYear()) {
                    minYear = priority.getYear();
                    priorityEvent = priority.getEventType();
                }
            }
        }

        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event motherEvent = entry.getValue();

            if (motherEvent.getEventType().equals(priorityEvent)
                    && motherEvent.getPersonID().equals(mother.getPersonID())) {
                if ((!data.isMaleFilterOn()) || (data.isFemaleFilterOn() && data.isMaleFilterOn())) {

                    LatLng motherPosition = new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude());
                    Polyline newLine = map.addPolyline(new PolylineOptions().add(childPosition, motherPosition)
                            .color(Color.RED).width(7));

                    lines.add(newLine);

                    familyLines(mother, motherEvent, generation / 2);
                }

                return;
            }
        }
    }

    private void storyLines(Event event) {
        List<Event> personEvents = data.getPersonEvents().get(event.getPersonID());
        LatLng event1 = new LatLng(personEvents.get(0).getLatitude(), personEvents.get(0).getLongitude());

        for (int i = 1; i < personEvents.size(); i++) {
            LatLng event2 = new LatLng(personEvents.get(i).getLatitude(), personEvents.get(i).getLongitude());
            Polyline newLine = map.addPolyline(new PolylineOptions().add(event1, event2)
                    .color(Color.GREEN).width(7));
            event1 = new LatLng(personEvents.get(i).getLatitude(), personEvents.get(i).getLongitude());
            lines.add(newLine);
        }
    }
}