package ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.LoginTask;
import net.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import request.LoginRequest;
import request.RegisterRequest;
import will.familymap.R;

public class LoginFragment extends Fragment {

    private Button signInButton;
    private Button registerButton;

    private EditText host;
    private String strHost = null;
    private EditText port;
    private String strPort = null;
    private EditText username;
    private String strUsername = null;
    private EditText password;
    private String strPassword = null;
    private EditText firstName;
    private String strFirstName = null;
    private EditText lastName;
    private String strLastName = null;
    private EditText email;
    private String strEmail = null;
    private RadioGroup gender;
    private String strGender = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        host = v.findViewById(R.id.host);
        host.addTextChangedListener(new TextChange());

        port = v.findViewById(R.id.port);
        port.addTextChangedListener(new TextChange());

        username = v.findViewById(R.id.username);
        username.addTextChangedListener(new TextChange());

        password = v.findViewById(R.id.password);
        password.addTextChangedListener(new TextChange());

        firstName = v.findViewById(R.id.firstName);
        firstName.addTextChangedListener(new TextChange());

        lastName = v.findViewById(R.id.lastName);
        lastName.addTextChangedListener(new TextChange());

        email = v.findViewById(R.id.email);
        email.addTextChangedListener(new TextChange());

        gender = v.findViewById(R.id.genders);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (gender.getCheckedRadioButtonId() == R.id.male) {
                    strGender = "m";
                }
                else if (gender.getCheckedRadioButtonId() == R.id.female) {
                    strGender = "f";
                }
                enableButtons();
            }
        });
        signInButton = v.findViewById(R.id.signInButton);
        registerButton = v.findViewById(R.id.registerButton);
        enableButtons();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest lRequest = new LoginRequest(strUsername, strPassword);

                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String success = bundle.getString("Login Success");
                        if (success.equals("false")) {
                            Context context = getActivity();
                            CharSequence text = "Login Unsuccessful";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                            Context context = getActivity();
                            CharSequence text = "Login Successful\n"+success;
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            MainActivity activity = (MainActivity) context;
                            activity.loggedIn();
                        }
                    }
                };

                LoginTask loginTask = new LoginTask(uiThreadMessageHandler, strHost, strPort, lRequest);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRequest rRequest = new RegisterRequest(strUsername, strPassword, strEmail,
                        strFirstName, strLastName, strGender);

                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String success = bundle.getString("Register Success");
                        if (success.equals("false")) {
                            Context context = getActivity();
                            CharSequence text = "Register Unsuccessful";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                            Context context = getActivity();
                            CharSequence text = "Register Successful\n"+success;
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            MainActivity activity = (MainActivity) context;
                            activity.loggedIn();
                        }
                    }
                };

                RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, strHost, strPort, rRequest);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);

            }
        });

        return v;
    }

    private void enableButtons() {
        if (strHost == null || strPort == null || strUsername == null || strPassword == null) {
            signInButton.setEnabled(false);
        }
        else { signInButton.setEnabled(true); }

        if (strHost == null || strPort == null || strUsername == null || strPassword == null ||
                strFirstName == null || strLastName == null || strEmail == null || strGender == null) {
            registerButton.setEnabled(false);
        }
        else { registerButton.setEnabled(true); }
    }

    private class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(host.getText())) { strHost = host.getText().toString(); }
            if (!TextUtils.isEmpty(port.getText())) {strPort = port.getText().toString(); }
            if (!TextUtils.isEmpty(username.getText())) { strUsername = username.getText().toString(); }
            if (!TextUtils.isEmpty(password.getText())) { strPassword = password.getText().toString(); }
            if (!TextUtils.isEmpty(firstName.getText())) { strFirstName = firstName.getText().toString(); }
            if (!TextUtils.isEmpty(lastName.getText())) { strLastName = lastName.getText().toString(); }
            if (!TextUtils.isEmpty(email.getText())) { strEmail = email.getText().toString(); }

            enableButtons();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }
}