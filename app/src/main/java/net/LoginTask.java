package net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import model.DataCache;
import request.LoginRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;

public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private String host;
    private String port;
    private final LoginRequest[] loginAttempts;

    private DataCache data = DataCache.initialize();
    private DataTask DataInitialize;

    public LoginTask(Handler messageHandler, String host, String port, LoginRequest... loginAttempts) {
        this.messageHandler = messageHandler;
        this.host = host;
        this.port = port;
        this.loginAttempts = loginAttempts;
    }

    @Override
    public void run() {
        ServerProxy proxy = ServerProxy.initialize();
        LoginResult result = proxy.login(host, port, loginAttempts[0]);
        if (result.isSuccess()) {
            PersonsResult persons = proxy.getAllPersons(host, port, result.getAuthtoken());
            EventsResult events = proxy.getAllEvents(host, port, result.getAuthtoken());

            DataInitialize = new DataTask();
            DataInitialize.initializeUser(persons, events, result.getPersonID());

            String name = data.getPeople().get(result.getPersonID()).getFirstName() + " " +
                    data.getPeople().get(result.getPersonID()).getLastName();
            sendMessage(name);
        }
        else {
            sendMessage("false");
        }
    }

    private void sendMessage(String success) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString("Login Success", success);
        message.setData(messageBundle);
        System.out.println(success);
        messageHandler.sendMessage(message);
    }

}
