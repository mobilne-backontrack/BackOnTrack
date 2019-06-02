package pl.krakow.uek.view.auth.signup.step;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.krakow.uek.R;

public class LoginCredentialsStep extends Fragment implements BlockingStep {

    private EditText username;
    private EditText password;
    private EditText confirmationPassword;
    private EditText email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_credentials, container, false);
        initUsername(view);
        initPassword(view);
        initPasswordConfirmation(view);
        initEmail(view);
        return view;
    }


    private void initUsername(View view) {
        username = view.findViewById(R.id.username);
    }

    private void initPassword(View view) {
        password = view.findViewById(R.id.password);
    }

    private void initPasswordConfirmation(View view) {
        confirmationPassword = view.findViewById(R.id.password_confirmation);
    }

    private void initEmail(View view) {
        email = view.findViewById(R.id.email);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!validateUsername())
            return new VerificationError("Username verification error");
        if (!validatePassword())
            return new VerificationError("Password verification error");
        if (!validateEmail())
            return new VerificationError("Email address verification error");

        return null;
    }

    private boolean validateUsername() {
        if (username.getText() == null || username.getText().toString().isEmpty()) {
            Toast.makeText(getView().getContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText() == null || password.getText().toString().isEmpty()) {
            Toast.makeText(getView().getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (confirmationPassword.getText() == null || confirmationPassword.getText().toString().isEmpty()) {
            Toast.makeText(getView().getContext(), "Enter confirmation password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
        Matcher matcher = pattern.matcher(password.getText().toString());
        if (!matcher.find()) {
            Toast.makeText(getView().getContext(), "Incorrect password. \nMust contains one digit from 0-9, \nmin. one lowercase character, " +
                    "\nmin. one uppercase character, \nlength at least 6 characters and maximum of 20", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.getText().toString().equals(confirmationPassword.getText().toString())) {
            Toast.makeText(getView().getContext(), "Passwords are not the same!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        if (email.getText() == null || email.getText().toString().isEmpty()) {
            Toast.makeText(getView().getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email.getText().toString());
        if (!matcher.matches()) {
            Toast.makeText(getView().getContext(), "Incorrect email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error) {}

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        SharedPreferences userDetails = getContext().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        edit.putString("email", email.getText().toString());
        edit.putString("password", password.getText().toString());
        edit.apply();

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) { }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) { }
}
