package pl.krakow.uek.view.auth.signin;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import pl.krakow.uek.MainActivity;
import pl.krakow.uek.R;
import pl.krakow.uek.view.auth.signup.SignUpActivity;

public class SignInActivity extends AppCompatActivity {

   public void signUpButtonListener(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void signInButtonListener(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}

