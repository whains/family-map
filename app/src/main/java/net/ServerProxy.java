package net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

public class ServerProxy {

    private static ServerProxy proxy;

    public static ServerProxy initialize() {
        if (proxy == null) {
            proxy = new ServerProxy();
        }
        return proxy;
    }

    public RegisterResult register(String serverHostName, String serverPortNumber, RegisterRequest request) {
        try {
            Gson gson = new Gson();

            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqBody = gson.toJson(request);
            OutputStream outBody = http.getOutputStream();
            writeString(reqBody, outBody);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResult result = gson.fromJson(respData, RegisterResult.class);
                return result;
            }
            else {
                return new RegisterResult(null, null, null,
                        false, "Error: HTTP error");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult(null, null, null,
                    false, "Error: Login error");
        }
    }

    LoginResult login(String serverHostName, String serverPortNumber, LoginRequest request) {
        try {
            Gson gson = new Gson();

            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqBody = gson.toJson(request);
            OutputStream outBody = http.getOutputStream();
            writeString(reqBody, outBody);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                LoginResult result = gson.fromJson(respData, LoginResult.class);
                return result;
            }
            else {
                return new LoginResult(null, null, null,
                        false, "Error: HTTP error");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new LoginResult(null, null, null,
                    false, "Error: Login error");
        }
    }

    public PersonsResult getAllPersons(String serverHostName, String serverPortNumber, String authToken) {
        try {
            Gson gson = new Gson();

            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                PersonsResult persons = gson.fromJson(respData, PersonsResult.class);
                return persons;
            }
            else {
                return new PersonsResult(null, "Error: HTTP error", false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new PersonsResult(null, "Error: Could not get all people", false);
        }
    }

    public EventsResult getAllEvents(String serverHostName, String serverPortNumber, String authToken) {
        try {
            Gson gson = new Gson();

            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                EventsResult events = gson.fromJson(respData, EventsResult.class);
                return events;
            }
            else {
                return new EventsResult(null, "Error: HTTP error", false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new EventsResult(null, "Error: Could not get all events", false);
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
