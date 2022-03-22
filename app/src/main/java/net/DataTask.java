package net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.DataCache;
import model.Event;
import model.Person;
import result.EventsResult;
import result.PersonsResult;

public class DataTask {
    DataCache data = DataCache.initialize();

    private List<String> fatherSide = new ArrayList<>();
    private List<String> motherSide = new ArrayList<>();

    public void initializeUser(PersonsResult persons, EventsResult events, String result) {
        initializePersons(persons);
        initializeEvents(events);
        initializePersonEvents(data.getEvents());

        Person user = data.getPeople().get(result);

        data.setUser(user);

        fatherSide.add(user.getPersonID());
        fatherSide.add(user.getFatherID());
        initializeFatherSide(data.getPeople().get(user.getFatherID()), 5);

        motherSide.add(user.getPersonID());
        motherSide.add(user.getMotherID());
        initializeMotherSide(data.getPeople().get(user.getMotherID()), 5);

        data.setFatherSide(fatherSide);
        data.setMotherSide(motherSide);

        data.setMales(initializeMales());
        data.setFemales(initializeFemales());
    }

    private void initializePersons(PersonsResult persons) {
        Map<String, Person> personMap = new HashMap<String, Person>();
        Person[] pArray = persons.getData();

        for (Person p : pArray) { personMap.put(p.getPersonID(), p); }

        data.setPeople(personMap);
    }

    private void initializeEvents(EventsResult events) {
        Map<String, Event> eventMap = new HashMap<String, Event>();
        Event[] eArray = events.getData();

        for (Event e : eArray) { eventMap.put(e.getEventID(), e); }

        data.setEvents(eventMap);
        data.calcPersonEvents();
    }

    private void initializePersonEvents(Map<String, Event> eventMap) {
        Map<String, List<Event>> personEvents = new HashMap<>();

        for (Map.Entry<String, Person> p : data.getPeople().entrySet()) {
            List<Integer> eventYears = new ArrayList<Integer>();
            List<Event> events = new ArrayList<Event>();

            for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
                if (p.getKey().equals(entry.getValue().getPersonID())) {
                    eventYears.add(entry.getValue().getYear());
                }
            }

            Set<Integer> yearSet = new HashSet<Integer>();
            for (int year : eventYears) { yearSet.add(year); }
            eventYears = new ArrayList<Integer>();
            for (int year : yearSet) { eventYears.add(year); }

            Collections.sort(eventYears);
            for (int year : eventYears) {
                for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
                    if (p.getValue().getPersonID().equals(entry.getValue().getPersonID())
                            && entry.getValue().getYear() == year) {
                        events.add(entry.getValue());
                    }
                }
            }

            personEvents.put(p.getKey(), events);
        }

        data.setPersonEvents(personEvents);
    }

    private void initializeFatherSide(Person person, int generation) {
        if (person.getFatherID() != null) { fatherSideFatherRecursion(person, generation); }
        if (person.getMotherID() != null) { fatherSideMotherRecursion(person, generation); }
    }

    private void fatherSideFatherRecursion(Person person, int generation) {
        Person father = data.getPeople().get(person.getFatherID());
        fatherSide.add(father.getPersonID());

        initializeFatherSide(father, generation / 2);

        return;
    }

    private void fatherSideMotherRecursion(Person person, int generation) {
        Person mother = data.getPeople().get(person.getMotherID());
        fatherSide.add(mother.getPersonID());

        initializeFatherSide(mother, generation / 2);

        return;
    }

    private void initializeMotherSide(Person person, int generation) {
        if (person.getFatherID() != null) { motherSideFatherRecursion(person, generation); }
        if (person.getMotherID() != null) { motherSideMotherRecursion(person, generation); }
    }

    private void motherSideFatherRecursion(Person person, int generation) {
        Person father = data.getPeople().get(person.getFatherID());
        motherSide.add(father.getPersonID());

        initializeFatherSide(father, generation / 2);

        return;
    }

    private void motherSideMotherRecursion(Person person, int generation) {
        Person mother = data.getPeople().get(person.getMotherID());
        motherSide.add(mother.getPersonID());

        initializeFatherSide(mother, generation / 2);

        return;
    }

    private List<String> initializeMales() {
        List<String> males = new ArrayList<>();
        Map<String, Person> people = data.getPeople();

        for (Map.Entry<String, Person> personEntry : people.entrySet()) {
            if (personEntry.getValue().getGender().toLowerCase().equals("m")) {
                males.add(personEntry.getValue().getPersonID());
            }
        }

        return males;
    }

    private List<String> initializeFemales() {
        List<String> females = new ArrayList<>();
        Map<String, Person> people = data.getPeople();

        for (Map.Entry<String, Person> personEntry : people.entrySet()) {
            if (personEntry.getValue().getGender().toLowerCase().equals("f")) {
                females.add(personEntry.getValue().getPersonID());
            }
        }

        return females;
    }
}
