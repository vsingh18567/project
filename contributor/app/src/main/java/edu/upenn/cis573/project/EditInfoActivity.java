package edu.upenn.cis573.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditInfoActivity extends AppCompatActivity {

    Contributor contributor = MainActivity.contributor;
    EditText nameField, emailField, ccnField, cvvField, monthField, yearField, postalField;
    private String name, email, creditCardNumber, creditCardCVV, creditCardExpiryMonth, creditCardExpiryYear, creditCardPostCode;

    private DataManager dataManager = new DataManager(new WebClient("10.0.2.2", 3001));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        nameField = findViewById(R.id.editText_Name);
        nameField.setText(contributor.getName());
        emailField = findViewById(R.id.editText_Email);
        emailField.setText(contributor.getEmail());
        ccnField = findViewById(R.id.edit_CCN);
        ccnField.setText(contributor.getCreditCardNumber());
        cvvField = findViewById(R.id.edit_CVV);
        cvvField.setText(contributor.getCreditCardCVV());
        monthField = findViewById(R.id.edit_ExpiryMonth);
        monthField.setText(contributor.getCreditCardExpiryMonth());
        yearField = findViewById(R.id.edit_ExpiryYear);
        yearField.setText(contributor.getCreditCardExpiryYear());
        postalField = findViewById(R.id.edit_PostalCode);
        postalField.setText(contributor.getCreditCardPostCode());

    }

    public void onUpdateInfoButtonClicked(View view) {
        // Toast.makeText(this, "Thanks for clicking!", Toast.LENGTH_LONG).show();

        name = nameField.getText().toString();
        email = emailField.getText().toString();
        creditCardNumber = ccnField.getText().toString();
        creditCardCVV = cvvField.getText().toString();
        creditCardExpiryMonth = monthField.getText().toString();
        creditCardExpiryYear = yearField.getText().toString();
        creditCardPostCode = postalField.getText().toString();

        if (name == null || email == null || creditCardNumber == null || creditCardCVV == null ||
                creditCardExpiryYear == null || creditCardExpiryMonth == null || creditCardPostCode == null) {
            Toast.makeText(this, "The required info could not be loaded. Please check connection.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (contributor.getName().equals(name) && contributor.getEmail().equals(email) && contributor.getCreditCardNumber().equals(creditCardNumber) &&
                    contributor.getCreditCardCVV().equals(creditCardCVV) && contributor.getCreditCardExpiryMonth().equals(creditCardExpiryMonth) &&
                    contributor.getCreditCardExpiryYear().equals(creditCardExpiryYear) && contributor.getCreditCardPostCode().equals(creditCardPostCode)) {
                Toast.makeText(this, "No info was changed!", Toast.LENGTH_LONG).show();
                return;
            }

            String contributorId = contributor.getId();
            Log.v("editUserInfo", contributorId);
            Contributor newContrib = dataManager.editUserInfo(contributorId, name, email, creditCardNumber, creditCardCVV, creditCardExpiryMonth, creditCardExpiryYear, creditCardPostCode);

            if (newContrib != null) {
                Toast.makeText(this, "Account information updated successfully!", Toast.LENGTH_LONG).show();
                MainActivity.contributor = newContrib;

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute( () -> {
                    try { Thread.sleep(3000); } catch (Exception e) { }
                    finish();
                });

            }
            else {
                Toast.makeText(this, "Sorry, something went wrong! Info could not be updated.", Toast.LENGTH_LONG).show();
            }

        } catch (IllegalStateException illegalStateException) {
            Toast.makeText(this, "Info could not be updated. Please check connection.", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "Info could not be updated. Please check your inputs and try again.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // e.printStackTrace();
            Toast.makeText(this, "An unexpected error occurred. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
