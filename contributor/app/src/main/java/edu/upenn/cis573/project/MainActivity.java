package edu.upenn.cis573.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static Contributor contributor;
    private DataManager dataManager = new DataManager(new WebClient("10.0.2.2", 3001));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClick(View view) {

        EditText loginField = findViewById(R.id.loginField);
        String login = loginField.getText().toString();

        EditText passwordField = findViewById(R.id.passwordField);
        String password = passwordField.getText().toString();


        try {
            contributor = dataManager.attemptLogin(login, password);
            if (contributor == null) {
                Toast.makeText(this, "Login failed! Please check your credentials.", Toast.LENGTH_LONG).show();
                // occurs when user/pw are empty/wrong
            } else {

                Intent i = new Intent(this, MenuActivity.class);

                startActivity(i);
            }
        } catch (IllegalStateException ise) {
            Toast.makeText(this, "Login failed! Check connection and try again.", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException iae) {
            Toast.makeText(this, "Login failed! Input error.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Unknown error. Please try again.", Toast.LENGTH_LONG).show();
        }



    }
}