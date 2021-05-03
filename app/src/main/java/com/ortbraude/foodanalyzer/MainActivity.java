package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText userNameText;
    EditText passwordText;
    TextView signUpTextView;
    ConstraintLayout backgroundLayout;
    ImageView logoImageView;
    TextView headlineTextView;
    TextView welcomeTextView;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER &&  event.getAction() == KeyEvent.ACTION_DOWN){
            closeKeyboard();
            logInClicked(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signUpTextView){
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.backgroundLayout || v.getId() == R.id.logoImageView || v.getId() == R.id.headlineTextView || v.getId() == R.id.welcomeTextView){
            closeKeyboard();
        }
    }

    private void closeKeyboard(){
        InputMethodManager inputMethodManager = ( InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void logInClicked(View v){

        if(userNameText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()){
            Toast.makeText(this, "A Username and a Password are required.", Toast.LENGTH_SHORT).show();
        }else{
            ParseUser.logInInBackground(userNameText.getText().toString(),passwordText.getText().toString(),new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        logIn();
                    } else if (user == null) {
                        Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong, try again later" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void logIn(){
        Intent intent = new Intent(getApplicationContext(), MainWindowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log In");

        userNameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.passwordText);
        signUpTextView = findViewById(R.id.signUpTextView);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        logoImageView = findViewById(R.id.logoImageView);
        headlineTextView = findViewById(R.id.headlineTextView);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        //set up sign up listener
        signUpTextView.setOnClickListener(this);

        //set up close keyboard listener
        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);
        headlineTextView.setOnClickListener(this);
        welcomeTextView.setOnClickListener(this);

        //set up keyboard listener
        passwordText.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null){
            System.out.println(ParseUser.getCurrentUser().getUsername());
            logIn();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

}