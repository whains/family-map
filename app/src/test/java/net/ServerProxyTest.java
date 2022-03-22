package net;

import org.junit.Assert;
import org.junit.Test;

import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

public class ServerProxyTest {

    @Test
    public void registerSuccess() {
        ServerProxy proxy = new ServerProxy();

        RegisterRequest request = new RegisterRequest("username", "password",
                "email@gmail.com", "will", "h","m");
        RegisterResult result = proxy.register("127.0.0.1", "8080", request);
        Assert.assertNotNull(result.getUsername());
    }

    @Test
    public void registerFailure() {
        ServerProxy proxy = new ServerProxy();
        RegisterRequest request = new RegisterRequest("username", "password",
                "email@gmail.com", "will", "h","m");
        RegisterResult result = proxy.register("127.0.0.1", "8080", request);
        Assert.assertNotNull(result.getMessage());
    }

    @Test
    public void loginSuccess() {
        ServerProxy proxy = new ServerProxy();
        LoginRequest request = new LoginRequest("username", "password");
        LoginResult result = proxy.login("127.0.0.1", "8080", request);
        Assert.assertNotNull(result.getUsername());
    }

    @Test
    public void loginFailure() {
        ServerProxy proxy = new ServerProxy();
        LoginRequest request = new LoginRequest("fakeuser", "password");
        LoginResult result = proxy.login("127.0.0.1", "8080", request);
        Assert.assertNotNull(result.getMessage());
    }

    @Test
    public void getPersonsSuccess() {
        ServerProxy proxy = new ServerProxy();
        LoginRequest request = new LoginRequest("username", "password");
        LoginResult result = proxy.login("127.0.0.1", "8080", request);
        String authToken = result.getAuthtoken();
        PersonsResult persons = proxy.getAllPersons("127.0.0.1", "8080", authToken);
        Assert.assertNotNull(persons.getData());
    }

    @Test
    public void getPersonsFailure() {
        ServerProxy proxy = new ServerProxy();
        PersonsResult persons = proxy.getAllPersons("127.0.0.1", "8080", "fake");
        Assert.assertNotNull(persons.getMessage());
    }

    @Test
    public void getEventsSuccess() {
        ServerProxy proxy = new ServerProxy();        LoginRequest request = new LoginRequest("username", "password");
        LoginResult result = proxy.login("127.0.0.1", "8080", request);
        String authToken = result.getAuthtoken();
        EventsResult events = proxy.getAllEvents("127.0.0.1", "8080", authToken);
        Assert.assertNotNull(events.getData());
    }

    @Test
    public void getEventsFailure() {
        ServerProxy proxy = new ServerProxy();
        EventsResult events = proxy.getAllEvents("127.0.0.1", "8080", "fake");
        Assert.assertNotNull(events.getMessage());
    }

}
