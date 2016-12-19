package com.eilifint.ravimal.helloauction;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.UserEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignUpActivity extends AppCompatActivity {

    /**
     * EditText field to enter the name
     */
    private EditText mNameEdit;
    /**
     * EditText field to enter the email address
     */
    private EditText mEmailEdit;

    /**
     * EditText field to enter password
     */
    private EditText mPasswordEdit;
    /**
     * button to sign up
     */
    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        // Find all relevant views that we will need to read user input from
        mNameEdit = (EditText) findViewById(R.id.admin_name_edit_text);
        mEmailEdit = (EditText) findViewById(R.id.admin_email_edit_text);
        mPasswordEdit = (EditText) findViewById(R.id.admin_password_edit_text);
        mSignUp = (Button) findViewById(R.id.admin_signup_button);

        //sign up button on click listener
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] projection = {
                        UserEntry._ID,
                        UserEntry.COLUMN_USER_NAME

                };

                String email = mEmailEdit.getText().toString().trim();
                // Define a selection for where clause
                String selection = UserEntry.COLUMN_USER_EMAIL + "=?";
                // Define a selection arguments for where clause
                String[] selectionArgs = {email};
                // creating a Cursor for the data being displayed.
                Cursor cursor = getContentResolver().query(UserEntry.CONTENT_URI, projection, selection, selectionArgs, null);
                if (cursor.moveToFirst()) {
                    Toast.makeText(UserSignUpActivity.this, R.string.email_already_exist,
                            Toast.LENGTH_SHORT).show();

                } else {
                    //insert user
                    insertUser();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserSignUpActivity.this);
                    int robot = preferences.getInt(getString(R.string.robot_created), 0);
                    if (robot == 0) {
                        insertRobot();
                    }

                }
            }
        });
    }

    String regex = "^(.+)@(.+)$";
    Pattern pattern = Pattern.compile(regex);

    /**
     * Helper method validate email.
     */
    public boolean emailValidate(String email) {
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else {
            Toast.makeText(this, R.string.correct_email,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Helper method to insert  user data into the database.
     */
    private void insertUser() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String name = mNameEdit.getText().toString().trim();
        String email = mEmailEdit.getText().toString().trim();
        String passwrd = mPasswordEdit.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and Toto's product attributes are the values.
        if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(passwrd)) {
            if (emailValidate(email)) {
                //content value to store key value pair
                ContentValues values = new ContentValues();
                values.put(UserEntry.COLUMN_USER_NAME, name);
                values.put(UserEntry.COLUMN_USER_EMAIL, email);
                values.put(UserEntry.COLUMN_USER_PASSWORD, passwrd);

                // Insert a new row for Toto in the database, returning the ID of that new row.
                Uri newUri = getContentResolver().insert(UserEntry.CONTENT_URI, values);
                // Show a toast message depending on whether or not the insertion was successful
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_user_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_user_successful),
                            Toast.LENGTH_SHORT).show();
                    // Exit activity
                    finish();
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.dont_miss),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Helper method to insert  robot user data into the database.
     */
    private void insertRobot() {
        String ROBOT_NAME = getString(R.string.robot_name);
        String ROBOT_EMAIL = getString(R.string.robot_email);
        String ROBOT_PW = getString(R.string.borot_passwrd);
        //content value to store key value pair
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER_NAME, ROBOT_NAME);
        values.put(UserEntry.COLUMN_USER_EMAIL, ROBOT_EMAIL);
        values.put(UserEntry.COLUMN_USER_PASSWORD, ROBOT_PW);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        Uri newUri = getContentResolver().insert(UserEntry.CONTENT_URI, values);
        if (newUri != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserSignUpActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(getString(R.string.robot), 1);
            editor.commit();
        }

    }

}
