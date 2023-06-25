package edu.upenn.cis573.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private DataManager dataManager = new DataManager(new WebClient("10.0.2.2", 3001));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }


    public void onViewDonationsButtonClick(View view) {

        Intent i = new Intent(this, ViewDonationsActivity.class);


        startActivity(i);

    }

    public void onMakeDonationButtonClick(View view) {

        Intent i = new Intent(this, MakeDonationActivity.class);


        startActivity(i);

    }

    public void onEditInfoButtonClick(View view) {
        AlertDialog.Builder pwDialog = new AlertDialog.Builder(this);
        pwDialog.setTitle("Verify Password");
        pwDialog.setCancelable(true);

        final EditText input = new EditText(this); // EditText extends TextView extends View
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwDialog.setView(input);

        pwDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String password = input.getText().toString();
                // Toast.makeText(MenuActivity.this, password, Toast.LENGTH_SHORT).show();
                try {
                    if (dataManager.checkPassword(MainActivity.contributor.getId(), password)) {
                        Intent intent = new Intent(MenuActivity.this, EditInfoActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MenuActivity.this, "Identity could not be verified.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    Toast.makeText(MenuActivity.this, "Error! Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        pwDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        pwDialog.show();
    }
}