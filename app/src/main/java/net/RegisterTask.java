package net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import model.DataCache;
import request.RegisterRequest;
import result.EventsResult;
import result.PersonsResult;
import result.RegisterResult;

public class RegisterTask implements Runnable {
    private final Handler messageHandler;
    private String host;
    private String port;
    private final RegisterRequest[] registerAttempts;

    private DataCache data = DataCache.initialize();
    private DataTask DataInitialize;

    public RegisterTask(Handler messageHandler, String host, String port, RegisterRequest... registerAttempts) {
        this.messageHandler = messageHandler;
        this.host = host;
        this.port = port;
        this.registerAttempts = registerAttempts;
    }

    @Override
    public void run() {
        ServerProxy proxy = ServerProxy.initialize();
        RegisterResult result = proxy.register(host, port, registerAttempts[0]);
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
        messageBundle.putString("Register Success", success);
        message.setData(messageBundle);
        System.out.println(success);
        messageHandler.sendMessage(message);
    }
}
