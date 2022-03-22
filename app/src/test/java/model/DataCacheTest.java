package model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

import net.DataTask;

import java.util.List;
import java.util.Map;

import result.EventsResult;
import result.PersonsResult;

public class DataCacheTest {

    DataCache data = DataCache.initialize();

    @BeforeClass
    static public void setup() {
        Person p1 = new Person("person1ID", "person1", "Bob", "Joe",
                "m", "person2ID", "person3ID", "Person1Spouse");
        Person p2 = new Person("person2ID", "person2", "Kyle", "Last",
                "m", null, null, "Person2Spouse");
        Person p3 = new Person("person3ID", "person3", "Sarah", "Joe",
                "f", null, null, "Person3Spouse");
        Person[] persons;
        persons = new Person[3];
        persons[0] = p1;
        persons[1] = p2;
        persons[2] = p3;

        PersonsResult pResult = new PersonsResult(persons, null, true);

        Event e1 = new Event("event1ID", "person1", "person1ID", 0.0,0.0,
                "USA", "New York City", "Baptism", 1999);
        Event e2 = new Event("event2ID", "person2", "person2ID", 100.0,200.0,
                "Australia", "Sydney", "Marriage", 2002);
        Event e3 = new Event("event3ID", "person3", "person3ID", -300.0,400.0,
                "Canada", "Vancouver", "Death", 1970);
        Event[] events;
        events = new Event[3];
        events[0] = e1;
        events[1] = e2;
        events[2] = e3;

        EventsResult eResult = new EventsResult(events, null, true);

        DataTask dataTask = new DataTask();
        dataTask.initializeUser(pResult, eResult, p1.getPersonID());
    }

    @Test
    public void testPersons() {
        Map<String, Person> people = data.getPeople();
        Assert.assertEquals(3, people.size());
    }

    @Test
    public void testEvents() {
        Map<String, Event> events = data.getEvents();
        Assert.assertEquals(3, events.size());
    }

    @Test
    public void testPersonEvents() {
        Map<String, List<Event>> personEvents = data.getPersonEvents();
        List<Event> person1Events = personEvents.get("person1ID");
        Assert.assertEquals(1, person1Events.size());
    }

    @Test
    public void testFatherSide() {
        List<String> fatherSide = data.getFatherSide();
        Assert.assertEquals("person2ID", fatherSide.get(1));
    }

    @Test
    public void testMotherSide() {
        List<String> motherSide = data.getMotherSide();
        Assert.assertEquals("person3ID", motherSide.get(1));
    }

    @Test
    public void testMales() {
        List<String> males = data.getMales();
        Assert.assertEquals(2, males.size());
    }

    @Test
    public void testFemales() {
        List<String> females = data.getFemales();
        Assert.assertEquals(1, females.size());
    }
}
