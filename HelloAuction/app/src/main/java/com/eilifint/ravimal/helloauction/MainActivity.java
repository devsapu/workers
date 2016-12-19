package com.eilifint.ravimal.helloauction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.UserEntry;

public class MainActivity extends AppCompatActivity {

    /**
     * button to login
     */
    private Button mLogin;

    /**
     * Email EditText
     */
    private EditText mEmaill;

    /**
     * Password EditText
     */
    private EditText mPassword;

    /**
     * button to create account
     */
    private Button mCreateAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding view to use the list.xml layout file
        mCreateAcc = (Button) findViewById(R.id.create_acc_button);
        mLogin = (Button) findViewById(R.id.login_button);
        mEmaill = (EditText) findViewById(R.id.email_edit_text);
        mPassword = (EditText) findViewById(R.id.pw_edit_text);

        //button onclick listener to create account
        mCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to start
                Intent accountIntend = new Intent(MainActivity.this, UserSignUpActivity.class);
                startActivity(accountIntend);
            }
        });

        //login button onclick listener
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define a projection that specifies which columns from the database
                String[] projection = {
                        UserEntry._ID,
                        UserEntry.COLUMN_USER_NAME,

                };
                //email and password values
                String email = mEmaill.getText().toString().trim();
                String passwrd = mPassword.getText().toString().trim();
                //check email and password are not empty
                if (!TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(passwrd)) {
                    // Define a selection for where clause
                    String selection = UserEntry.COLUMN_USER_EMAIL + "=?" + " AND " +
                            UserEntry.COLUMN_USER_PASSWORD + "=?";
                    // Define a selection arguments for where clause
                    String[] selectionArgs = {email, passwrd};
                    // creating a Cursor for the data being displayed.
                    Cursor cursor = getContentResolver().query(
                            UserEntry.CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            null);
                    // check cursor has values
                    if (cursor.moveToFirst()) {
                        //get user id
                        int id = cursor.getInt(cursor.getColumnIndex(UserEntry._ID));
                        //get user id
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(getString(R.string.user), id);
                        editor.commit();
                        //start home activity
                        Intent accountIntend = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(accountIntend);

                    } else {
                        //login fail message
                        Toast.makeText(MainActivity.this, R.string.login_fail,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //login success message
                    Toast.makeText(MainActivity.this, getString(R.string.email_passwrd_required),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}