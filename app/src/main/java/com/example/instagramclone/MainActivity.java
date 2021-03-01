package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameET;
    EditText passwordET;
    TextView signUpTextView;
    boolean signUpMode = false;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpTextView) {
            Button loginButton = (Button) findViewById(R.id.loginButton);
            if (!signUpMode) {
                signUpMode = true;
                signUpTextView.setText("Login");
                loginButton.setText("Sign Up");
            } else {
                signUpMode = false;
                signUpTextView.setText("Sign Up");
                loginButton.setText("Login");
            }
        } else if (v.getId() == R.id.logoImageView || v.getId() == R.id.constraintLayout){
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void LoginClicked(View view){

        if(usernameET.getText().toString().matches("") || passwordET.getText().toString().matches("")){
            Toast.makeText(MainActivity.this,"Invalid Username or Password", Toast.LENGTH_SHORT).show();
        } else {
            if(signUpMode) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameET.getText().toString());
                user.setPassword(passwordET.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Who Signed Up ",user.getUsername());
                            Toast.makeText(MainActivity.this, user.getUsername()+" Signed Up", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(usernameET.getText().toString(), passwordET.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if( user != null){
                            Toast.makeText(MainActivity.this, user.getUsername()+" Logged In", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        ImageView logoImageView= (ImageView) findViewById(R.id.logoImageView);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        signUpTextView.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        constraintLayout.setOnClickListener(this);

        passwordET.setOnKeyListener(new View.OnKeyListener() {
             @Override
             public boolean onKey(View v, int keyCode, KeyEvent event) {
                 if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                     LoginClicked(v);
                 }
                 return false;
             }
         });

        if(ParseUser.getCurrentUser() != null){
            showUserList();
            Toast.makeText(MainActivity.this, ParseUser.getCurrentUser().getUsername()+" Logged In", Toast.LENGTH_SHORT).show();
        }

        // Check if logged in ?

        /*if (ParseUser.getCurrentUser() != null){
            Log.i("Signed In",ParseUser.getCurrentUser().getUsername());
        } else {
            Log.i("Signed In","ParseUser.getCurrentUser().getUsername()");

        }*/

        //ParseUser.logOut();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}