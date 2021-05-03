package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.parse.ParseUser;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    ProgressBar progressBar;
    EditText userNameTextSignUp;
    EditText firsNameTextSignUp;
    EditText lastNameTextSignUp;
    EditText emailTextSignUp;
    EditText passwordTextSignUp;
    EditText confirmPasswordTextSignUp;
    ConstraintLayout backgroundLayoutSignUp;
    ImageView logoImageViewSignUp;


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER &&  event.getAction() == KeyEvent.ACTION_DOWN){
            closeKeyboard();
            signUpClicked(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backgroundLayoutSignUp || v.getId() == R.id.logoImageViewSignUp){
            closeKeyboard();
        }
    }

    private void closeKeyboard(){
        InputMethodManager inputMethodManager = ( InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void signUpClicked(View v) {
        if(userNameTextSignUp.getText().toString().isEmpty() || firsNameTextSignUp.getText().toString().isEmpty()
                || lastNameTextSignUp.getText().toString().isEmpty() || emailTextSignUp.getText().toString().isEmpty()
                || passwordTextSignUp.getText().toString().isEmpty() || confirmPasswordTextSignUp.getText().toString().isEmpty())  {
            Toast.makeText(this, "You must fill all fields.", Toast.LENGTH_SHORT).show();
        }else if (!passwordTextSignUp.getText().toString().equals(confirmPasswordTextSignUp.getText().toString())){
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
        }else{
            signUp(userNameTextSignUp.getText().toString(), firsNameTextSignUp.getText().toString(),lastNameTextSignUp.getText().toString(),emailTextSignUp.getText().toString(),passwordTextSignUp.getText().toString());
        }
    }

    public void signUp(String username, String firstName, String lastName,String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.put("firstName",firstName);
        user.put("lastName",lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            if (e == null) {
                ParseUser.logOut();
                showAlert("Account Created Successfully!", "Please verify your email before Login", false);
            } else {
                ParseUser.logOut();
                showAlert("Error Account Creation failed", "Account could not be created" + " :" + e.getMessage(), true);
            }
        });
    }

    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    if (!error) {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        progressBar = findViewById(R.id.progressBarSignUp);
        userNameTextSignUp = findViewById(R.id.userNameTextSignUp);
        firsNameTextSignUp = findViewById(R.id.firsNameTextSignUp);
        lastNameTextSignUp = findViewById(R.id.lastNameTextSignUp);
        emailTextSignUp = findViewById(R.id.emailTextSignUp);
        passwordTextSignUp = findViewById(R.id.passwordTextSignUp);
        confirmPasswordTextSignUp = findViewById(R.id.confirmPasswordTextSignUp);
        backgroundLayoutSignUp = findViewById(R.id.backgroundLayoutSignUp);
        logoImageViewSignUp = findViewById(R.id.logoImageViewSignUp);

        //set up close keyboard listener
        backgroundLayoutSignUp.setOnClickListener(this);
        logoImageViewSignUp.setOnClickListener(this);

        //set up keyboard listener
        confirmPasswordTextSignUp.setOnKeyListener(this);
    }


}