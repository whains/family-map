package model;

import java.util.*;

public class DataCache {

    private static DataCache instance;

    public static DataCache initialize() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private Person user;

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private List<String> fatherSide;
    private List<String> motherSide;
    private List<String> males;
    private List<String> females;
    private Person person;
    private Event event;

    private boolean inSettings;

    private boolean lifeStoryLinesOn;
    private boolean familyTreeLinesOn;
    private boolean spouseLinesOn;

    private boolean fatherFilterOn;
    private boolean motherFilterOn;
    private boolean maleFilterOn;
    private boolean femaleFilterOn;

    private boolean finish;

    private DataCache() {
        user = null;
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        fatherSide = new ArrayList<>();
        motherSide = new ArrayList<>();
        males = new ArrayList<>();
        females = new ArrayList<>();
        person = null;
        event = null;
        lifeStoryLinesOn = true;
        familyTreeLinesOn = true;
        spouseLinesOn = true;
        fatherFilterOn = false;
        motherFilterOn = false;
        maleFilterOn = false;
        femaleFilterOn = false;
        inSettings = false;
        finish = false;
    }

    private void clear() {
        people.clear();
        events.clear();
        personEvents.clear();
        person = null;
        event = null;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public void setPerson(Person p) { this.person = p; }
    public Person getPerson() { return person; }
    public void setEvent(Event e) { this.event = e; }
    public Event getEvent() { return event; }

    private List<Event> sort(List<Event> events) {
        List<Event> sortedList = new ArrayList<>();

        while (events.size() > 0) {
            int j = 0;
            Event curr = events.get(0);
            for (int i = 0; i < events.size(); ++i) {
                if (events.get(i).getYear() < curr.getYear()) {
                    curr = events.get(i);
                    j = i;
                }
            }
            sortedList.add(curr);
            events.remove(j);
        }

        return sortedList;
    }

    public void calcPersonEvents() {
        personEvents.clear();

        for (Event event : events.values()) {
            String personID = event.getPersonID();

            List<Event> eventList = null;
            if (personEvents.containsKey(personID)) {
                eventList = personEvents.get(personID);
            }
            else {
                eventList = new ArrayList<>();
                personEvents.put(personID, eventList);
            }

            eventList.add(event);
        }

        for (List<Event> eventList : personEvents.values()) {
            sort(eventList);
        }
    }

    public List<String> getMales() {
        return males;
    }

    public void setMales(List<String> males) {
        this.males = males;
    }

    public List<String> getFemales() {
        return females;
    }

    public void setFemales(List<String> females) {
        this.females = females;
    }

    public boolean isInSettings() {
        return inSettings;
    }

    public void setInSettings(boolean inSettings) {
        this.inSettings = inSettings;
    }

    public List<String> getFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(List<String> fatherSide) {
        this.fatherSide = fatherSide;
    }

    public List<String> getMotherSide() {
        return motherSide;
    }

    public void setMotherSide(List<String> motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isFatherFilterOn() {
        return fatherFilterOn;
    }

    public void setFatherFilterOn(boolean fatherFilterOn) {
        this.fatherFilterOn = fatherFilterOn;
    }

    public boolean isMotherFilterOn() {
        return motherFilterOn;
    }

    public void setMotherFilterOn(boolean motherFilterOn) {
        this.motherFilterOn = motherFilterOn;
    }

    public boolean isMaleFilterOn() {
        return maleFilterOn;
    }

    public void setMaleFilterOn(boolean maleFilterOn) {
        this.maleFilterOn = maleFilterOn;
    }

    public boolean isFemaleFilterOn() {
        return femaleFilterOn;
    }

    public void setFemaleFilterOn(boolean femaleFilterOn) {
        this.femaleFilterOn = femaleFilterOn;
    }

    public boolean isLifeStoryLinesOn() {
        return lifeStoryLinesOn;
    }

    public void setLifeStoryLinesOn(boolean lifeStoryLinesOn) {
        this.lifeStoryLinesOn = lifeStoryLinesOn;
    }

    public boolean isFamilyTreeLinesOn() {
        return familyTreeLinesOn;
    }

    public void setFamilyTreeLinesOn(boolean familyTreeLinesOn) {
        this.familyTreeLinesOn = familyTreeLinesOn;
    }

    public boolean isSpouseLinesOn() {
        return spouseLinesOn;
    }

    public void setSpouseLinesOn(boolean spouseLinesOn) {
        this.spouseLinesOn = spouseLinesOn;
    }

    public static DataCache getInstance() {
        return instance;
    }

    public static void setInstance(DataCache instance) {
        DataCache.instance = instance;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }
}
